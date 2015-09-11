package uk.co.redfruit.gdx.experiments.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uk.co.redfruit.gdx.experiments.LibGDXExperiments;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "LibGDX Experiments";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new LibGDXExperiments(), config);
	}
}
