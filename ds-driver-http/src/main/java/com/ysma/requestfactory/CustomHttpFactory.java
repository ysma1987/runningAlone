package com.ysma.requestfactory;

import com.alibaba.fastjson.JSONObject;
import com.ysma.commonutil.HttpClientUtil;
import com.ysma.constant.NameListConstant;
import com.ysma.ppt.params.DxRequestModel;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author Vi_error @Date: 19-2-22 下午1:54
 * @author ysma 2019-10-21
 */

public class CustomHttpFactory {

  private static final Logger log = LoggerFactory.getLogger(CustomHttpFactory.class);

  private static final String encoding = "UTF-8";


  public String doPost(DxRequestModel requestModel) {
    JSONObject jsonObject = new JSONObject();
    String url = requestModel.getApiUrl();
    HttpPost httpPost = new HttpPost(url);
    httpPost = setPost(httpPost);
    Object object = JSONObject.toJSON(requestModel.getArgsMap());
    StringEntity stringEntity = new StringEntity(object.toString(), Charset.forName(encoding));
    stringEntity.setContentEncoding(encoding);
    // 发送Json格式的数据请求
    stringEntity.setContentType("application/json");
    httpPost.setEntity(stringEntity);
    try {
      CloseableHttpResponse response =
              HttpClientUtil.getHttpClient(url).execute(httpPost, HttpClientContext.create());
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != 200) {
        log.error("query  http service  exception,  http status code is {}, url:{}", statusCode, url);
        jsonObject.put("code", NameListConstant.HTTP_EXCEPTION_CODE);
        jsonObject.put("msg", NameListConstant.HTTP_EXCEPTION_MSG);
        return jsonObject.toJSONString();
      }
      HttpEntity entity = response.getEntity();
      String result = EntityUtils.toString(entity, encoding);
      EntityUtils.consume(entity);
      return result;
    } catch (Exception e) {
      log.error("query  http service  exception, {}", e.getMessage());
      jsonObject.put("code", NameListConstant.HTTP_EXCEPTION_CODE);
      jsonObject.put("msg", NameListConstant.HTTP_EXCEPTION_MSG);
      return jsonObject.toJSONString();
    }
  }

  /**
   * 设置普通http请求的post，不支持鉴权
   */
  public HttpPost setPost(HttpPost httpPost) {
    httpPost.addHeader("Content-type", "application/json");
    return httpPost;
  }

}
