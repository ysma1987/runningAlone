package com.ysma.ppt.service;

import com.alibaba.fastjson.JSON;
import com.ysma.ppt.util.ThreadUtil;
import org.springframework.stereotype.Service;

@Service
public class NormalService {

    public String ask(String name){
        System.out.println(JSON.toJSON(ThreadUtil.getString()));
        return name + ":good";
    }

}
