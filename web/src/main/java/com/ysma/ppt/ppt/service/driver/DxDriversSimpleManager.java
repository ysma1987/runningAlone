package com.ysma.ppt.ppt.service.driver;

import com.ysma.ppt.IDxDriver;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ysma 驱动管理器
 */
@Slf4j
public class DxDriversSimpleManager {

    private final static ConcurrentHashMap<String, IDxDriver> registeredDrivers = new ConcurrentHashMap<>();

    private DxDriversSimpleManager(){
    }

    public static void LoaderJars(String path){
        try {
            URL[] urls = loadUrls(path);
            for(URL url : urls){
                DxClassLoader dxClassLoader = new DxClassLoader(new URL[]{url});
                IDxDriver driver = dxClassLoader.getDriver();
                registeredDrivers.put(driver.code(), driver);
                log.info("自定义类加载 成功 文件:{}", url.getPath());
            }
        } catch (FileNotFoundException e) {
            log.error("自定义类加载失败 文件不存在:{}", path);
        } catch (MalformedURLException e) {
            log.error("自定义类加载失败 MalformedURLException", e);
        } catch (ClassNotFoundException e) {
            log.error("自定义类加载失败 驱动缺失", e);
        }

        log.info("=========jar包加载完成:{}==========", path);
    }

    /**卸载所有驱动-重新加载*/
    public static synchronized void deregisterAll(String jarPath){

        //1.失效注册管理器 直到成功
        registeredDrivers.clear();
        log.info("deregisterAll 驱动引用清理完成");

        //2.重新加载
        LoaderJars(jarPath);
    }

    public static synchronized void clearDrivers(){
        //1.失效注册管理器 直到成功
        registeredDrivers.clear();
        log.info("clearDrivers 驱动引用清理完成");
    }

    /**获取驱动*/
    public static IDxDriver getDriver(String driverCode){
        return registeredDrivers.get(driverCode);
    }

    /**加载jar包*/
    private static URL[] loadUrls(String jarPath) throws FileNotFoundException, MalformedURLException {
        //1.jar包列表
        List<URL> urlList = new ArrayList<>();

        //2.递归遍历 目录下的jar包
        File menu = new File(jarPath);
        if(!menu.exists()){
            log.error("加载目录不存在, jarPath:{}", jarPath);
            throw new FileNotFoundException();
        }
        loopJarFiles(menu, urlList);

        //3.转换
        return urlList.toArray(new URL[0]);
    }

    /**
     * 递归加载jar包
     */
    private static void loopJarFiles(File file, List<URL> urlList) throws MalformedURLException {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files == null){
                log.error("加载目录为空目录, menu:{}", file.getName());
            } else {
                for(File subFile : files){
                    loopJarFiles(subFile, urlList);
                }
            }
        } else {
            String path = file.getAbsolutePath();
            if(path.endsWith(".jar")){
                log.info("加载驱动 file:{}", path);
                urlList.add(file.toURI().toURL());
            } else {
                log.error("file:{} 文件不为jar包 忽略", path);
            }
        }
    }
}
