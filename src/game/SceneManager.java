package game;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import scene.Scene;
import time.Time;

public class SceneManager {
    private final GameManager gameManager;

    public SceneManager(GameManager gameManager){
        this.gameManager = gameManager;
    }

    private void LoadScene(int newSceneIndex, long currentTime){
        try{
            if(newSceneIndex >= 0){         // com cena dos configs (modo de jogo levels)
                String newSceneFile = gameManager.gameConfig.sceneConfigs.get(newSceneIndex);
                Scene scene = new Scene(newSceneFile, currentTime, newSceneIndex);
                gameManager.currentScene = scene;
                gameManager.RenderBackground();
                gameManager.spawnManager.prepareSpawns();
            }
            if(newSceneIndex == -1){        // com cena padrão (modo de jogo infinito)
                Scene scene = new Scene();
                gameManager.currentScene = scene;
                gameManager.RenderBackground();
                gameManager.spawnManager.prepareSpawns();
            }
        }
        catch(IOException | SAXException | ParserConfigurationException e){
            System.out.println(e.getMessage());
            return;
        }
    }

    public void UpdateSceneAndGameMode(){
        if(gameManager.currentScene == null){      // se a fase ainda nao foi carregada
            if(gameManager.currentGameMode == 0){          // e o modo é de fases
                LoadScene(0, Time.time);        // carrega a primeira fase    
                System.out.println("começando primeira fase");
            }   
            if(gameManager.currentGameMode == 1){          // se o modo é infiníto
                LoadScene(-1, Time.time);               // começa direto no infinito
                System.out.println("iniciando modo de jogo infinito");
            }
            return;
        }
        if(gameManager.currentScene.SceneIsDone() && gameManager.currentScene.getIndex()+1 < gameManager.gameConfig.numberOfScenes && gameManager.currentScene.getIndex() != -1){    // se o boss morreu e ainda nao acabaram as cenas, carrega a proxima
            LoadScene(gameManager.currentScene.getIndex()+1, Time.time);
            System.out.println("começando proxima fase");
        }
        if(gameManager.currentScene.SceneIsDone() && gameManager.currentScene.getIndex()+1 >= gameManager.gameConfig.numberOfScenes && gameManager.currentGameMode == 0){       // se o boss morreu e acabaram as cenas, muda pro modo infinito
            gameManager.currentGameMode = 1;
            gameManager.currentScene = null;
            LoadScene(-1, Time.time);
            System.out.println("modo de jogo alterado para infinito");
        }
    }
}
