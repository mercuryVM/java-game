package scene.config;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameConfig {
    public float playerHealth;
    public int numberOfScenes;
    public ArrayList<String> gameModes = new ArrayList<String>();
    public ArrayList<String> sceneConfigs = new ArrayList<String>();

    public GameConfig(File config) throws IOException, SAXException, ParserConfigurationException  {
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(config);
            document.getDocumentElement().normalize();

            NodeList scenes = document.getElementsByTagName("sceneConfig");
            NodeList modes = document.getElementsByTagName("mode");

            for(int i = 0; i < scenes.getLength(); i++){
                String scene = scenes.item(i).getTextContent();
                sceneConfigs.add(scene);
            }
            for(int j = 0; j < modes.getLength(); j++){
                String mode = modes.item(j).getTextContent();
                gameModes.add(mode);
            }

            var gameElement = document.getDocumentElement();
            this.playerHealth = Float.parseFloat(gameElement.getElementsByTagName("playerHealth").item(0).getTextContent());
            this.numberOfScenes = Integer.parseInt(gameElement.getElementsByTagName("numberOfScenes").item(0).getTextContent());
        }
        catch(Exception e){
            throw e;
        }
    }
}
