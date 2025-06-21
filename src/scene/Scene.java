package scene;
import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import scene.config.BackgroundConfig;
import scene.config.EntityConfig;
import scene.config.PowerupConfig;

import java.awt.*;
import java.io.IOException;

public class Scene {
    public ArrayList<EntityConfig> enemiesConfig = new ArrayList<EntityConfig>();
    public ArrayList<BackgroundConfig> backgroundsConfig = new ArrayList<BackgroundConfig>();
    public ArrayList<PowerupConfig> powerUpsConfig = new ArrayList<PowerupConfig>();
    public boolean hasBoss = false;
    private int sceneIndex;
    private boolean bossDefeated = false; 

    public Scene(String file, long currentTime, int currentSceneIndex) throws IOException, SAXException, ParserConfigurationException {
        try{
            this.sceneIndex = currentSceneIndex;
            ConfigsGetter configsGetter = new ConfigsGetter(this);

            // quando a fase tá em .xml não tem boss, de resto funciona normalmente.
            if(file.toLowerCase().endsWith(".txt"))
                configsGetter.getConfigsFromTxt(file, currentTime);
            if(file.toLowerCase().endsWith(".xml"))
                configsGetter.getConfigsFromXml(file, currentTime);

            // ao ordenar agora, a operação de pegar o proximo inimigo (repetida varias vezes) fica bem mais rápida
            powerUpsConfig.sort(Comparator.comparing(p -> p.getSpawnInterval()));
            enemiesConfig.sort(Comparator.comparing(e -> e.getInterval()));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Scene(){     // instancia sem parametro caso jogo esteja no modo infinito
        this.backgroundsConfig.add(new BackgroundConfig(100, 0.045f, new Color(135, 206, 250), 2));
        this.backgroundsConfig.add(new BackgroundConfig(50, 0.070f, new Color(230, 230, 250), 3));
        this.backgroundsConfig.add(new BackgroundConfig(50, 0.100f, new Color(152, 255, 152), 1));
        this.sceneIndex = -1;
        this.enemiesConfig = null;
    }

    public EntityConfig getNextEnemyInterval(int type){
        if(this.enemiesConfig != null){
            int tamanho = this.enemiesConfig.size();
            if(tamanho > 0){        // como ta ordenado por spawn interval, o proximo inimigo do tipo eh o primeiro desse tipo que aparecer na lista
                for(int i = 0; i < tamanho; i++){      // roda as entidades da cena e procura o primeiro intervalo de spawn de um tipo de inimigo
                    EntityConfig e = enemiesConfig.get(i);
                    if (e.getType() == type && !e.isBoss()){
                        return e;
                    }
                }
            }
        }

        return null;
    }

    public EntityConfig getBossInterval(){
        int tamanho = this.enemiesConfig.size();
        if(tamanho > 0){        // como ta ordenado por spawn interval, o proximo inimigo do tipo eh o primeiro desse tipo que aparecer na lista
            for(int i = 0; i < tamanho; i++){      // roda as entidades da cena e procura o primeiro intervalo de spawn de um tipo de inimigo
                EntityConfig e = enemiesConfig.get(i);
                if (e.isBoss()){
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
        return sceneIndex;
    }

    public int getNumOfEnemies(){
        return this.enemiesConfig.size();
    }

    public void removeRecentlySpawnedEnemy(EntityConfig e){
        if(e != null){
            this.enemiesConfig.remove(e);
        }
    }

    public void bossDied(){
        this.bossDefeated = true;
    }

    public boolean SceneIsDone(){
        if(this.enemiesConfig != null){
            if(this.enemiesConfig.isEmpty()){
                if(hasBoss){
                    return bossDefeated;
                } 
                else{
                    return true;
                }
            }
            else{
                return false;
            }
        }
        else{
            return true;
        }
    }
}