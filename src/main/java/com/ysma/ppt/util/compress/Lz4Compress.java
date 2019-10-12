package com.ysma.ppt.util.compress;

import java.io.IOException;

/**
 * lz4压缩
 * LZ4是一种无损数据压缩算法，着重于压缩和解压缩速度
 */
public class Lz4Compress extends AbstCompress implements ICompress {
    @Override
    public byte[] compress(byte[] data) throws IOException {
        return new byte[0];
    }

    @Override
    public byte[] uncompress(byte[] data) throws IOException {
        return new byte[0];
    }
}
