package com.amap.task.utils;

import com.amap.task.utils.HttpUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.deploy.panel.ITreeNode;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 红人快递通
 * Created by yang.hua on 14-4-1.
 */
public class GetKdtData {
    private static String encoderXml = "<KDT><REQUESTTYPE>26</REQUESTTYPE><APPID>gd</APPID><APPKEY>gd0325</APPKEY><TOKEN/><BUSSINESS><LONGITUDE>%s</LONGITUDE><LATITUDE>%s</LATITUDE></BUSSINESS></KDT>GDDT";
    private static String reqXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><KDT><REQUESTTYPE>26</REQUESTTYPE><APPID>gd</APPID><APPKEY>gd0325</APPKEY><SAFITY>%s</SAFITY><TOKEN/><BUSSINESS><LONGITUDE>%s</LONGITUDE><LATITUDE>%s</LATITUDE></BUSSINESS></KDT>";
    private static String URL = "http://www.kdt360.com/userxml.action";

    public static String encoderByMd5(String str)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        // 加密后的字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    public static String getParam(String lat, String lng) {
        String xml = String.format(encoderXml, lat, lng);
        try {
            return String.format(reqXml, encoderByMd5(xml), lat, lng);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getByCoordinate(String lat, String lng) throws IOException {
        return HttpUtils.postXml(URL, getParam(lat, lng), 60);
    }

    public static List<Map<String, Object>> extraXml(String xml) throws DocumentException {
        Document doc = DocumentHelper.parseText(xml);
        Element rootElement = doc.getRootElement();
        List<Element> list = rootElement.selectNodes("/KDT/BUSSINESS/LIST/ITEM");
        List<Map<String, Object>> result = Lists.newArrayList();
        for (Element element : list) {
            Map<String, Object> map = Maps.newHashMap();
            Iterator<Element> it = element.elementIterator();
            while (it.hasNext()) {
                Element e = it.next();
                map.put(e.getName(), e.getText());
            }
            result.add(map);
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            String result = getByCoordinate("116.523205", "39.922766");
            System.out.println(result);

            if (Strings.isNullOrEmpty(result) && result.length() > 20) {
                System.out.println(extraXml(result).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


}
