package com.ysma.ppt.util.compress;

import java.io.IOException;

/**
 * bzip压缩
 * bzip2是Julian Seward开发并按照自由软件／开源软件协议发布的数据压缩算法及程序。
 * Seward在1996年7月第一次公开发布了bzip2 0.15版，
 * 在随后几年中这个压缩工具稳定性得到改善并且日渐流行，
 * Seward在2000年晚些时候发布了1.0版。
 * bzip2比传统的gzip的压缩效率更高，但是它的压缩速度较慢
 */
public class BzipCompress extends AbstCompress implements ICompress{
    @Override
    public byte[] compress(byte[] data) throws IOException {
        return new byte[0];
    }

    @Override
    public byte[] uncompress(byte[] data) throws IOException {
        return new byte[0];
    }
}
