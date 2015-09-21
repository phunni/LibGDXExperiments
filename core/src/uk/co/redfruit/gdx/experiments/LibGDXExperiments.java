package uk.co.redfruit.gdx.experiments;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uk.co.redfruit.gdx.experiments.box2d.*;

public class LibGDXExperiments extends Game {
	
	@Override
	public void create () {
		//Screen screen = new Box2DExerimentsShips();
		// Screen screen = new Box2DExperimentsBottles();
        Screen screen = new Box2DExperimentsComplexShapes();
        //Screen screen = new Box2DExperimentsSimpleImage();
		//Screen screen = new Box2DExperimentsBasicScreen();
        setScreen(screen);
	}

}
