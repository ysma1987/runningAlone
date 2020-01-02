package com.ysma.ppt.es.repository;

import com.ysma.ppt.es.EsLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ysma
 * ES 日志dao
 */
public interface EsAggRepository extends ElasticsearchRepository<EsLogDto, String> {

    /*调试代码 不推荐直接使用
    List<EsLogDto> findByApiCode(String apiCode);

    List<EsLogDto> findByApiCodeStartsWith(String apiCode);

    //yyyy-MM-dd HH:mm:ss
    List<EsLogDto> findByAddTimeBetween(String begin, String end);*/

    /**
     * 分页查询-接口code
     * @param apiCode 接口
     * @param pageable es搜索默认第一页页码是0
     * @return 分页
     */
   Page<EsLogDto> findByApiCode(String apiCode, Pageable pageable);

    /**
     * 根据时间查询  UTC时间 实现类负责转换
     * @param begin yyyy-MM-dd HH:mm:ss
     * @param end yyyy-MM-dd HH:mm:ss
     * @param pageable es搜索默认第一页页码是0
     * @return 分页
     */
   Page<EsLogDto> findDistinctByAddTimeBetweenOrderByAddTimeDesc(String begin, String end, Pageable pageable);

}
