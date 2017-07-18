package com.sss.indranil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IndranilParser {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		// TODO Auto-generated method stub
		String referenceToObj = "/CustomObject/fields[type = 'Lookup' or type = 'MasterDetail']/referenceTo";
		//String countReferenceToObj = "count(/CustomObject/fields[type = 'Lookup' or type = 'MasterDetail']/referenceTo)";
		Path xmlPath = Paths.get(args[0]);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document xmlDoc = documentBuilder.parse(xmlPath.toFile());
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression xPathOfRefObj = xpath.compile(referenceToObj);		
		//XPathExpression xPathCountOfRefObj = xpath.compile(countReferenceToObj);		
		//Double count = (Double) xPathCountOfRefObj.evaluate(xmlDoc, XPathConstants.NUMBER);
		NodeList nodeList = (NodeList) xPathOfRefObj.evaluate(xmlDoc, XPathConstants.NODESET);
		int count = nodeList.getLength();
		if(count != 0.0d) {
			for(int i = 0 ; i < nodeList.getLength() ; i++ ) {
				Node node = nodeList.item(i);
				System.out.println(Collections.nCopies(20, "-"));
				System.out.println(node.getTextContent());
			}
		}
	}

}
