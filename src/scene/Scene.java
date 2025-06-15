package scene;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import scene.config.EntityConfig;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class Scene {
    ArrayList<EntityConfig> enemiesConfig = new ArrayList<EntityConfig>();
    Color primaryColor;
    Color secundaryColor;
    Color tertiaryColor;

    private static Color getColorByName(String colorName) {
        try {
            Field field = Color.class.getField(colorName.toUpperCase());
            return (Color) field.get(null);
        } catch (Exception e) {
            return Color.BLACK; // cor padrão se der erro
        }
    }

    public Scene(String file) throws IOException, SAXException, ParserConfigurationException {
        File xmlFile = new File(file);          // "PhaseOneConfig.xml"
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList enemyNodes = doc.getElementsByTagName("Enemy");
        String pc = doc.getElementsByTagName("PrimaryBackgroundColor").item(0).getTextContent();
        String sc = doc.getElementsByTagName("SecundaryBackgroundColor").item(0).getTextContent();
        String tc = doc.getElementsByTagName("TertiaryBackgroundColor").item(0).getTextContent();

        this.primaryColor = getColorByName(pc);
        this.secundaryColor = getColorByName(sc);
        this.tertiaryColor = getColorByName(tc);

        for (int i = 0; i < enemyNodes.getLength(); i++) {
            Node node = enemyNodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;

                int type = Integer.parseInt(e.getElementsByTagName("Type").item(0).getTextContent());
                int interval = Integer.parseInt(e.getElementsByTagName("Type").item(0).getTextContent());
                float posX = Float.parseFloat(e.getElementsByTagName("Type").item(0).getTextContent());
                float posY = Float.parseFloat(e.getElementsByTagName("Type").item(0).getTextContent());

                EntityConfig enemy = new EntityConfig(type, interval, posX, posY);
                enemiesConfig.add(enemy);
            }
        }
    }
}