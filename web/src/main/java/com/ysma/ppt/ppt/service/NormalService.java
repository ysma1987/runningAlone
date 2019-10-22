package com.ysma.ppt.ppt.service;

import com.alibaba.fastjson.JSON;
import com.ysma.ppt.ppt.util.ThreadUtil;
import org.springframework.stereotype.Service;

@Service
public class NormalService {

    public String ask(String name){
        System.out.println(JSON.toJSON(ThreadUtil.getString()));
        return name + ":good";
    }

}
