package cn.ttsales.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * DomUtils包含了用于简化XML文档对象模型操作的帮助方法， 这些方法通过调用JAXP实现DOM对象的检索或者执行XPath查询。
 * 
 * @author Gray
 * 
 */
public abstract class DomUtil {

	/**
	 * 获取JAXP DocumentBuider对象
	 * 
	 * @param namespace
	 *            是否支持名称空间
	 * @param validate
	 *            是否在解析文档时进行验证
	 * @return
	 */
	public static DocumentBuilder newDocumentBuilder(boolean namespace,
			boolean validate) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(validate);
			factory.setNamespaceAware(namespace);
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			// 更改默认EntityResolver的行为，以使documentBuilder在解析实体时不使用网络资源
			documentBuilder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId,
						String systemId) {
					return new InputSource(new StringReader(""));
				}
			});
			return documentBuilder;
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从InputSource指定的源装载一个Document对象
	 * 
	 * @param input
	 * @param namespace
	 *            是否支持名称空间
	 * @param validate
	 *            是否在解析文档时进行验证
	 * @return
	 */
	public static Document loadDocument(InputSource input, boolean namespace,
			boolean validate) {
		try {
			return newDocumentBuilder(namespace, validate).parse(input);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从InputStream指定的源装载一个Document对象
	 * 
	 * @param input
	 * @param namespace
	 *            是否支持名称空间
	 * @param validate
	 *            是否在解析文档时进行验证
	 * @return
	 */

	public static Document loadDocument(InputStream input, boolean namespace,
			boolean validate) {
		return loadDocument(new InputSource(input), namespace, validate);
	}

	/**
	 * 从包含XML内容的字符串装载一个Document对象
	 * 
	 * @param inputXml
	 *            包含XML内容的字符串
	 * @param namespace
	 *            是否支持名称空间
	 * @param validate
	 *            是否在解析文档时进行验证
	 * @return
	 */
	public static Document loadDocument(String inputXml, boolean namespace,
			boolean validate) {
		return loadDocument(new InputSource(new StringReader(inputXml)),
				namespace, validate);
	}

	/**
	 * 从InputStream指定的源装载一个Document对象,忽略名称空间并在解析文档时忽略验证
	 * 
	 * @param input
	 * @return
	 */
	public static Document loadDocument(InputStream input) {
		return loadDocument(input, false, false);
	}

	/**
	 * 从InputSource指定的源装载一个Document对象,忽略名称空间并在解析文档时忽略验证
	 * 
	 * @param input
	 * @return
	 */
	public static Document loadDocument(InputSource input) {
		return loadDocument(input, false, false);
	}

	/**
	 * 从包含XML内容的字符串装载一个Document对象,忽略名称空间并在解析文档时忽略验证
	 * 
	 * @param inputXml
	 *            包含XML内容的字符串
	 * @return
	 */
	public static Document loadDocument(String inputXml) {
		return loadDocument(inputXml, false, false);
	}

	/**
	 * 从InputStream指定的源装载一个Document对象,支持名称空间，在解析文档时忽略验证
	 * 
	 * @param input
	 * @return
	 */
	public static Document loadDocumentNS(InputStream input) {
		return loadDocument(input, true, false);
	}

	/**
	 * 从InputSource指定的源装载一个Document对象,支持名称空间，在解析文档时忽略验证
	 * 
	 * @param input
	 * @return
	 */
	public static Document loadDocumentNS(InputSource input) {
		return loadDocument(input, true, false);
	}

	/**
	 * 从包含XML内容的字符串装载一个Document对象,支持名称空间，在解析文档时忽略验证
	 * 
	 * @param inputXml
	 *            包含XML内容的字符串
	 * @return
	 */
	public static Document loadDocumentNS(String inputXml) {
		return loadDocument(inputXml, true, false);
	}

	/**
	 * 将一个Document对象保存到Result指定的输出中
	 * 
	 * @param dom
	 * @param output
	 */
	public static void saveDocument(Document dom, Result output) {
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.transform(new DOMSource(dom), output);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将一个Document对象保存到OutputStream指定的输出流中
	 * 
	 * @param dom
	 * @param output
	 */
	public static void saveDocument(Document dom, OutputStream output) {
		saveDocument(dom, new StreamResult(output));
	}

	/**
	 * 获取Node的值，对Element返回Element对象的文本值， 对其他类型的节点则返回其getNodeValue方法的调用返回值
	 * 
	 * @param node
	 * @return
	 * 
	 * @see #getTextValue(Element)
	 */
	public static <T extends Node> String value(T node) {
		if (node == null)
			return null;
		return node instanceof Element ? getTextValue((Element) node) : node
				.getNodeValue();
	}

	/**
	 * 获取Element对象的文本值。
	 * Element对象的文本值是指Element中包含的所有文本类型数据(如普通文本和CDATA数据)和解释后的实体引用文本
	 * 
	 * @param valueEle
	 * @return
	 */
	public static String getTextValue(Element valueEle) {
		StringBuilder sb = new StringBuilder();
		NodeList nl = valueEle.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node item = nl.item(i);
			if ((item instanceof CharacterData && !(item instanceof Comment))
					|| item instanceof EntityReference) {
				sb.append(item.getNodeValue());
			}
		}
		return sb.toString();
	}

	/**
	 * 从NodeList提取所有节点到一个List对象中
	 * 
	 * @param nl
	 * @return
	 */
	public static <T extends Node> List<T> list(NodeList nl) {
		return list(nl, null);
	}

	/**
	 * 从NodeList提取所有指定类型的节点到一个List对象中
	 * 
	 * @param nl
	 * @param nodeClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> List<T> list(NodeList nl, Class<T> nodeClass) {
		List<T> ret = new ArrayList<T>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (nodeClass == null
					|| (nodeClass != null && nodeClass.isInstance(node)))
				ret.add((T) node);
		}
		return ret;
	}

	/**
	 * 获取元素的第一个子元素，如果指定了tagNames,则获取第一个满足标记名称条件的子元素
	 * 
	 * @param ele
	 * @param tagNames
	 *            子元素的标记名称
	 * @return
	 */
	public static Element childElement(Element ele, String... tagNames) {
		List<Element> ret = childElements(ele, tagNames);
		return !ret.isEmpty() ? ret.get(0) : null;
	}

	/**
	 * 获取元素的所有子元素，如果指定了tagNames,则获取所有满足标记名称条件的子元素
	 * 
	 * @param ele
	 * @param tagNames
	 *            子元素的标记名称
	 * @return
	 */
	public static List<Element> childElements(Element ele, String... tagNames) {
		if (tagNames == null || tagNames.length == 0)
			return getChildElements(ele);
		return getChildElementsByTagName(ele, tagNames);
	}

	/**
	 * 获取元素第一个子元素的值，如果指定了tagNames,则获取第一个满足标记名称条件子元素的值
	 * 
	 * @param ele
	 * @param tagNames
	 *            子元素的标记名称
	 * @return
	 */
	public static String childElementValue(Element ele, String... tagNames) {
		return value(childElement(ele, tagNames));
	}

	/**
	 * 获取元素的所有子元素
	 * 
	 * @param ele
	 * @return
	 */
	public static List<Element> getChildElements(Element ele) {
		NodeList nl = ele.getChildNodes();
		List<Element> childEles = new ArrayList<Element>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element) {
				childEles.add((Element) node);
			}
		}
		return childEles;
	}

	/**
	 * 获取元素所有满足标记名称条件的子元素
	 * 
	 * @param ele
	 * @param childEleNames
	 * @return
	 */
	public static List<Element> getChildElementsByTagName(Element ele,
			String[] childEleNames) {
		List<String> childEleNameList = Arrays.asList(childEleNames);
		NodeList nl = ele.getChildNodes();
		List<Element> childEles = new ArrayList<Element>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element
					&& nodeNameMatch(node, childEleNameList)) {
				childEles.add((Element) node);
			}
		}
		return childEles;
	}

	/**
	 * (PRIVATE)检查节点的名称是否匹配指定的名称条件
	 * 
	 * @param node
	 * @param desiredNames
	 * @return
	 */
	private static boolean nodeNameMatch(Node node,
			Collection<String> desiredNames) {
		return (desiredNames.contains(node.getNodeName()) || desiredNames
				.contains(node.getLocalName()));
	}

	/**
	 * (PRIVATE)检查一个字符串是否包含内容
	 * 
	 * @param s
	 * @return
	 */
	private static boolean hasText(String s) {
		return s != null && s.length() > 0;
	}

	/**
	 * 在Node上执行指定的xpath表达式
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式，xpath表达式中可以包含名称空间前缀，
	 *            但这些前缀必须在namespaceMap中指定或在node所在文档中声明。
	 * @param namespaceMap
	 *            Map对象，指定xpath中所使用的一组名称空间前缀(Map的Key)及其定义(Map的Value)
	 * @param returnType
	 *            返回结果的类型，为 XPathConstants中声明的类型
	 * @return
	 */
	public static Object evaluate(final Node node, String xpath,
			final Map<String, String> namespaceMap, QName returnType) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath builder = factory.newXPath();
			builder.setNamespaceContext(new NamespaceContext() {
				public String getNamespaceURI(String prefix) {
					if (namespaceMap != null) {
						if (namespaceMap.containsKey(prefix))
							return namespaceMap.get(prefix);
						if (namespaceMap.containsKey(null))
							return namespaceMap.get(null);
					}

					if (node != null) {
						String ret = node.lookupNamespaceURI(prefix);
						if (hasText(ret))
							return ret;
					}

					return hasText(prefix) ? prefix : "*";
				}

				public String getPrefix(String namespaceURI) {
					return null;
				}

				public Iterator<?> getPrefixes(String namespaceURI) {
					return null;
				}
			});
			return builder.compile(xpath).evaluate(node, returnType);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 在Node上执行指定的xpath表达式，执行结果为一个节点集
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式，xpath表达式中可以包含名称空间前缀，
	 *            但这些前缀必须在namespaceMap中指定或在node所在文档中声明。
	 * @param namespaceMap
	 *            Map对象，指定xpath中所使用的一组名称空间前缀(Map的Key)及其定义(Map的Value)
	 * @param returnClass
	 *            节点集中节点的类型
	 * @return
	 */
	public static <T extends Node> List<T> select(Node node, String xpath,
			Map<String, String> namespaceMap, Class<T> returnClass) {
		return list(
				(NodeList) evaluate(node, xpath, namespaceMap,
						XPathConstants.NODESET), returnClass);
	}

	/**
	 * 在Node上执行指定的xpath表达式，执行结果为一个节点集
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式，xpath表达式中可以包含名称空间前缀，
	 *            但这些前缀必须在namespaceMap中指定或在node所在文档中声明。
	 * @param namespaceMap
	 *            Map对象，指定xpath中所使用的一组名称空间前缀(Map的Key)及其定义(Map的Value)
	 * @return
	 */
	public static <T extends Node> List<T> select(Node node, String xpath,
			Map<String, String> namespaceMap) {
		return select(node, xpath, namespaceMap, null);
	}

	/**
	 * 在Node上执行指定的xpath表达式，执行结果为一个节点集
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式。
	 * @return
	 */
	public static <T extends Node> List<T> select(Node node, String xpath) {
		return select(node, xpath, null, null);
	}

	/**
	 * 在Node上执行指定的xpath表达式，执行结果为一个节点集
	 * 
	 * @param node
	 * @param returnClass
	 *            节点集中节点的类型
	 * @return
	 */
	public static <T extends Node> List<T> select(Node node, String xpath,
			Class<T> returnClass) {
		return select(node, xpath, null, returnClass);
	}

	/**
	 * 在Node上执行指定的xpath表达式，执行结果为单个节点
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式，xpath表达式中可以包含名称空间前缀，
	 *            但这些前缀必须在namespaceMap中指定或在node所在文档中声明。
	 * @param namespaceMap
	 *            Map对象，指定xpath中所使用的一组名称空间前缀(Map的Key)及其定义(Map的Value)
	 * @param returnClass
	 *            返回节点的类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> T selectSingle(Node node, String xpath,
			Map<String, String> namespaceMap, Class<T> returnClass) {
		Object ret = evaluate(node, xpath, namespaceMap, XPathConstants.NODE);
		if (returnClass == null
				|| (returnClass != null && returnClass.isInstance(ret)))
			return (T) ret;
		return null;
	}

	/**
	 * 在Node上执行指定的xpath表达式，执行结果为单个节点
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式，xpath表达式中可以包含名称空间前缀，
	 *            但这些前缀必须在namespaceMap中指定或在node所在文档中声明。
	 * @param namespaceMap
	 *            Map对象，指定xpath中所使用的一组名称空间前缀(Map的Key)及其定义(Map的Value)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> T selectSingle(Node node, String xpath,
			Map<String, String> namespaceMap) {
		return (T) selectSingle(node, xpath, namespaceMap, null);
	}

	/**
	 * 在Node上执行指定的xpath表达式，执行结果为单个节点
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> T selectSingle(Node node, String xpath) {
		return (T) selectSingle(node, xpath, null, null);
	}

	/**
	 * 在Node上执行指定的xpath表达式，执行结果为单个节点
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式。
	 * @param returnClass
	 *            返回节点的类型
	 * @return
	 */
	public static <T extends Node> T selectSingle(Node node, String xpath,
			Class<T> returnClass) {
		return selectSingle(node, xpath, null, returnClass);
	}

	/**
	 * 在Node上执行指定的xpath表达式，选择单个节点并返回其节点值
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式，xpath表达式中可以包含名称空间前缀，
	 *            但这些前缀必须在namespaceMap中指定或在node所在文档中声明。
	 * @param namespaceMap
	 *            Map对象，指定xpath中所使用的一组名称空间前缀(Map的Key)及其定义(Map的Value)
	 * @return
	 * 
	 * @see #value(Node)
	 */
	public static String selectValue(Node node, String xpath,
			Map<String, String> namespaceMap) {
		return value(selectSingle(node, xpath, namespaceMap));
	}

	/**
	 * 在Node上执行指定的xpath表达式，选择单个节点并返回其节点值
	 * 
	 * @param node
	 * @param xpath
	 *            指定的xpath表达式
	 * @return
	 * 
	 * @see #value(Node)
	 */
	public static String selectValue(Node node, String xpath) {
		return value(selectSingle(node, xpath));
	}
}
