package com.ysma.ppt.util.mail;

import lombok.Data;

/**
 * desc: 邮件实体类
 *
 * @author ysma
 * date : 2020/1/15 17:27
 */
@Data
public class EmailEntity {
    private String serviceName;//服务名称

    private String host;

    private String port;

    private String account;

    private String password;
}
