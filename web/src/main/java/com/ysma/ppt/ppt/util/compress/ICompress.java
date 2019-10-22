package com.ysma.ppt.ppt.util.compress;

import java.io.IOException;

/**
 * @author ysma 接口 定义共性标准
 */
public interface ICompress {

    //压缩
    byte[] compress(byte[] data) throws IOException;

    //解压缩
    byte[] uncompress(byte[] data) throws IOException;
}
