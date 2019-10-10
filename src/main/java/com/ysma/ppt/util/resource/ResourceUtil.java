package com.ysma.ppt.util.resource;

import com.ysma.ppt.exception.CustomException;
import com.ysma.ppt.intf.constants.BusinessExceptConstants;
import com.ysma.ppt.intf.constants.PptConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;

@Slf4j
public class ResourceUtil {

    /**
     * 获取文件路径
     * @param path 文件路径
     * @return URL
     */
    public static URL getResourcePath(String path) {
        try {
            //1.以Linux路径为准
            path = path.replaceAll(PptConstants.PATH_WINDOWS, PptConstants.PATH_LINUX);

            /*
              2.依据开头自主选择加载方法
              第一：前面有 "/" 代表了工程的根目录,例如工程名叫做myproject,"/"代表了myproject
              第二：前面没有 "" 代表当前类的目录
             */
            return path.startsWith(PptConstants.PATH_LINUX) ?
                    ResourceUtil.class.getResource(path) :
                    ResourceUtil.class.getClassLoader().getResource(path);
        } catch (Exception e) {
            log.error("ResourceUtil.getResourcePath 文件不存在, path:{}", path);
            throw new CustomException(BusinessExceptConstants.FILE_LACK, path + "文件不存在");
        }
    }

    /**
     * 获取文件
     * @see #getResourcePath(String path)
     */
    public static File getResourceFile(String path) {

        URL url = getResourcePath(path);

        return new File(url.getPath());
    }
}
