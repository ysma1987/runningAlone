package com.ysma.ppt.nio;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Description 读写demo
 * Date 2020/7/27 11:47
 *
 * @author by mays
 */
public class FileRwDemo {

    private static final String inPath = "D:\\ysma\\test.txt";

    private static final String outPath = "D:\\ysma\\_copy.txt";

    private static final String outPath2 = "D:\\ysma\\_copy2.txt";

    public static void main(String[] args) {
        /*nioVersion();
        ioVersion();*/
        nioVersion2();
    }

    /**
     * io版本
     */
    private static void ioVersion(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File file = new File(inPath);
            fis = new FileInputStream(file);
            fos = new FileOutputStream(new File(outPath2));
            IOUtils.copy(fis, fos);
        } catch (IOException ignore) {
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * nio版本
     * transferTo复制出来的文件，大小只有2G
     *   因为：int var8 = (int)Math.min(var3, 2147483647L);
     * transferFrom复制出来的文件，大小同原文件
     */
    private static void nioVersion() {
        FileChannel in = null, out = null;
        try {
            in = FileChannel.open(Paths.get(inPath), StandardOpenOption.READ);
            out = FileChannel.open(Paths.get(outPath), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            long size = in.size();
            in.transferTo(0, size, out);
        } catch (IOException ignore) {
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 解除transferTo复制的2G限制
     * 零拷贝应用
     */
    private static void nioVersion2(){
        FileChannel in = null, out = null;
        try {
            in = FileChannel.open(Paths.get(inPath), StandardOpenOption.READ);
            out = FileChannel.open(Paths.get(outPath), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            long size = in.size();
            out.transferFrom(in, 0, size);
        } catch (IOException ignore) {
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

}
