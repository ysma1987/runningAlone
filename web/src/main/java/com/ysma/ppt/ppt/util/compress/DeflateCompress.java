package com.ysma.ppt.ppt.util.compress;

import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.InflaterInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Deflate 压缩
 * DEFLATE是同时使用了LZ77算法与哈夫曼编码（Huffman Coding）的一个无损数据压缩算法，
 * DEFLATE压缩与解压的源代码可以在自由、通用的压缩库zlib上找到，
 * zlib官网：http://www.zlib.net/ jdk中对zlib压缩库提供了支持，
 * 压缩类Deflater和解压类Inflater，Deflater和Inflater都提供了native方法。
 */
public class DeflateCompress extends AbstCompress implements ICompress {

    public static void main(String[] args) throws IOException {
        DeflateCompress deflateCompress = new DeflateCompress();
        String a ="{\"extendDsList\":[{\"datasource\":{\"code\":\"first_internal_1\",\"createTime\":\"2019-10-09 14:58:21\",\"description\":\"行内第一数据源\",\"modifyTime\":\"2019-10-09 16:55:25\",\"name\":\"行内第一数据源\",\"providerCode\":\"internal_database\",\"providerInfo\":\"{\\\"jdbcUrl\\\":\\\"jdbc:mysql://10.2.1.106:3306/data_engine\\\",\\\"password\\\":\\\"Y1R1MTIzNDU2\\\",\\\"userName\\\":\\\"ctu\\\"}\",\"providerName\":\"数据聚合联调库\",\"providerType\":\"MYSQL\",\"querySql\":\"select code, name, url, source from name_list_info where code = #{code}\",\"serviceTtl\":7,\"serviceUrl\":\"\",\"status\":1,\"type\":4},\"paramReqList\":[{\"code\":\"name\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:30\",\"name\":\"名称\",\"required\":1},{\"code\":\"id\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"long\",\"modifyTime\":\"2019-10-09 15:01:34\",\"name\":\"身份证号\",\"required\":1}],\"paramResList\":[{\"code\":\"phone\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"int\",\"modifyTime\":\"2019-10-09 15:01:24\",\"name\":\"手机号\"},{\"code\":\"rank\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:19\",\"name\":\"排位分\"}],\"provider\":{\"code\":\"internal_database\",\"createTime\":\"2019-10-09 14:58:09\",\"modifyTime\":\"2019-10-09 14:58:09\",\"name\":\"数据聚合联调库\"}}],\"realDsList\":[]}";
        System.out.println(deflateCompress.unCompress(deflateCompress.compress(a)));
    }

    @Override
    public byte[] compress(byte[] data) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DeflaterOutputStream dos = new DeflaterOutputStream(baos);
            dos.write(data);
            dos.flush();
            dos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public byte[] uncompress(byte[] data) throws IOException {
        try {
            ByteArrayOutputStream baOs = new ByteArrayOutputStream();
            ByteArrayInputStream baIs = new ByteArrayInputStream(data);
            InflaterInputStream deflateIs = new InflaterInputStream(baIs);

            int n;
            byte[] buff = new byte[256];
            while ((n = deflateIs.read(buff)) >= 0){
                baOs.write(buff, 0, n);
            }

            return baOs.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }
}
