package com.ysma.ppt.ppt.util.compress;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * Snappy压缩
 * Snappy（以前称Zippy）是Google基于LZ77的思路
 * 用C++语言编写的快速数据压缩与解压程序库，并在2011年开源。
 * 它的目标并非最大压缩率或与其他压缩程序库的兼容性，而是非常高的速度和合理的压缩率。
 */
public class SnappyCompress extends AbstCompress implements ICompress {

    public static void main(String[] args) {
        SnappyCompress snappyCompress = new SnappyCompress();
        String a ="{\"extendDsList\":[{\"datasource\":{\"code\":\"first_internal_1\",\"createTime\":\"2019-10-09 14:58:21\",\"description\":\"行内第一数据源\",\"modifyTime\":\"2019-10-09 16:55:25\",\"name\":\"行内第一数据源\",\"providerCode\":\"internal_database\",\"providerInfo\":\"{\\\"jdbcUrl\\\":\\\"jdbc:mysql://10.2.1.106:3306/data_engine\\\",\\\"password\\\":\\\"Y1R1MTIzNDU2\\\",\\\"userName\\\":\\\"ctu\\\"}\",\"providerName\":\"数据聚合联调库\",\"providerType\":\"MYSQL\",\"querySql\":\"select code, name, url, source from name_list_info where code = #{code}\",\"serviceTtl\":7,\"serviceUrl\":\"\",\"status\":1,\"type\":4},\"paramReqList\":[{\"code\":\"name\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:30\",\"name\":\"名称\",\"required\":1},{\"code\":\"id\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"long\",\"modifyTime\":\"2019-10-09 15:01:34\",\"name\":\"身份证号\",\"required\":1}],\"paramResList\":[{\"code\":\"phone\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"int\",\"modifyTime\":\"2019-10-09 15:01:24\",\"name\":\"手机号\"},{\"code\":\"rank\",\"createTime\":\"2019-10-09 14:58:21\",\"dataType\":\"string\",\"modifyTime\":\"2019-10-09 15:01:19\",\"name\":\"排位分\"}],\"provider\":{\"code\":\"internal_database\",\"createTime\":\"2019-10-09 14:58:09\",\"modifyTime\":\"2019-10-09 14:58:09\",\"name\":\"数据聚合联调库\"}}],\"realDsList\":[]}";
        System.out.println(snappyCompress.unCompress(snappyCompress.compress(a)));
    }

    @Override
    public byte[] compress(byte[] data) throws IOException {
        return Snappy.compress(data);
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SnappyOutputStream sos = new SnappyOutputStream(baos);
        sos.write(data);
        sos.flush();
        sos.close();
        return baos.toByteArray();*/
    }

    @Override
    public byte[] uncompress(byte[] data) throws IOException {
        return Snappy.uncompress(data);
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        SnappyInputStream sis = new SnappyInputStream(bais);
        int n;
        byte[] buff = new byte[256];
        while ((n = sis.read(buff)) >= 0){
            baos.write(buff, 0, n);
        }
        return baos.toByteArray();*/
    }
}
