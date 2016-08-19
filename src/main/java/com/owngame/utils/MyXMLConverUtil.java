package com.owngame.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import weixin.popular.util.XMLConverUtil;

/**
 * 继承自Weixin popular 主要是增加了其对有嵌套XML的消息解析
 *
 * @author Administrator
 *
 */
public class MyXMLConverUtil extends XMLConverUtil {
	/**
	 * 转换简单的xml to map
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
	 *  递归解析xml
	 * @param node
	 * @param map
	 * @return
	 */
	private static Map<String, String> parseNode(Node node,
												 Map<String, String> map) {
//		System.out.println("node name:" + node.getNodeName());
		NodeList childNodes = node.getChildNodes();
		if(childNodes.getLength() == 1 || childNodes.getLength() == 0 ){
			map.put(node.getNodeName(), node.getTextContent());
			return map;
		}else{
			for (int i = 0; i < childNodes.getLength(); i++) {
				map = parseNode(childNodes.item(i), map);
			}
			map.put(node.getNodeName(), "hasChildren");
		}
		return map;
	}

}
