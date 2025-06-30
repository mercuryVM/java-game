package game;

import entities.enemies.EnemyA;
import entities.enemies.EnemyB;
import entities.enemies.boss.BossA;
import entities.enemies.boss.BossB;
import entities.powerups.HealthPowerup;
import entities.powerups.Powerup;
import libraries.GameLib;
import math.Vector2;
import scene.config.PowerupConfig;

public class Spawner{
    private final GameManager gameManager;

    public Spawner(GameManager gm){
        this.gameManager = gm;
    }

    public void SpawnEnemyA() {
        var enemy = new EnemyA(gameManager);
        enemy.setActive();
        enemy.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0f);
        enemy.velocity = new Vector2(0.20f + (float) Math.random() * 0.15f, 0.20f + (float) Math.random() * 0.15f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;

        gameManager.enemies.add(enemy);
    }

    public void SpawnEnemyA(float posX, float posY) {       // overload pra modo fase com posição inicial pré-demilimitada
        var enemy = new EnemyA(gameManager);
        enemy.setActive();
        enemy.position = new Vector2(posX, posY);
        enemy.velocity = new Vector2(0.20f + (float) Math.random() * 0.15f, 0.20f + (float) Math.random() * 0.15f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;

        gameManager.enemies.add(enemy);
    }

    public void SpawnEnemyB() {
        var enemy = new EnemyB(gameManager);
        enemy.position = new Vector2((float) (GameLib.WIDTH * 0.20), -10.0f);
        enemy.velocity = new Vector2(0.42f, 0.42f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;
        enemy.setActive();

        gameManager.enemies.add(enemy);
    }

    public void SpawnEnemyB(float posX, float posY) {       // overload pra modo fase com posição inicial pré-demilimitada
        var enemy = new EnemyB(gameManager);
        enemy.position = new Vector2(posX, posY);
        enemy.velocity = new Vector2(0.42f, 0.42f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;
        enemy.setActive();

        gameManager.enemies.add(enemy);
    }

    public void SpawnBossA() {
        var boss = new BossA(gameManager, 2500);
        boss.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), 50.0f);
        boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();
        gameManager.enemies.add(boss);

        gameManager.ui.setIsBoss(true);
    }

    public void SpawnBossA(int health, float posX, float posY) {    // overload pra modo fase com posição inicial e hp predefinidos
        var boss = new BossA(gameManager, health);
        boss.position = new Vector2(posX, posY);
        boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();
        gameManager.enemies.add(boss);

        gameManager.ui.setIsBoss(true);
    }

    public void SpawnBossB() {
        var boss = new BossB(gameManager, 2300);
        boss.position = new Vector2( 60.0f, 60.0f);
        boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();
        gameManager.enemies.add(boss);

        gameManager.ui.setIsBoss(true);
    }

    public void SpawnBossB(int health, float posX, float posY) {
        var boss = new BossB(gameManager, health);
        boss.position = new Vector2(posX, posY);
        boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();
        gameManager.enemies.add(boss);

        gameManager.ui.setIsBoss(true);
    }

    public void SpawnPowerup() {
        var powerup = new HealthPowerup(gameManager);
        powerup.setActive();
        powerup.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0f);
        powerup.velocity = new Vector2(0.05f, 0.05f);
        powerup.angle = (3f * (float) Math.PI) / 2f;
        powerup.rotationVelocity = 0.0f;

        gameManager.powerups.add(powerup);
    }

    public void SpawnPowerup(PowerupConfig pwupConfig) {      // overload pra modo de fase com powerups predefinidos 
        var powerup = new Powerup(gameManager);
        powerup.modifierList = pwupConfig.modifiers;
        powerup.setActive();
        powerup.position = new Vector2(pwupConfig.getPositionX(), pwupConfig.getPositionY());
        powerup.velocity = new Vector2(0.05f, 0.05f);
        powerup.angle = (3f * (float) Math.PI) / 2f;
        powerup.rotationVelocity = 0.0f;

        gameManager.powerups.add(powerup);
    }
}
