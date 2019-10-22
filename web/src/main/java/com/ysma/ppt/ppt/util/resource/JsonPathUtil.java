package com.ysma.ppt.ppt.util.resource;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;
import com.ysma.ppt.ppt.intf.pojo.TemplateDO;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ysma 2019-09-25
 * JsonPath工具类
 * JsonPath表达式可以使用点表示法:$.store.book[0].title
 *                  或括号表示法:$['store']['book'][0]['title']
 *
 * real_param_response表path字段存储格式仿点表示法,如store.book[1].isbn
 */
public class JsonPathUtil {

    //JsonPath中的“根成员对象”始终称为$，无论是对象还是数组
    private static final String ROOT_PREFIX = "$";

    private static Configuration configuration;

    static {
        configuration = Configuration.builder().options(
                Option.DEFAULT_PATH_LEAF_TO_NULL, // 如果路径不存在则返回null,而不要抛出PathNotFoundException
                Option.SUPPRESS_EXCEPTIONS // 抑制异常的抛出，当设置了Option.ALWAYS_RETURN_LIST时返回[],否则返回null
        ).jsonProvider(new JsonSmartJsonProvider()).build();
    }

    /**
     * 解析类
     * @param resJsonStr 待解析的返参对象
     * @param expectList 定义的预期结果集合
     * @return 结果集
     */
    public static Map<String, Object> parseJson(String resJsonStr, List<BeanMap> expectList){
        /*1.此处预先解析json,默认请情下JsonPath.read方法每掉一次都会重新解析json，此处预先解析好就不用每次都进行解析*/
        DocumentContext context = JsonPath.parse(resJsonStr, configuration);

        //2.构造返回结果
        Map<String, Object> resultMap = new HashMap<>();

        expectList.forEach(beanMap -> {
            String path = String.join(".", ROOT_PREFIX, (String)beanMap.get("path"));

            //beanMap.get("dataType") 数据类型的作用弱化了
            Object val = context.read(path);
            resultMap.put((String)beanMap.get("code"), val);
        });

        return resultMap;
    }

    /**groovy脚本中可使用此定制开发*/
    public static Map<String, Object> parsePathJson(String resJsonStr, List<Map<String, String>> pathList){
        /*1.此处预先解析json,默认请情下JsonPath.read方法每掉一次都会重新解析json，此处预先解析好就不用每次都进行解析*/
        DocumentContext context = JsonPath.parse(resJsonStr, configuration);

        //2.构造返回结果
        Map<String, Object> resultMap = new HashMap<>();

        pathList.forEach(pathMap -> {
            String path = String.join(".", ROOT_PREFIX, pathMap.get("path"));

            //beanMap.get("dataType") 数据类型的作用弱化了
            Object val = context.read(path);
            resultMap.put(pathMap.get("code"), val);
        });

        return resultMap;
    }

    /**
     * https://www.baeldung.com/guide-to-jayway-jsonpath
     * 官网地址,可查询过滤器定义功能等
     */
    private static void testParse(String resJsonStr, List<BeanMap> expectList){
        Object obj = configuration.jsonProvider().parse(resJsonStr);

        expectList.forEach(beanMap -> {
            String path = String.join(".", ROOT_PREFIX, (String)beanMap.get("path"));
            Object read = JsonPath.read(obj, path, Filter.filter(Criteria.where("price").lt(5.5)));
            System.out.println("read:"+read);
        });
    }

    public static void main(String[] args) {
        List<TemplateDO> responseDOS = new ArrayList<>();
        TemplateDO rd = new TemplateDO();
        rd.setCode("color");
        rd.setPath("store.bicycle[?]");
        rd.setDataType("double");
        responseDOS.add(rd);

        /*ParamResponseRealDO rd2 = new ParamResponseRealDO();
        rd2.setCode("category");
        rd2.setPath("hehe.store.book[*].category");
        rd2.setDataType("array");
        responseDOS.add(rd2);*/

        List<BeanMap> expectList = responseDOS.stream().map(BeanMap::create).collect(Collectors.toList());

        String respJson = getRespJson();

        /*Map<String, Object> resultMap = parseJson(respJson, expectList);
        System.out.println(JSON.toJSONString(resultMap));*/

        testParse(respJson, expectList);
    }

    private static String getRespJson(){
        return  "{ \"store\": {\n" +
                "    \"book\": [ \n" +
                "      { \"category\": \"reference\",\n" +
                "        \"author\": \"Nigel Rees\",\n" +
                "        \"title\": \"Sayings of the Century\",\n" +
                "        \"price\": 8.95\n" +
                "      },\n" +
                "      { \"category\": \"fiction\",\n" +
                "        \"author\": \"Evelyn Waugh\",\n" +
                "        \"title\": \"Sword of Honour\",\n" +
                "        \"price\": 12.99\n" +
                "      },\n" +
                "      { \"category\": \"fiction\",\n" +
                "        \"author\": \"Herman Melville\",\n" +
                "        \"title\": \"Moby Dick\",\n" +
                "        \"isbn\": \"0-553-21311-3\",\n" +
                "        \"price\": 8.99\n" +
                "      },\n" +
                "      { \"category\": \"fiction\",\n" +
                "        \"author\": \"J. R. R. Tolkien\",\n" +
                "        \"title\": \"The Lord of the Rings\",\n" +
                "        \"isbn\": \"0-395-19395-8\",\n" +
                "        \"price\": 22.99\n" +
                "      }\n" +
                "    ],\n" +
                "    \"bicycle\": {\n" +
                "      \"color\": \"red\",\n" +
                "      \"price\": 19.95\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
