package com.ysma.ppt;

import com.ysma.ppt.es.EsLogDto;
import com.ysma.ppt.es.EsLogIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.AliasBuilder;
import org.springframework.stereotype.Component;

/**
 * ES启动检查
 * 完成当天索引的创建
 */
@Slf4j
@Component
public class InitListener implements ApplicationListener {

    private final ElasticsearchTemplate elasticsearchTemplate;

    private final EsLogIndex esLogIndex;

    public InitListener(ElasticsearchTemplate elasticsearchTemplate, EsLogIndex esLogIndex) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.esLogIndex = esLogIndex;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //创建索引
        try {
            String indexName = esLogIndex.getIndexName();
            boolean exist = elasticsearchTemplate.indexExists(indexName);
            if(exist){
                log.debug("渠道集市接入系统 已创建日志索引，不再创建");
            } else {
                elasticsearchTemplate.createIndex(indexName);
                elasticsearchTemplate.addAlias(new AliasBuilder()
                        .withIndexName(indexName)
                        .withAliasName(EsLogIndex.ALIAS_NAME)
                        .build());
                elasticsearchTemplate.putMapping(EsLogDto.class);
                log.debug("渠道集市接入系统 创建ES日志索引");

            }
        } catch (Exception e) {
            log.error("渠道集市接入系统 创建日志索引异常", e);
        }
    }
}
