package com.ysma.ppt.groovy
/** @author ysma 2019-10-09*/
import com.ysma.ppt.util.JsonPathUtil

//定义path路径和code
def pathList = [
        //定义常规节点获取
        ["code": "color", "path": "store.bicycle.color"],
        //定义数组节点获取
        ["code": "author", "path": "store.book[*].author"],
        //定义数组特别节点获取
        ["code": "title", "path": "store.book[?(@.category=='reference')].title"]
]

//返参报文通过ctx获取
if(resJson){
    return JsonPathUtil.parsePathJson(resJson, pathList)
} else {
    println("脚本resJson 绑定失败,未获取到")
    return null
}