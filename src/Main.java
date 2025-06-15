import game.GameManager;
import libraries.GameLib;
import org.xml.sax.SAXException;
import scene.config.GameConfig;
import time.Time;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/***********************************************************************/
/*                                                                     */
/* Para jogar:                                                         */
/*                                                                     */
/*    - cima, baixo, esquerda, direita: movimentação do player.        */
/*    - control: disparo de projéteis.                                 */
/*    - ESC: para sair do jogo.                                        */
/*                                                                     */
/***********************************************************************/

public class Main {
	public static GameManager gameManager;

	public static void main(String[] args) {
		try {
			//Carrega configuração do jogo antes de iniciar

			System.out.println("Carregando gameConfig.xml");

			var gameConfig = GameConfig.parseFromXMLFile(
					new File("gameConfig.xml")
			);

			//Cria GameManager com base na configuração carregada
			gameManager = new GameManager(
					gameConfig
			);

			Time.timeStartup = System.currentTimeMillis();

			//Inicializa o GameLoop
			GameLoop();
		}catch(IOException ex)  {
			System.out.println("gameConfig.xml não encontrado. Verifique se ele está na pasta...");
		}catch(SAXException | ParserConfigurationException ex) {
			System.out.println("gameConfig.xml inválido. Tente gerar outro...");
		}
	}

	static void GameLoop() {
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */

		long delta;
		long currentTime = System.currentTimeMillis();

		GameLib.initGraphics();

		while(running) {

			/* Usada para atualizar o estado dos elementos do jogo    */
			/* (player, projéteis e inimigos) "delta" indica quantos  */
			/* ms se passaram desde a última atualização.             */

			delta = System.currentTimeMillis() - currentTime;

			/* Já a variável "currentTime" nos dá o timestamp atual.  */

			currentTime = System.currentTimeMillis();

			gameManager.Update(delta, currentTime);
			gameManager.Render(delta, currentTime);
		}
	}
}
