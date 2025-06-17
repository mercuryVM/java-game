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
    private ArrayList<EntityConfig> enemiesConfig = new ArrayList<EntityConfig>();
    Color primaryColor;
    Color secundaryColor;
    Color tertiaryColor;
    long initialTime;
    int currentSceneIndex;

    private static Color getColorByName(String colorName) {
        try {
            Field field = Color.class.getField(colorName.toUpperCase());
            return (Color) field.get(null);
        } catch (Exception e) {
            return Color.BLACK; // cor padrão se der erro
        }
    }

    public Scene(String file, long currentTime, int currentSceneIndex) throws IOException, SAXException, ParserConfigurationException {
        File xmlFile = new File(file);          // "PhaseOneConfig.xml"
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList enemyNodes = doc.getElementsByTagName("Enemy");
        String pc = doc.getElementsByTagName("PrimaryBackgroundColor").item(0).getTextContent();
        String sc = doc.getElementsByTagName("SecundaryBackgroundColor").item(0).getTextContent();
        String tc = doc.getElementsByTagName("TertiaryBackgroundColor").item(0).getTextContent();

        if(!pc.isEmpty())
            this.primaryColor = getColorByName(pc);
        else
            this.primaryColor = Color.gray;
        if(!sc.isEmpty())
            this.secundaryColor = getColorByName(sc);
        else
            this.secundaryColor = Color.black;
        if(!tc.isEmpty())
            this.secundaryColor = getColorByName(tc);

        this.initialTime = currentTime;
        this.currentSceneIndex = currentSceneIndex;

        for (int i = 0; i < enemyNodes.getLength(); i++) {
            Node node = enemyNodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;

                String type = e.getElementsByTagName("Type").item(0).getTextContent();
                String interval = (e.getElementsByTagName("SpawnInterval").item(0).getTextContent());
                String posX = e.getElementsByTagName("PositionX").item(0).getTextContent();
                String posY = e.getElementsByTagName("PositionY").item(0).getTextContent();
                    
                if(type.isEmpty())
                    type = "1";
                if(interval.isEmpty())
                    interval = "1000";
                if(posX.isEmpty())
                    posX = "250.0";
                if(posY.isEmpty())
                    posY = "-10.0";
                
                EntityConfig enemy = new EntityConfig
                (
                    Integer.parseInt(type), 
                    (currentTime + Long.parseLong(interval)), 
                    Float.parseFloat(posX), 
                    Float.parseFloat(posY)
                );
                enemiesConfig.add(enemy);
            }
        }
    }

    public Scene(){     // instancia sem parametro caso jogo esteja no modo infinito
        this.primaryColor = Color.GRAY;
        this.secundaryColor = Color.BLACK;
        this.tertiaryColor = null;
        this.currentSceneIndex = -1;
        this.enemiesConfig = null;
    }

    public EntityConfig getNextEnemyInterval(int type){
        int tamanho = this.enemiesConfig.size();
        if(tamanho > 0){
            for(int i = 0; i < tamanho; i++){      // roda as entidades da cena e procura o primeiro intervalo de spawn de um tipo de inimigo
                EntityConfig e = enemiesConfig.get(i);
                if (e.getType() == type){
                    return e;
                }
            }
        }

        return null;
    }

    public int getIndex(){
        return currentSceneIndex;
    }

    public void removeRecentlySpawnedEnemy(EntityConfig e){
        if(e != null){
            this.enemiesConfig.remove(e);
        }
    }

    public boolean SceneIsDone(){
        if(this.enemiesConfig != null)
            return this.enemiesConfig.isEmpty();
        else 
            return true;
    }
}