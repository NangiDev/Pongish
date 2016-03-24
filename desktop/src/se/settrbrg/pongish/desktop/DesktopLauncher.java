package se.settrbrg.pongish.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import se.settrbrg.pongish.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pongish";
		config.resizable = false;
		config.forceExit = true;
		config.foregroundFPS = 60;
		config.vSyncEnabled = true;
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		new LwjglApplication(new Main(), config);
	}
}
