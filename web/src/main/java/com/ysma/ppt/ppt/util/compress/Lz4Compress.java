package com.ysma.ppt.ppt.util.compress;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * lz4压缩：LZ4是一种无损数据压缩算法，着重于压缩和解压缩速度
 * Yann Collet 在2011年发明了LZ4压缩算法。
 * LZ4算法虽然没有middle out那么牛逼得无死角，但在能保证一定压缩率的情况下，它以它无敌的压缩速度和更快的解压速度著称
 * 如果一句话概括LZ4：LZ4就是一个用16k大小哈希表储存字典并简化检索的LZ77
 *
 */
public class Lz4Compress extends AbstCompress implements ICompress {

    public static void main(String[] args) {
        Lz4Compress lz4Compress = new Lz4Compress();
        String a ="{\"extendDsList\":[{\"datasource\":{\"code\":\"first_internal_1\",\"createTime\":\"2019-10-09 14:58:21\",\"description\":\"行内第一数据源\",\"modifyTime\":\"2019-10-09 16:55:25\",\"name\":\"行内第一数据源\",\"providerCode\":\"internal_database\",\"providerInfo\":\"{\\\"jdbcUrl\\\":\\\"jdbc:mysql://10.2.1.106:3306/data_engine\\\",\\\"password\\\":\\\"Y1R1MTIzNDU2\\\",\\\"userName\\\":\\\"ctu\\\"}\",\"providerName\":\"数据聚合联调库\",\"providerType\":\"MYSQL\",\"querySql\":\"select code, name, url, source from name_list_info where code = #{code}\",\"serviceTtl\":7,\"serviceUrl\":\"\",\"status\":1,\"type\":4},\"paramReqList\":[{\"code\":\"name\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:30\",\"name\":\"名称\",\"required\":1},{\"code\":\"id\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"long\",\"modifyTime\":\"2019-10-09 15:01:34\",\"name\":\"身份证号\",\"required\":1}],\"paramResList\":[{\"code\":\"phone\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"int\",\"modifyTime\":\"2019-10-09 15:01:24\",\"name\":\"手机号\"},{\"code\":\"rank\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:19\",\"name\":\"排位分\"}],\"provider\":{\"code\":\"internal_database\",\"createTime\":\"2019-10-09 14:58:09\",\"modifyTime\":\"2019-10-09 14:58:09\",\"name\":\"数据聚合联调库\"}}],\"realDsList\":[]}";
        System.out.println(lz4Compress.unCompress(lz4Compress.compress(a)));
    }

    @Override
    public byte[] compress(byte[] data) throws IOException {

        try {
            //1.zip压缩
            ByteArrayOutputStream baOs = new ByteArrayOutputStream();
            LZ4BlockOutputStream lz4Os = new LZ4BlockOutputStream(baOs);
            lz4Os.write(data);
            lz4Os.flush();
            lz4Os.close();//关闭流 输出缓存区内容  否则解压时eof异常

            //2.获取并转义压缩内容
            return baOs.toByteArray();
        } catch (IOException e) {
            throw e;
        }

    }

    @Override
    public byte[] uncompress(byte[] data) throws IOException {
        try {
            //1.zip解压缩
            ByteArrayOutputStream baOs = new ByteArrayOutputStream();
            ByteArrayInputStream baIs = new ByteArrayInputStream(data);
            LZ4BlockInputStream zipIs = new LZ4BlockInputStream(baIs);

            byte[] temp = new byte[256];
            /*while (zipIs.read(temp) >=0){
                baOs.write(temp);
                1.当报文较大时，会存在冗余读，导致解压后出现冗余内容!
                2.原因在于倒数第二部分内容 为较长字符串时,内容超长
                3.当解析最后一部分内容时 由于内容较短，只覆盖了前N个长度的内容， 但是write是写了全部的
            }*/
            int n;
            while ((n = zipIs.read(temp)) >= 0){
                baOs.write(temp, 0, n);
            }

            return baOs.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }
}
