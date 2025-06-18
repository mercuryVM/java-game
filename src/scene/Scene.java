package scene;
import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import entities.player.modifiers.DoubleTapModifier;
import entities.player.modifiers.HealthAddModifier;
import entities.player.modifiers.InvincibleModifier;
import entities.player.modifiers.PlayerModifier;
import scene.config.BackgroundConfig;
import scene.config.EntityConfig;
import scene.config.PowerupConfig;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Scene {
    private ArrayList<EntityConfig> enemiesConfig = new ArrayList<EntityConfig>();
    public ArrayList<BackgroundConfig> backgroundsConfig = new ArrayList<BackgroundConfig>();
    public ArrayList<PowerupConfig> powerUpsConfig = new ArrayList<PowerupConfig>();
    long initialTime;
    int currentSceneIndex;

    public Scene(String file, long currentTime, int currentSceneIndex) throws IOException, SAXException, ParserConfigurationException {
        try{
            this.initialTime = currentTime;
            this.currentSceneIndex = currentSceneIndex;
            getBackgroundsConfigFromFile(file, currentTime);
            getEnemiesConfigFromFile(file, currentTime);
            getPowerupsConfigFromFile(file, currentTime);

            // ao ordenar agora, a operação de pegar o proximo inimigo (repetida varias vezes) fica bem mais rápida
            powerUpsConfig.sort(Comparator.comparing(p -> p.getSpawnInterval()));
            enemiesConfig.sort(Comparator.comparing(e -> e.getInterval()));
        }
        catch(Exception e){
            throw e;
        }
    }

    public Scene(){     // instancia sem parametro caso jogo esteja no modo infinito
        this.backgroundsConfig.add(new BackgroundConfig(100, 0.045f, new Color(135, 206, 250), 2));
        this.backgroundsConfig.add(new BackgroundConfig(50, 0.070f, new Color(230, 230, 250), 3));
        this.backgroundsConfig.add(new BackgroundConfig(50, 0.100f, new Color(152, 255, 152), 1));
        this.currentSceneIndex = -1;
        this.enemiesConfig = null;
    }

    private void getEnemiesConfigFromFile(String file, long currentTime) throws IOException, SAXException, ParserConfigurationException {
        try{
            File xmlFile = new File(file);          // "PhaseXConfig.xml"
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList enemyNodes = doc.getElementsByTagName("Enemy");
            for (int i = 0; i < enemyNodes.getLength(); i++) {
                Node node = enemyNodes.item(i);
    
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
    
                    int amount = 10;
                    String type = fetchElementData(e, "Type");
                    String interval = fetchElementData(e, "SpawnInterval");
                    String posX = fetchElementData(e, "PositionX");
                    String posY = fetchElementData(e, "PositionY");
                    Node amt = e.getElementsByTagName("Amount").item(0);
                        
                    if(amt != null && !amt.getTextContent().isEmpty())
                        amount = Integer.parseInt(amt.getTextContent());
    
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
                        Float.parseFloat(posY),
                        amount
                    );
                    enemiesConfig.add(enemy);
                }
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    private void getBackgroundsConfigFromFile(String file, long currentTime) throws IOException, SAXException, ParserConfigurationException {
        try{
            File xmlFile = new File(file);          // "PhaseXConfig.xml"
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList backgroundNodes = doc.getElementsByTagName("Background");
    
            for(int j = 0; j < backgroundNodes.getLength(); j++){
                Node node = backgroundNodes.item(j);
    
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) node;
                    int R,G,B = 0;
                    
                    String Rstring = fetchElementData(e, "R");
                    String Gstring = fetchElementData(e, "G");
                    String Bstring = fetchElementData(e, "B");
                    String numStars = fetchElementData(e, "NumOfStars");
                    String speed = fetchElementData(e, "Speed");
                    String size = fetchElementData(e, "Size");
    
                    if(Rstring.isEmpty() || Gstring.isEmpty() || Bstring.isEmpty()){
                        Rstring = "255"; Gstring = "255"; Bstring = "255";
                    }
                    if(numStars.isEmpty())
                        numStars = "100";
                    if(speed.isEmpty())
                        speed = "0.05";
                    if(size.isEmpty())
                        size = "2";
    
                    R = Integer.parseInt(Rstring);
                    G = Integer.parseInt(Gstring);
                    B = Integer.parseInt(Bstring);
    
                    BackgroundConfig bgconfig = new BackgroundConfig
                    (
                        Integer.parseInt(numStars),
                        Float.parseFloat(speed),
                        new Color(R,G,B),
                        Integer.parseInt(size)
                    );
    
                    backgroundsConfig.add(bgconfig);
                }
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    private void getPowerupsConfigFromFile(String file, long currentTime) throws IOException, SAXException, ParserConfigurationException {
        try{
            File xmlFile = new File(file);          // "PhaseXConfig.xml"
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList powerupNodes = doc.getElementsByTagName("PowerUp");
    
            for(int j = 0; j < powerupNodes.getLength(); j++){
                Node node = powerupNodes.item(j);

                ArrayList<PlayerModifier> modifiersList = new ArrayList<>();
    
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) node;
                    
                    NodeList powerupmodifiers = e.getElementsByTagName("Modifier");
                    
                    for(int i = 0; i < powerupmodifiers.getLength(); i++){          // pra cada powerup, roda seus modifiers
                        if(powerupmodifiers.item(i).getTextContent() == "Invincibility"){
                            modifiersList.add(new InvincibleModifier());
                        }
                        if(powerupmodifiers.item(i).getTextContent() == "Health"){
                            modifiersList.add(new HealthAddModifier(100));
                        }
                        if(powerupmodifiers.item(i).getTextContent() == "Double-tap"){
                            modifiersList.add(new DoubleTapModifier());
                        }
                        
                    }
                    
                    String interval = fetchElementData(e, "SpawnInterval");
                    String posX = fetchElementData(e, "PositionX");
                    String posY = fetchElementData(e, "PositionY");
                    
                    if(interval.isEmpty())
                        interval = "5000";
                    if(posX.isEmpty())
                        posX = "250";
                    if(posY.isEmpty())
                        posY = "-10.0";
    
                    PowerupConfig pConfig = new PowerupConfig 
                    (
                        (currentTime + Long.parseLong(interval)),
                        Float.parseFloat(posX),
                        Float.parseFloat(posY)
                    );
    
                    pConfig.modifiers = modifiersList;
                    powerUpsConfig.add(pConfig);
                }
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    private String fetchElementData(Element e, String data){
        return e.getElementsByTagName(data).item(0).getTextContent();
    }

    public EntityConfig getNextEnemyInterval(int type){
        int tamanho = this.enemiesConfig.size();
        if(tamanho > 0){        // como ta ordenado por spawn interval, o proximo inimigo do tipo eh o primeiro desse tipo que aparecer na lista
            for(int i = 0; i < tamanho; i++){      // roda as entidades da cena e procura o primeiro intervalo de spawn de um tipo de inimigo
                EntityConfig e = enemiesConfig.get(i);
                if (e.getType() == type){
                    return e;
                }
            }
        }

        return null;
    }

    public PowerupConfig getNextPowerupInterval(){
        int tamanho = this.powerUpsConfig.size();
        
        if(tamanho > 0){
            PowerupConfig SmallestIntervalPowerup = this.powerUpsConfig.get(0); // como ta ordenado por spawn e estamos exluindo ao spawnar, o proximo eh sempre o primeiro da lista
            return SmallestIntervalPowerup;
        }
        return null;
    } 

    public void removeRecentlySpawnedPowerup(PowerupConfig p){
        if(p != null){
            this.powerUpsConfig.remove(p);
        }
    }

    public int getIndex(){
        return currentSceneIndex;
    }

    public int getNumOfEnemies(){
        return this.enemiesConfig.size();
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