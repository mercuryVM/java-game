package game;

import game.ui.DamageIndicator;
import libraries.GameLib;

import java.awt.*;

public class UI {
    private final GameManager gameManager;
    private final DamageIndicator damageIndicator = new DamageIndicator();
    private boolean isGameOver = false;
    private boolean isBoss = false;

    public UI(GameManager manager) {
        gameManager = manager;
    }

    public void ApplyDamage() {
        damageIndicator.ApplyDamage();
    }

    private void RenderTexts() {
        GameLib.setColor(Color.WHITE);
        GameLib.drawString(10, 60, "Score: " + gameManager.getCurrentScore());
        GameLib.drawString(GameLib.WIDTH - 100, 60, "Vidas: " + gameManager.getPlayerLives());
        GameLib.drawString(10, 80, "Health: " + ((Float)gameManager.getCurrentHealth()).intValue());
        RenderSceneText();

        if(isBoss){
            GameLib.setColor(Color.RED);
            GameLib.drawString(GameLib.WIDTH / 2f - 40, 60, "Boss Life: " + gameManager.getCurrentBossHealth());
            GameLib.drawLine(GameLib.WIDTH/3f, 65, GameLib.WIDTH/3f + (GameLib.WIDTH/3f * (gameManager.getCurrentBossHealth()/ gameManager.getBossHealth())), 65);
        }

        if(isGameOver) {
            GameLib.setColor(Color.WHITE);
            GameLib.drawString((GameLib.WIDTH / 2f) - 50f, GameLib.HEIGHT / 2f, "FIM DE JOGO :(");
        }
    }

    private void RenderPowerups() {
        var modifiers = gameManager.player.getModifiers();
        for(int i = 0; i < modifiers.size(); i++) {
            var modifier = modifiers.get(i);
            GameLib.setColor(modifier.getModifier().getColor());
            GameLib.drawString(10, 120 + i * 25, "+ " + modifier.getModifier().getName() + " modifier" + ": " + modifier.getTimeLeft() + "s");
        }
    }

    public void RenderSceneText(){
        if(gameManager.currentGameMode == 0)
            GameLib.drawString(10, 100, "Fase: " + ((int)(gameManager.currentScene.getIndex()+1)));
        else
            GameLib.drawString(10, 100, "Modo: Infinito");
    }

    public void GameOver() {
        isGameOver = true;
    }

    public void setIsBoss(Boolean status){
        isBoss = status;
    }

    public void RenderUI(float deltaTime, long currentTime) {
        RenderTexts();
        RenderPowerups();

        damageIndicator.Update(deltaTime, currentTime);
    }
}
