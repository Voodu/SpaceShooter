package space.shooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import space.shooter.MainGame;

public class DesktopLauncher {

	public static final Integer WIDTH=1280, HEIGHT=960;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Cosmo attack";
		cfg.width = WIDTH;
		cfg.height = HEIGHT;
		cfg.foregroundFPS = 60;
		new LwjglApplication(new MainGame(), cfg);
	}
}
