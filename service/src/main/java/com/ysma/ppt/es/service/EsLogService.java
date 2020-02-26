package com.ysma.ppt.es.service;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ysma.ppt.es.EConsumer;
import com.ysma.ppt.es.EsLogDto;
import com.ysma.ppt.es.EsLogIndex;
import com.ysma.ppt.es.repository.EsAggRepository;
import com.ysma.ppt.helper.LogEsSupport;
import com.ysma.ppt.intf.AnnLog;
import com.ysma.ppt.intf.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 查询类方法请注意
 */
@Slf4j
@Service
public class EsLogService implements ILogService {

    //500ms循环清空一次,单台机器tps>=10000时,esQueue才有被offer失败的可能
    private static BlockingQueue<EsLogDto> esQueue = new LinkedBlockingDeque<>(5000);

    private static final ExecutorService executorService = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("esLog-%d")
                    .setDaemon(true)//守护线程
                    .build()
    );

    //ES日志
    private static final Logger LOGGER = LoggerFactory.getLogger("logEs");

    private final EsAggRepository esAggRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public EsLogService(EsAggRepository esAggRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.esAggRepository = esAggRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    /**
     * id查询 唯一
     * @param id 流水号
     * @return 日志
     */
    public EsLogDto queryById(String id){
        //查询条件
        Criteria criteria = new Criteria().or("_id").is(id);

        //查询载体
        CriteriaQuery query = wrapQueryObj(criteria);

        //查询结果
        return elasticsearchTemplate.queryForObject(query, EsLogDto.class);

    }

    /**
     * 根据apiCode接口查询
     * @param apiCode 接口
     * @param page 页码 es搜索默认第一页页码是0
     * @param size 分页大小
     * @return 分页列表
     */
    public Page<EsLogDto> queryByApiCode(String apiCode, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("addTime").descending());

        QueryBuilder queryBuilder = QueryBuilders.termsQuery("apiCode", apiCode);

        NativeSearchQuery query = wrapQueryPage(queryBuilder, pageable);

        return elasticsearchTemplate.queryForPage(query, EsLogDto.class);
    }

    /**
     * 时间范围查询
     * @param begin 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end 结束时间 yyyy-MM-dd HH:mm:ss
     * @param page 页码 es搜索默认第一页页码是0
     * @param size 分页大小
     * @return 分页列表
     */
    public Page<EsLogDto> queryByAddTime(String begin, String end, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            QueryBuilder queryBuilder = QueryBuilders
                    .rangeQuery("addTime")
                    .gte(begin)
                    .lt(end);

            NativeSearchQuery query = wrapQueryPage(queryBuilder, pageable);

            return elasticsearchTemplate.queryForPage(query, EsLogDto.class);
        } catch (Exception e){
            log.error("EsLogService.queryByAddTime es查询时间异常，begin:{}, end:{}", begin, end, e);
        }
        return null;
    }


    /**单项查询*/
    private CriteriaQuery wrapQueryObj(Criteria criteria){
        CriteriaQuery query = new CriteriaQuery(criteria);
        query.addIndices(EsLogIndex.ALIAS_NAME);
        query.addTypes(EsLogIndex.INDEX_TYPE);
        return query;
    }

    /**列表查询*/
    private NativeSearchQuery wrapQueryPage(QueryBuilder query, Pageable pageable){
        return new NativeSearchQueryBuilder()
                .withTypes(EsLogIndex.INDEX_TYPE)
                .withIndices(EsLogIndex.ALIAS_NAME)
                .withQuery(query)
                .withPageable(pageable)
                .build();
    }

    /**==================================================================*/

    @SuppressWarnings("unused")
    public Iterable<EsLogDto> queryAll(){
        return esAggRepository.findAll();
    }

    @SuppressWarnings("unused")
    public Iterable<EsLogDto> saveAll(List<EsLogDto> list){
        return esAggRepository.saveAll(list);
    }

    /**
     * 索引是否存在
     * 创建api操作索引
     */
    public <T> boolean indexExist(Class<T> clazz){
        return elasticsearchTemplate.indexExists(clazz);
    }

    /**
     * 索引重建
     * 创建api操作索引
     */
    public <T> boolean indexReAdd(Class<T> clazz){
        if(elasticsearchTemplate.indexExists(clazz)){
            elasticsearchTemplate.deleteIndex(clazz);
        }

        return elasticsearchTemplate.createIndex(clazz);
    }

    public boolean indexDel(String indexName){
        return elasticsearchTemplate.deleteIndex(indexName);
    }

    /**为索引添加别名*/
    public boolean indexAlias(String indexName){
        return elasticsearchTemplate.addAlias(new AliasBuilder()
                .withIndexName(indexName)
                .withAliasName(EsLogIndex.ALIAS_NAME)
                .build());
    }

    /*==================================================================*/

    /**
     * 处理日志
     * @param args 参数[apiCode,apiParam]
     * @param annLog 注解
     * @param objs 返参-异常 互斥操作
     */
    public void log(Object[] args, AnnLog annLog, long costTime, Object... objs){
        EsLogDto dto = wrap(args, annLog.source(), objs);
        dto.setAddTime(new Date());
        dto.setCostTime(costTime);
        if(annLog.esOn()){
            esQueue.offer(dto);//非阻塞入队列
        }
        LOGGER.info(JSON.toJSONString(dto));
    }

    private EsLogDto wrap(Object[] args, EConsumer source, Object... objs) {
        EsLogDto eld = new EsLogDto();
        switch (source){
            case XFXJ: {
                LogEsSupport.wrapXfxj(args, eld, objs);
                break;
            }
            case H5:
                LogEsSupport.wrapH5(args, eld, objs);
                break;
            default:
                eld.setId(UUID.randomUUID().toString());
                eld.setErrorMsg("渠道ES日志待扩展");
                break;
        }
        return eld;
    }

    /**
     * 1s执行两次,tps>=1000时 单次才有大于500的批量数据
     * 暂不做分批处理
     */
    @Scheduled(fixedDelay = 500)
    public void out(){
        try {
            if(!esQueue.isEmpty()){
                final int capacity = esQueue.size();
                List<EsLogDto> logDtoList = new ArrayList<>(capacity);
                esQueue.drainTo(logDtoList, capacity);
                //异步线程执行，释放当前定时任务执行线程
                executorService.execute(()->{
                    try {
                        esAggRepository.saveAll(logDtoList);
                    } catch (Exception e) {
                        log.error("com.ysma.ppt.es 批量导入异常", e);
                    }
                });
            }
        } catch (Exception e) {
            log.error("输出es日志异常", e);
        }
    }

}
