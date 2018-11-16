package com.wtjr.lqg.utils;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 选择地区
 * 
 * @author dell
 * 
 */
public class SelectAddressUtil {
	/**
	 * DOM解析得到省
	 * 
	 * @return listProvince省集合
	 * @throws Exception
	 */
	public static ArrayList<String> domXMLProvince(Context context)
			throws Exception {
		ArrayList<String> listProvince = new ArrayList<String>();
		NodeList nodeList = nodeList(context);
		// 循环遍历出所有子节点
		for (int i = 0; i < nodeList.getLength(); i++) {
			// 得到节点名
			Node n2 = nodeList.item(i);
			if (n2.getNodeName().equals("State")) {
				// 得到每个节点的属性
				NamedNodeMap nodeMap = n2.getAttributes();

				Node nAtt = nodeMap.item(0);

				String name = nAtt.getNodeName();
				String value = nAtt.getNodeValue();
				if (name.equals("Name")) {
					listProvince.add(value);
				}
			}
		}
		return listProvince;
	}

	/**
	 * DOM解析根据省的到城市
	 * 
	 * @return listCity城市集合
	 * @throws Exception
	 */
	public static ArrayList<String> domXMLCitys(Context context, String province)
			throws Exception {
		ArrayList<String> listCity = new ArrayList<String>();

		NodeList nodeList = nodeList(context);
		// 循环遍历出所有子节点
		for (int i = 0; i < nodeList.getLength(); i++) {
			// 得到节点名
			Node n2 = nodeList.item(i);
			if (n2.getNodeName().equals("State")) {
				// 得到每个节点的属性
				NamedNodeMap nodeMap = n2.getAttributes();
				Node nAtt = nodeMap.item(0);
				String value = nAtt.getNodeValue();
				// 如果选择的省存在，就返回对应的城市
				if (value.equals(province)) {
					// 获得2级子节点
					NodeList childs = n2.getChildNodes();
					for (int j = 0; j < childs.getLength(); j++) {
						Node ch = childs.item(j);
						if (ch.getNodeName().equals("City")) {
							NamedNodeMap nodeMap2 = ch.getAttributes();
							Node nAtt1 = nodeMap2.item(0);
							String value1 = nAtt1.getNodeValue();
							listCity.add(value1);
						}
					}
				}
			}
		}
		return listCity;
	}

	/**
	 * 返回一级节点
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static NodeList nodeList(Context context) throws Exception {
		// Assets中的资源文件是能使用流的方式操作
		// 得到Assets资源管理器对象
		AssetManager manage = context.getResources().getAssets();
		// 获得资源的输入流
		InputStream ins = manage.open("loclist.xml");

		// 第一步，实例化文档解析对象工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 第二步：使用工厂得到解析对象
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 解析xml文件，得到文档对象
		Document doc = builder.parse(ins);
		// 得到头结点
		NodeList node = doc.getElementsByTagName("CountryRegion");
		// 得到第一个头结点
		Node n = node.item(0);
		// 获得1级子节点
		NodeList nodeList = n.getChildNodes();

		return nodeList;
	}
}
