package com.ysma.ppt.service.file;

import com.ysma.ppt.service.driver.DxDriversSimpleManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/** 
 * 自定义文件监听器 
 * @author   ysma
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class FileListener extends FileAlterationListenerAdaptor{

    private String configDir;

    @Override
    public void onStart(FileAlterationObserver observer) {
        log.debug("FileListener 启动 observer:{}", observer.toString());
    }

    @Override
    public void onDirectoryCreate(File directory) {
        log.error("FileListener [违规新建目录]:path:{}", directory.getPath());
    }

    @Override
    public void onDirectoryChange(File directory) {
        log.error("FileListener [违规修改目录]:path:{}", directory.getPath());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        log.error("FileListener [违规删除目录]:path:{}", directory.getPath());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        log.debug("FileListener 停止 observer:{}", observer.toString());
    }

    @Override
    public void onFileCreate(File file) {
        log.info("FileListener [新建]:path:{}", file.getPath());
        if(file.getName().endsWith(".jar")){
            refreshProperties();
            log.info("{}-文件新增,重新加载jar包:{}", file.getName(), configDir);
        }
    }

    /**
     * properties配置文件的修改才会重新加载文件,
     * groovy脚本的更新--也需要同步更新引用定义的properties文件
     * @param file 配置文件
     */
    @Override  
    public void onFileChange(File file) {
        log.info("FileListener [修改]:path:{}", file.getPath());
        if(file.getName().endsWith(".jar")){
            refreshProperties(file.getPath());
            log.info("jar包修改,重新加载jar包:{}", file.getName());
        }
    }

    @Override  
    public void onFileDelete(File file) {
        log.info("FileListener [删除]:path:{},name:{}", file.getPath(), file.getName());
        //refreshProperties(); TODO 暂无直接删除的业务
        //log.info("{}-jar包删除,重新加载jar包:{}", file.getName(), configDir);
    }

    private void refreshProperties(String... filePaths){
        try {
            //全部清空 重新加载jar包
            DxDriversSimpleManager.deregisterAll(configDir);
        } catch (Exception e) {
            log.error("FileListener 刷新配置文件失败,path:{}", filePaths, e);
        }
    }
}