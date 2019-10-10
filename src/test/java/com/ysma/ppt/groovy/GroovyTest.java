package com.ysma.ppt.groovy;

import com.alibaba.fastjson.JSON;
import com.ysma.ppt.PptApplication;
import com.ysma.ppt.service.groovy.GroovyScriptEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)//导入spring测试框架[2]
@SpringBootTest(classes = PptApplication.class)//提供spring依赖注入
public class GroovyTest {

    @Autowired
    private GroovyScriptEngine groovyScriptEngine;

    @Test
    public void testTemplate(){
        String respJson = getRespJson();
        Map<String, Object> map = new HashMap<>();
        map.put("resJson", respJson);
        String script = getScript();
        Object scriptResult = groovyScriptEngine.execute(script, map);
        if(scriptResult instanceof Map){
            System.out.println(JSON.toJSONString(scriptResult));
        } else {
            System.out.println("脚本解析返参不为Map<String, Object>类型");
        }
    }

    private String getScript(){

        String script ="import com.ysma.ppt.util.JsonPathUtil\n" +
                "\n" +
                "def pathList = [\n" +
                "        [\"code\": \"color\", \"path\": \"store.bicycle.color\"],\n" +
                "        [\"code\": \"author\", \"path\": \"store.book[*].author\"],\n" +
                "        [\"code\": \"title\", \"path\": \"store.book[?(@.category=='reference')].title\"]\n" +
                "]\n" +
                "\n" +
                "if(resJson){\n" +
                "    return JsonPathUtil.parsePathJson(resJson, pathList)\n" +
                "} else {\n" +
                "    println(\"脚本resJson 绑定失败,未获取到\")\n" +
                "    return null\n" +
                "}";
        return script;
    }

    private String getRespJson(){
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
