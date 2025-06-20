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
        GameLib.drawString(10, 58, "Score: " + gameManager.getCurrentScore());
        GameLib.drawString(GameLib.WIDTH - 100, 58, "Vidas: " + gameManager.getPlayerLives());
        GameLib.drawString(10, 78, "Health: " + ((Float)gameManager.getCurrentHealth()).intValue());

        if(isBoss){
            GameLib.setColor(Color.RED);
            GameLib.drawString(GameLib.WIDTH / 2f - 40, 60, "Boss Life: " + gameManager.getCurrentBossHealth());
            GameLib.drawLine(GameLib.WIDTH/3f, 65, GameLib.WIDTH/3f + (GameLib.WIDTH/3f * (gameManager.getCurrentBossHealth()/ gameManager.getBossHealth())), 65);
        }


        if(isGameOver) {
            GameLib.drawString((GameLib.WIDTH / 2f) - 50f, GameLib.HEIGHT / 2f, "FIM DE JOGO :(");
        }
    }

    private void RenderPowerups() {
        var modifiers = gameManager.player.getModifiers();
        for(int i = 0; i < modifiers.size(); i++) {
            var modifier = modifiers.get(i);
            GameLib.setColor(modifier.getModifier().getColor());
            GameLib.drawString(10, 100 + i * 25, "+ " + modifier.getModifier().getName() + " modifier" + ": " + modifier.getTimeLeft() + "s");
        }
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
