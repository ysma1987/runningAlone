package com.ysma.ppt.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysma.ppt.es.EsLogDto;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class LogEsSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogEsSupport.class);

    /**幸福消金流水号*/
    private static final String SERIAL_NO_XFXJ = "applySerialNo";

    /**网贷H5流水号*/
    private static final String SERIAL_NO_H5 = "bizTrackNo";

    /**H5网贷专项处理*/
    public static void wrapH5(Object[] args, EsLogDto eld, Object... objs){
        //1.apiCode 赋值 索引项
        eld.setApiCode((String)args[0]);

        //2.入参request项 无索引
        String apiParam = (String)args[1];
        eld.setReq(apiParam);

        //3.id项 主键
        String id = assignH5Id(apiParam);
        eld.setId( id == null? UUID.randomUUID().toString() : id);

        //4.异常或者返参 无索引
        assignRespOrErr(eld, objs);
    }

    /**幸福消金专项处理*/
    public static void wrapXfxj(Object[] args, EsLogDto eld, Object... objs) {
        //1.apiCode 索引项
        eld.setApiCode((String)args[0]);

        //2.入参request项 无索引
        String apiParam = (String)args[1];
        eld.setReq(apiParam);

        //3.id项 主键
        String id = assignXfxjId(apiParam);
        eld.setId( id == null? UUID.randomUUID().toString() : id);

        //4.异常或者返参 无索引
        assignRespOrErr(eld, objs);
    }

    /**网贷H5 流水号抓取 xml解析*/
    private static String assignH5Id(String apiParam){
        String id = null;
        try {
            JSONObject obJson = JSON.parseObject(apiParam);
            id = obJson.getString(SERIAL_NO_H5);
        } catch (Exception e) {
            LOGGER.error("LogEsSupport.assignXfxjId H5 流水号获取失败, apiParam:{}", apiParam, e);
        }
        return id;
    }

    /**幸福消金 流水号抓取 json解析*/
    private static String assignXfxjId(String apiParam){
        String id = null;
        try {
            JSONObject obJson = JSON.parseObject(apiParam);
            id = obJson.getString(SERIAL_NO_XFXJ);
        } catch (Exception e) {
            LOGGER.error("LogEsSupport.assignXfxjId 幸福消金 流水号获取失败, apiParam:{}", apiParam, e);
        }
        return id;
    }

    private static void assignRespOrErr(EsLogDto eld, Object... objs){
        if(objs.length > 0){
            Object obj = objs[0];
            if(obj instanceof Throwable){//运行时异常
                Throwable t = (Throwable)obj;
                eld.setErrorCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                eld.setErrorMsg(t.getMessage());
            } else {//无异常
                eld.setRes(JSON.toJSONString(obj));
            }
        } else {
            //void方法 且 无异常
        }
    }
}
