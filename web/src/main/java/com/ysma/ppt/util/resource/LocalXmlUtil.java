package com.ysma.ppt.util.resource;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

import java.io.File;

@Slf4j
public class LocalXmlUtil {

    //请求报文地址
    private static final String XML_REQ_PATH = "/config/model/req.resource";

    //响应报文地址
    private static final String XML_RES_PATH = "/config/model/res.resource";

    public static void main(String[] args) {
        /*Document doc = getReqModel();
        Element element = doc.getRootElement();
        System.out.println(element.getName());
        System.out.println(element.element("bizTrackNo").getText());*/

        /*Document doc = getResModel();
        Element element = doc.getRootElement();
        element.element("code").setText("500");
        element.element("message").setText("xml解析失败");
        Element ipE = element.element("serverIp");
        ipE.addAttribute("desc", "入参未传递serverIp");
        element.element("returnTime").setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println(doc.asXML());*/

        /*String resource = "<?resource version=\"1.0\" encoding=\"UTF-8\"?><controlData><transBranch>交易机构-必填</transBranch><transMedium>渠道号-必填</transMedium><transSeqNo>渠道流水号-必填</transSeqNo><transTeller>柜员号-选填</transTeller><sysIndicator>柜员号-选填</sysIndicator><servcId>柜员号-选填</servcId><termId>终端设备编号-选填</termId><userId>用户ID-选填</userId><reqTime>服务调用时间-必填</reqTime><bizTrackNo>业务跟踪号-必填</bizTrackNo><pageSize>分页参数,本页记录数-选填</pageSize><pageIndex>分页参数,页码-选填</pageIndex><hostIp>服务调用方IP-必填</hostIp></controlData>";
        try {
            Document doc = parseXml(resource);
            System.out.println(doc.asXML());

            System.out.println(JSON.toJSONString(resource));
        } catch (DocumentException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 解析xml
     * @param xml resp resource
     * @return doc
     */
    public static Document parseXml(String xml) throws DocumentException {
        try {
           return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("LocalXmlUtil.parseXml 解析异常 resource:{}", xml, e);
            throw e;
        }
    }

    /**
     * 请求报文模板解析
     */
    public static Document getReqModel() {
        return getDocument(XML_REQ_PATH);
    }

    /**
     * 响应报文模板解析
     */
    public static Document getResModel() {
        return getDocument(XML_RES_PATH);
    }

    private static Document getDocument(String xml_soap_path) {

        try {
            File xmlFile = ResourceUtil.getResourceFile(xml_soap_path);
            SAXReader saxReader = new SAXReader();
            return saxReader.read(xmlFile);
        } catch (DocumentException e) {
            log.error("LocalXmlUtil.getDocument dom解析DocumentException异常", e);
        } catch (Exception e) {
            log.error("LocalXmlUtil.getDocument dom文件读取Exception异常", e);
        }
        return null;
    }
}
