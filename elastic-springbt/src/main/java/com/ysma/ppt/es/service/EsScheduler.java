package com.ysma.ppt.es.service;

import com.ysma.ppt.es.EsLogDto;
import com.ysma.ppt.es.EsLogIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.AliasBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * desc: ES定时器
 *
 * @author ysma
 * date : 2019/12/30 9:48
 */
@Slf4j
@Component
public class EsScheduler {

    private final ElasticsearchTemplate elasticsearchTemplate;

    /**构造器注入*/
    public EsScheduler(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    /**每天0点自增索引*/
    @Scheduled(cron = "0 0 0 * * ?")
    private void autoChange(){
        //1.当天索引并赋值
        EsLogIndex.DATE = LocalDate.now().toString();
        String indexName = EsLogIndex.PREFIX + EsLogIndex.DATE;

        boolean exist = elasticsearchTemplate.indexExists(indexName);
        if(!exist){
            elasticsearchTemplate.createIndex(indexName);
            elasticsearchTemplate.addAlias(new AliasBuilder()
                    .withIndexName(indexName)
                    .withAliasName(EsLogIndex.ALIAS_NAME)
                    .build());
            elasticsearchTemplate.putMapping(EsLogDto.class);
            log.info("渠道集市接入系统 创建ES日志索引:{}", indexName);
        }
    }

    /**每天1点删除索引*/
    @Scheduled(cron = "0 0 1 * * ?")
    private void autoDelete(){
        LocalDate dayOfMonthBefore = LocalDate.now().minusMonths(1);

        String indexName = EsLogIndex.PREFIX + dayOfMonthBefore.toString();
        do{
            try {
                boolean flag = elasticsearchTemplate.deleteIndex(indexName);
                if(flag){
                    log.info("EsScheduler.autoDelete 定期清理索引:{} 成功", indexName);
                } else {
                    log.info("EsScheduler.autoDelete 定期清理索引:{} 失败", indexName);
                }
            } catch (Exception ex) {
                log.error("EsScheduler.autoDelete 定期清理索引:{} 异常", indexName, ex);
            }

            //日期前移 索引存在则继续删除
            dayOfMonthBefore = dayOfMonthBefore.minusDays(1);
            indexName = EsLogIndex.PREFIX + dayOfMonthBefore.toString();
        }while (elasticsearchTemplate.indexExists(indexName));
    }

}
