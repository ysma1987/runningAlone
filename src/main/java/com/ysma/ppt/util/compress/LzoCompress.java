package com.ysma.ppt.util.compress;

import org.anarres.lzo.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * lzo压缩:LZO是致力于解压速度的一种数据压缩算法，
 * LZO是致力于解压速度的一种数据压缩算法，LZO 是 Lempel-Ziv-Oberhumer 的缩写。
 * 这个算法是无损算法，参考实现程序是线程安全的。 实现它的一个自由软件工具是lzop。现在 LZO 有用于 Perl、Python 以及 Java 的各种版本
 * https://blog.csdn.net/ihaveadream511/article/details/73822052
 *
 * LZO压缩算法采用(重复长度L，指回距离D)代替当前已经在历史字符串中出现过的字符串，
 * 其中，重复长度是指，后出现的字符串与先出现的字符串中连续相同部分的长度;指回距离是指，
 * 先后两个相同字符串之间相隔的距离(每个字节为一个单位);如果没出现过(定义为新字符)，
 * 则首先输出新字符的个数，再输出新字符。例如，待处理的字符串为“ABCDEFGHABCDEFJKLM”，
 * 压缩算法逐个处理字符，处理ABCDEFGH时没发现重复字符;处理到ABCDEF时发现这些字符在历史字符串中已经出现过，
 * 计算重复长度为6，指回距离(当前A离历史A的距离)为8，则用(6,8)代替ABCDEF;处理到JKLM时没发现重复字符，字符串到此处理完毕，
 * 则整个字符串被压缩成：(08)h ABCDEFGH(6,8)(04)h JKLM，其中h表示16进制
 */
public class LzoCompress extends AbstCompress implements ICompress {

    public static void main(String[] args) {
        LzoCompress lzoCompress = new LzoCompress();
        String a ="{\"extendDsList\":[{\"datasource\":{\"code\":\"first_internal_1\",\"createTime\":\"2019-10-09 14:58:21\",\"description\":\"行内第一数据源\",\"modifyTime\":\"2019-10-09 16:55:25\",\"name\":\"行内第一数据源\",\"providerCode\":\"internal_database\",\"providerInfo\":\"{\\\"jdbcUrl\\\":\\\"jdbc:mysql://10.2.1.106:3306/data_engine\\\",\\\"password\\\":\\\"Y1R1MTIzNDU2\\\",\\\"userName\\\":\\\"ctu\\\"}\",\"providerName\":\"数据聚合联调库\",\"providerType\":\"MYSQL\",\"querySql\":\"select code, name, url, source from name_list_info where code = #{code}\",\"serviceTtl\":7,\"serviceUrl\":\"\",\"status\":1,\"type\":4},\"paramReqList\":[{\"code\":\"name\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:30\",\"name\":\"名称\",\"required\":1},{\"code\":\"id\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"long\",\"modifyTime\":\"2019-10-09 15:01:34\",\"name\":\"身份证号\",\"required\":1}],\"paramResList\":[{\"code\":\"phone\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"int\",\"modifyTime\":\"2019-10-09 15:01:24\",\"name\":\"手机号\"},{\"code\":\"rank\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:19\",\"name\":\"排位分\"}],\"provider\":{\"code\":\"internal_database\",\"createTime\":\"2019-10-09 14:58:09\",\"modifyTime\":\"2019-10-09 14:58:09\",\"name\":\"数据聚合联调库\"}}],\"realDsList\":[]}";
        System.out.println(lzoCompress.unCompress(lzoCompress.compress(a)));
    }

    /**
     * LZO压缩特点：
     * 解压速度很快，并且很简单；
     * 解压时不需要内存支持；
     * 压缩的速度还不错；
     * 压缩时只需要 64 KiB 的内存支持；
     * 压缩比例可以根据需要调节，而这并不影响解压的效率，提高压缩比例自然会降低压缩速度；
     * 压缩包含了很多的压缩级别，提供很多选择；
     * 提供只需要 8 KiB 内存支持的压缩级别；
     * 提供线程安全；
     * 提供无损压缩
     */
    @Override
    public byte[] compress(byte[] data) throws IOException {
        //1.lzo压缩
        ByteArrayOutputStream baOs = new ByteArrayOutputStream();

        /**LZO1B适合处理大量的数据，或者有高冗余性的数据；LZO1F适合处理小量数据和二进制数据；
         * LZO1X适合各种环境；
         * LZO1Y 和 LZO1Z 跟 LZO1X 很相像，它们能够在一些环境中达到更好的压缩比例*/
        LzoOutputStream lzoOs = new LzoOutputStream(baOs,
                LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, LzoConstraint.COMPRESSION));
        lzoOs.write(data);
        lzoOs.flush();
        lzoOs.close();//关闭流 输出缓存区内容  否则解压时eof异常
        return baOs.toByteArray();
    }

    @Override
    public byte[] uncompress(byte[] data) throws IOException {
        ByteArrayOutputStream baOs = new ByteArrayOutputStream();
        ByteArrayInputStream baIs = new ByteArrayInputStream(data);

        LzoInputStream lzoIs = new LzoInputStream(baIs,
                LzoLibrary.getInstance().newDecompressor(LzoAlgorithm.LZO1X, LzoConstraint.COMPRESSION));
        byte[] temp = new byte[256];
            /*while (zipIs.read(temp) >=0){
                baOs.write(temp);
                1.当报文较大时，会存在冗余读，导致解压后出现冗余内容!
                2.原因在于倒数第二部分内容 为较长字符串时,内容超长
                3.当解析最后一部分内容时 由于内容较短，只覆盖了前N个长度的内容， 但是write是写了全部的
            }*/
        int n;
        while ((n = lzoIs.read(temp)) >= 0){
            baOs.write(temp, 0, n);
        }

        return baOs.toByteArray();
    }
}
