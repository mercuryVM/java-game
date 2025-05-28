package input;

import entities.player.Player;
import libraries.GameLib;

public class Input {
    private final Player player;

    public Input(Player player) {
        this.player = player;
    }

    public void Process(float deltaTime, long currentTime) {
        if(!this.player.isActive()) return;

        if(GameLib.iskeyPressed(GameLib.KEY_UP)) player.position.y -= deltaTime * player.velocity.y;
        if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player.position.y += deltaTime * player.velocity.y;
        if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player.position.x -= deltaTime * player.velocity.x;
        if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player.position.x += deltaTime * player.velocity.x;

        if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
            player.Shoot(currentTime);
        }

        if(player.position.x < 0.0) player.position.x = 0.0f;
        if(player.position.x >= GameLib.WIDTH) player.position.x = GameLib.WIDTH - 1;
        if(player.position.y < 25.0) player.position.y = 25.0f;
        if(player.position.y >= GameLib.HEIGHT) player.position.y = GameLib.HEIGHT - 1;
    }
}
