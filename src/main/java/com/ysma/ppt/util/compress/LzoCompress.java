package com.ysma.ppt.util.compress;

/**
 * lzo压缩
 * LZO是致力于解压速度的一种数据压缩算法，
 * LZO是Lempel-Ziv-Oberhumer的缩写，这个算法是无损算法
 */
public class LzoCompress extends AbstCompress implements ICompress {
    @Override
    public byte[] compress(byte[] data) {
        return new byte[0];
    }

    @Override
    public byte[] uncompress(byte[] data) {
        return new byte[0];
    }
}
