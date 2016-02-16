package uk.co.redfruit.gdx.experiments;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import uk.co.redfruit.gdx.experiments.box2d.*;

/**
 * Created by paul on 12/10/15.
 */
public class MenuScreen extends ChangeListener implements Screen, InputProcessor {

    protected static final float WORLD_WIDTH = 20f;
    protected static final float WORLD_HEIGHT = 10f;

    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    private Stage stage;
    private Skin skin;
    private Game game;

    private Texture background;
    private Image backgroundImage;

    private Button basicScreen;
    private Button simpleImage;
    private Button complexShapes;
    private Button bottles;
    private Button movement;
    private Button ships;
    private Button runningMan;
    private Button explosion;
    private Button particles;

    public MenuScreen(Game game) {
        super();
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        //camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {
        stage  = new Stage(new FillViewport(800, 480, camera));
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        background = new Texture(Gdx.files.internal("images/background.png"));
        rebuildStage();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
    }


    @Override
    public void changed(ChangeEvent event, Actor actor) {
        if (actor.equals(basicScreen)) {
            game.setScreen(new Box2DExperimentsBasicScreen(game));
        } else if (actor.equals(simpleImage)) {
            game.setScreen(new Box2DExperimentsSimpleImage(game));
        } else if (actor.equals(complexShapes)) {
            game.setScreen(new Box2DExperimentsComplexShapes(game));
        } else if (actor.equals(bottles)) {
            game.setScreen(new Box2DExperimentsBottles(game));
        } else if (actor.equals(ships)) {
            game.setScreen(new Box2DExerimentsShips(game));
        } else if (actor.equals(movement)) {
            game.setScreen(new Box2DExperimentsMovement(game));
        } else if (actor.equals(runningMan)) {
            game.setScreen(new LibGDXExperimentsRunningMan(game));
        } else if (actor.equals(explosion)) {
            game.setScreen(new LibGDXExperimentsExplosion(game));
        } else if (actor.equals(particles)) {
            game.setScreen(new LibGDXExperimentsParticles(game));
        }
    }

    private void rebuildStage() {
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(800, 480);
        stack.add(getBackGroundTable());
        stack.add(getMenuTable());

    }

    private Table getBackGroundTable() {
        Table table = new Table();
        backgroundImage = new Image(background);
        backgroundImage.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        table.add(backgroundImage);
        return table;
    }

    private Table getMenuTable() {
        Table table = new Table();
        table.center();
        basicScreen = new TextButton("Basic Screen", skin);
        table.add(basicScreen);
        table.row();
        simpleImage = new TextButton("Simple Image", skin);
        table.add(simpleImage);
        table.row();
        complexShapes = new TextButton("Complex Shapes", skin);
        table.add(complexShapes);
        table.row();
        bottles = new TextButton("Bottles", skin);
        table.add(bottles);
        table.row();
        ships = new TextButton("Ships", skin);
        table.add(ships);
        table.row();
        movement = new TextButton("Movement", skin);
        table.add(movement);
        table.row();
        runningMan = new TextButton("Running Man", skin);
        table.add(runningMan);
        table.row();
        explosion = new TextButton("Explosion", skin);
        table.add(explosion);
        table.row();
        particles = new TextButton("Particles", skin);
        table.add(particles);
        table.row();


        basicScreen.addListener(this);
        simpleImage.addListener(this);
        complexShapes.addListener(this);
        bottles.addListener(this);
        ships.addListener(this);
        movement.addListener(this);
        runningMan.addListener(this);
        explosion.addListener(this);
        particles.addListener(this);
        return table;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
