package com.owngame.utils;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weixin.popular.util.XMLConverUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 继承自Weixin popular 主要是增加了其对有嵌套XML的消息解析
 * <p>
 * 由于其修复了Bug 此处暂时废弃 20161122
 *
 * @author Administrator
 */
public class MyXMLConverUtil extends XMLConverUtil {
    /**
     * 转换简单的xml to map
     *
     * @param xml
     * @return
     */
    public static Map<String, String> convertToMap(String xml) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xml);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            if (root != null) {
                NodeList childNodes = root.getChildNodes();
                if (childNodes != null && childNodes.getLength() > 0) {
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node node = childNodes.item(i);
                        if (node != null
                                && node.getNodeType() == Node.ELEMENT_NODE) {
                            map = parseNode(node, map);
                        }
                    }
                }
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 不知道怎么的会解析出这种节点，只好先删除了
        map.remove("#text");
        return map;
    }


    /**
     * 递归解析xml
     *
     * @param node
     * @param map
     * @return
     */
    private static Map<String, String> parseNode(Node node,
                                                 Map<String, String> map) {
        NodeList childNodes = node.getChildNodes();
        if (childNodes.getLength() == 1 || childNodes.getLength() == 0) {
            map.put(node.getNodeName(), node.getTextContent());
            return map;
        } else {
            for (int i = 0; i < childNodes.getLength(); i++) {
                map = parseNode(childNodes.item(i), map);
            }
            map.put(node.getNodeName(), "hasChildren");
        }
        return map;
    }

}
