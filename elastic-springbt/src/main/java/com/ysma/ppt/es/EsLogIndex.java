package com.ysma.ppt.es;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * desc: 动态创建索引
 *
 * @author ysma
 * date : 2019/12/21 14:50
 */
@Component
public class EsLogIndex {

    //索引前缀
    public static final String PREFIX = "ysma_index-";

    /**索引别名*/
    public static final String ALIAS_NAME = "ysma_index-all";

    //外部触发变更日期
    public static String DATE = LocalDate.now().toString();

    /**查询使用的模糊索引*/
    public static String INDEX_QUERY = "ysma_index-*";

    public static String INDEX_TYPE = "ysma_es";

    public String indexType;

    //索引按天动态创建
    private String indexName;

    public String getIndexName() {
        return PREFIX + EsLogIndex.DATE;
    }

    public String getIndexType() {
        return INDEX_TYPE;
    }
}
