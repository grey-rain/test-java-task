package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlReader {

    public Object[][] readXml(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("element");
            int nodesAmount = nodeList.getLength();
            Object[][] testData = new Object[nodesAmount][6];
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //TODO: rework - avoid hardcoded tags and iterator
                    Element nodeElement = (Element) node;
                    testData[i][0] = nodeElement.getElementsByTagName("sender").item(0).getTextContent();
                    testData[i][1] = nodeElement.getElementsByTagName("senderPassword").item(0).getTextContent();
                    testData[i][2] = nodeElement.getElementsByTagName("subj").item(0).getTextContent();
                    testData[i][3] = nodeElement.getElementsByTagName("msgBody").item(0).getTextContent();
                    testData[i][4] = nodeElement.getElementsByTagName("recipient").item(0).getTextContent();
                    testData[i][5] = nodeElement.getElementsByTagName("recipientPassword").item(0).getTextContent();
                }
            }
            return testData;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[][] {};
        }
    }
}
