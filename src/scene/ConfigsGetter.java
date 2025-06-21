package scene;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

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
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigsGetter {
    private Scene Scene = null;

    public ConfigsGetter(Scene s){
        this.Scene = s;
    }

    public void getConfigsFromXml(String file, long currentTime) throws IOException, SAXException, ParserConfigurationException {
        try{
            getEnemiesConfigFromXml(file, currentTime);
            getBackgroundsConfigFromXml(file, currentTime);
            getPowerupsConfigFromXml(file, currentTime);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void getEnemiesConfigFromXml(String file, long currentTime) throws IOException, SAXException, ParserConfigurationException {
        try{
            File xmlFile = new File(file);          // "SceneXConfig.xml"
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
                    this.Scene.enemiesConfig.add(enemy);
                }
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    public void getBackgroundsConfigFromXml(String file, long currentTime) throws IOException, SAXException, ParserConfigurationException {
        try{
            File xmlFile = new File(file);          // "SceneXConfig.xml"
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
    
                    this.Scene.backgroundsConfig.add(bgconfig);
                }
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    public void getPowerupsConfigFromXml(String file, long currentTime) throws IOException, SAXException, ParserConfigurationException {
        try{
            File xmlFile = new File(file);          // "SceneXConfig.xml"
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
                        if(powerupmodifiers.item(i).getTextContent().equals("Invincibility")){
                            modifiersList.add(new InvincibleModifier());
                        }
                        if(powerupmodifiers.item(i).getTextContent().equals("Health")){
                            modifiersList.add(new HealthAddModifier(100));
                        }
                        if(powerupmodifiers.item(i).getTextContent().equals("Double-tap")){
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
                    this.Scene.powerUpsConfig.add(pConfig);
                }
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    public void getConfigsFromTxt(String file, long currentTime) throws FileNotFoundException, InputMismatchException {
        try{
            // pro caso do .txt precisamos assumir algumas coisas que no xml era mais "solto" e teria mais margem para problemas
            // por exemplo, não pode acontecer de eu ter uma linha inimigo, ter tipo, não ter posX e ter posY. 
            // Todos os valores de chave até posY precisam ser preenchidos

            File txtFile = new File(file);          // "SceneXConfig.txt"
            Scanner FileScanner = new Scanner(txtFile);

            while (FileScanner.hasNextLine()) {
                String lineScanner = FileScanner.nextLine().trim();

                if (lineScanner.isEmpty()) {
                    continue; // pula as linhas vazias
                }
                
                Scanner linhaScanner = new Scanner(lineScanner);
                String chave = linhaScanner.next();

                if(chave.equals("ENEMY")){
                    // amount pode estar vazio 
                    // com excessão do amount que é a última posição e pode ou não estar preenchida
                    int type = linhaScanner.nextInt();
                    long interval = currentTime + linhaScanner.nextLong();
                    String posX = linhaScanner.next();
                    String posY = linhaScanner.next();
                    int amount = 10;
                    if(linhaScanner.hasNext())
                        amount = linhaScanner.nextInt();

                    EntityConfig enemy = new EntityConfig(type, interval, Float.parseFloat(posX), Float.parseFloat(posY), amount);
                    this.Scene.enemiesConfig.add(enemy);
                }
                if(chave.equals("BOSS")){
                    this.Scene.hasBoss = true;

                    int type = linhaScanner.nextInt();
                    int health = linhaScanner.nextInt();
                    long interval = currentTime + linhaScanner.nextLong();
                    String posX = linhaScanner.next();
                    String posY = linhaScanner.next();

                    EntityConfig boss = new EntityConfig(type, health, interval, Float.parseFloat(posX), Float.parseFloat(posY));
                    this.Scene.enemiesConfig.add(boss);
                }
                if(chave.equals("POWERUP")){    
                    ArrayList<PlayerModifier> modifiersList = new ArrayList<>();
                    
                    long interval = currentTime + linhaScanner.nextLong();
                    String posX = linhaScanner.next();
                    String posY = linhaScanner.next();

                    // posso nao ter nenhum modifier e posso ter varios, conforme for tendo vc vai adicionando
                    while(linhaScanner.hasNext()){
                        String modifier = linhaScanner.next();
                        if(modifier.equals("Invincibility"))
                            modifiersList.add(new InvincibleModifier());
                        if(modifier.equals("Health"))
                            modifiersList.add(new HealthAddModifier(100));
                        if(modifier.equals("Double-tap"))
                            modifiersList.add(new DoubleTapModifier());
                    }
    
                    PowerupConfig pConfig = new PowerupConfig(interval, Float.parseFloat(posX), Float.parseFloat(posY));     
                    pConfig.modifiers = modifiersList;
                    this.Scene.powerUpsConfig.add(pConfig);
                }
                if(chave.equals("BACKGROUND")){                    
                    int R = linhaScanner.nextInt();
                    int G = linhaScanner.nextInt();
                    int B = linhaScanner.nextInt();
                    int numStars = linhaScanner.nextInt();
                    String speed = linhaScanner.next();
                    int size = linhaScanner.nextInt();
    
                    BackgroundConfig bgConfig = new BackgroundConfig(numStars, Float.parseFloat(speed), new Color(R,G,B), size);
                    this.Scene.backgroundsConfig.add(bgConfig);
                }

                linhaScanner.close();
            }

            FileScanner.close();
        }
        catch(Exception e){
            throw e;
        }
    }

    private String fetchElementData(Element e, String data){
        return e.getElementsByTagName(data).item(0).getTextContent();
    }
}
