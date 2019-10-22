package com.ysma.ppt.ppt.service.driver;

import com.ysma.ppt.IDxDriver;
import com.ysma.ppt.anno.DriverDescAnno;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author ysma 顶象类加载器 for driver 三方数据源驱动 2019-10-16
 * 需要进行"生殖隔离"  classloader per jar
 */
@Slf4j
public class DxClassLoader extends URLClassLoader{

    private static final String PACKAGE_INFO = "com.dingxianginc.obj.service.package-info";

    public DxClassLoader(URL[] urls) {
        super(urls);
    }

    /*public static void main(String[] args) throws Exception {
        File menu = new File("D:\\downloads\\dx-diy-driver.jar");
        URL url = menu.toURI().toURL();

        URL[] urls = new URL[]{url};
        System.out.println("====>"+urls[0].getPath());
        DxClassLoader dxClassLoader = new DxClassLoader(urls);

        *//*Class<?> clazz = dxClassLoader.findClass("com.dingxianginc.obj.service.DxDriver");
        IDxDriver dxDriver = (IDxDriver) clazz.newInstance();
        System.out.println("====>"+dxDriver.code());*//*

        Class cls = dxClassLoader.findClass(PACKAGE_INFO);
        DriverDescAnno dda = (DriverDescAnno) cls.getDeclaredAnnotation(DriverDescAnno.class);
        System.out.println("===========>"+dda.driverName());
    }*/

    /**获取加载的驱动*/
    public IDxDriver getDriver() throws ClassNotFoundException {
        //1.抓取包定义
        Class clz = this.findClass(PACKAGE_INFO);

        //2.抓取注解
        DriverDescAnno dda = (DriverDescAnno) clz.getDeclaredAnnotation(DriverDescAnno.class);

        //3 .加载驱动
        return loadDriver(dda.driverName());
    }

    /**加载驱动*/
    private IDxDriver loadDriver(String driverName){
        try {
            Class<?> clazz = findClass(driverName);
            if(clazz != null){
                return (IDxDriver) clazz.newInstance();
            }
        } catch (ClassNotFoundException e) {
            log.error("DxClassLoader.loadDriver 加载驱动{} 失败", driverName);
        } catch (IllegalAccessException e) {
            log.error("DxClassLoader.loadDriver 加载驱动{} 失败, 访问权限拒绝", driverName, e);
        } catch (InstantiationException e) {
            log.error("DxClassLoader.loadDriver 加载驱动{} 失败, 创建实例失败", driverName, e);
        }
        return null;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        //已加载库寻找
        Class<?> clazz = super.findLoadedClass(className);

        if(clazz == null){
            log.debug("自定义加载 className:{}", className);
            return super.findClass(className);
        }

        return clazz;
    }

}
