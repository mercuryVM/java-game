package scene.config;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import scene.Scene;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameConfig {
    public float playerHealth = 100.0f;

    public List<Scene> sceneList = new ArrayList<>();

    public GameConfig(float playerHealth) {
        this.playerHealth = playerHealth;
    }

    public static GameConfig parseFromXMLFile(File config) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(config);

        var gameElement = document.getDocumentElement();

        String healthTag = gameElement.getElementsByTagName("playerHealth").item(0).getTextContent();

        float playerHealth = Float.parseFloat(healthTag);

        var gameConfig = new GameConfig(playerHealth);

        return gameConfig;
    }
}
