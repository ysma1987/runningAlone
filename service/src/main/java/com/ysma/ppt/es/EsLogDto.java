package com.ysma.ppt.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * com.ysma.ppt.es 映射文件
 */
@Data
@Document(indexName = "#{esLogIndex.indexName}", type = "#{esLogIndex.indexType}", shards = 3)
public class EsLogDto implements Serializable {

    //流水号
    @Id
    private String id;

    //接口
    @Field(type = FieldType.Keyword)
    private String apiCode;

    @Field(type = FieldType.Text, index = false)
    private String req;

    @Field(type = FieldType.Text, index = false)
    private String res;

    @Field(type = FieldType.Integer, index = false)
    private Integer errorCode;

    @Field(type = FieldType.Text, index = false)
    private String errorMsg;

    //jackson来格式化日期
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;

    //耗时
    @Field(type = FieldType.Long, index = false)
    private long costTime;

}
