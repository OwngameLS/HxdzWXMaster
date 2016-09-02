package com.owngame.utils;

import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 信息转换格式、解析的工具类
 *
 * @author Administrator
 */
public class InfoFormatUtil {

    /**
     * xml转为map集合 所有从微信服务器Post过来的消息都封装成了xml格式，需要将其转换为Map格式，用于分析或者组建其他格式的消息用于返回
     *
     * @param s
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String, String> xmlToMap(String s)
            throws IOException, DocumentException {
        Map<String, String> map;
        map = MyXMLConverUtil.convertToMap(s);
        return map;
    }

    /**
     * 将inputStream转换为String的方法
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        return IOUtils.toString(is, "UTF-8");


//		System.out.println("is null @ inputStream2String:" + (is.toString()));
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int i = -1;
//		while ((i = is.read()) != -1) {
//			baos.write(i);
//		}
//		String s = baos.toString("UTF-8");
//		baos.close();
//		return s;
    }

}
