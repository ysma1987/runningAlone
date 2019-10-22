package com.ysma.ppt.ppt.service.file;

import com.ysma.ppt.ppt.service.driver.DxDriversSimpleManager;
import com.ysma.ppt.ppt.util.resource.VariableConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ysma
 * 文件监听配置器
 */
@Slf4j
@Configuration
public class FileListenerConfig {

    //轮询间隔 5s
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(5L);

    @Autowired
    private VariableConfig variableConfig;

    private List<FileAlterationObserver> observers = new ArrayList<>();

    public FileListenerConfig() {
    }

    @PostConstruct
    public void init() {
        //1.获取根目录
        String dir = this.monitorRoot();
        //2.构建观察者
        this.monitorDir(dir);
        //3.启动监听
        this.monitorOn();
        //4.初始化加载
        DxDriversSimpleManager.LoaderJars(dir);
    }

    /**
     * 根目录获取
     * @return root
     */
    private String monitorRoot() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        assert url != null;
        String path = url.getPath();
        String configDir = this.variableConfig.getFilePath();
        return configDir.startsWith("/") ? path + configDir.substring(1) : path + configDir;
    }

    /**
     * 构建观察者
     * @param dir 递归目录
     */
    public void monitorDir(String dir) {
        File resource = new File(dir);
        if (resource.isDirectory()) {
            File[] files = resource.listFiles();
            if (files != null) {
                 for(File file : files){
                     this.monitorDir(file.getPath());
                }
            }

            FileAlterationObserver observer = observeFile(dir);
            this.observers.add(observer);
            log.info("监听文件目录:{}", dir);
        }

    }

    /**
     * 构造Observer
     * @param path 路径
     * @return Observer
     */
    public FileAlterationObserver observeFile(String path) {
        FileAlterationObserver observer = new FileAlterationObserver(path,
                FileFilterUtils.or(
                        FileFilterUtils.fileFileFilter(),
                        FileFilterUtils.directoryFileFilter()));
        observer.addListener(new FileListener(path));
        return observer;
    }

    /**
     * 启动监听
     */
    public void monitorOn() {
        try {
            FileAlterationMonitor monitor = new FileAlterationMonitor(
                    INTERVAL,
                    observers.toArray(new FileAlterationObserver[0]));
            monitor.start();
        } catch (Exception ex) {
            log.error("FileListenerConfig.wrapSimple 监听失败", ex);
        }
    }
}
