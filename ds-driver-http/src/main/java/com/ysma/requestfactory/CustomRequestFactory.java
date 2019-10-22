package com.ysma.requestfactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysma.constant.NameListConstant;
import com.ysma.ppt.params.DxRequestModel;
import com.ysma.ppt.params.DxResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vi_error @Date: 19-2-22
 * @author ysma 2019-10-21
 */
public class CustomRequestFactory {

  private static final Logger log = LoggerFactory.getLogger(CustomRequestFactory.class);

  public DxResponseModel customRequestExcutor(DxRequestModel requestModel) {

    DxResponseModel dxResponseModel = new DxResponseModel();
    CustomHttpFactory customHttpFactory = new CustomHttpFactory();

    try {
      String json = customHttpFactory.doPost(requestModel);
      JSONObject jsonObject = JSONObject.parseObject(json);

      if (!NameListConstant.HTTP_EXCEPTION_CODE.equals(jsonObject.getString("code"))) {
        dxResponseModel.setResCode(NameListConstant.HTTP_QUERY_SUC_CODE);
        dxResponseModel.setResMsg(NameListConstant.QUERY_SUC_MSG);
      } else {
        dxResponseModel.setResCode(jsonObject.getString("code"));
        dxResponseModel.setResMsg(jsonObject.getString("msg"));
      }
      dxResponseModel.setResponse(json);
      log.info("查询请求:{},查询结果:{}", JSON.toJSONString(requestModel), json);
      return dxResponseModel;
    } catch (Exception e) {
      log.error("query  http service  exception, request:{}", JSON.toJSONString(requestModel),  e);
      dxResponseModel.setResCode(NameListConstant.HTTP_EXCEPTION_CODE);
      dxResponseModel.setResMsg(NameListConstant.HTTP_EXCEPTION_MSG);
      return dxResponseModel;
    }
  }
}
