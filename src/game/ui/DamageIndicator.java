package game.ui;

import libraries.GameLib;
import time.Time;

import java.awt.*;

public class DamageIndicator {
    private long damageStart = 0, damageEnd = 0;

    public void ApplyDamage() {
        damageStart = Time.time;
        damageEnd = Time.time + 250;
    }

    public void Update(float deltaTime, long currentTime) {
        if(Time.time < damageEnd) {
            float progress = (float)(damageEnd - Time.time) / (damageEnd - damageStart);

            GameLib.setColor(new Color(1, 0, 0, (progress) * 0.5f));
            System.out.println(progress);

            GameLib.fillRect(
                    0,
                    0,
                    GameLib.WIDTH * 2,
                    GameLib.HEIGHT * 2
            );
        }
    }
}
