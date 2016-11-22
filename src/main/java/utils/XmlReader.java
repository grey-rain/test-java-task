package utils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlReader {
    private Logger log = LoggerFactory.getLogger(XmlReader.class);

    public Object[][] readXml(String filePath) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(new File(filePath));
            Element rootNode = document.getRootElement();
            List list = rootNode.getChildren("element");
            int dataSize = list.size();
            int argumentsSize = ((Element) list.get(0)).getChildren().size();
            Object[][] elements = new Object[dataSize][argumentsSize];
            log.debug("DataProvider = [{}][{}]", dataSize, argumentsSize);
            for (int i = 0; i < dataSize; i++) {
                Element node = (Element) list.get(i);
                for (int j = 0; j < argumentsSize; j++) {
                    elements[i][j] = node.getChildren().get(j).getText();
                }
            }
            return elements;
        } catch (IOException io) {
            System.out.println(io.getMessage());
            log.error("File not found. {}", io.getLocalizedMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
            log.error("JDom exception. {}", jdomex.getLocalizedMessage());
        }
        log.warn("Empty dataProvider will be set.");
        return new Object[][] {};
    }
}
