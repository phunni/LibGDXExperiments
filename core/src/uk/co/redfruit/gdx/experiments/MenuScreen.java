package uk.co.redfruit.gdx.experiments;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.co.redfruit.gdx.experiments.box2d.*;

/**
 * Created by paul on 12/10/15.
 */
public class MenuScreen extends ChangeListener implements Screen {

    protected static final float WORLD_WIDTH = 20f;
    protected static final float WORLD_HEIGHT = 10f;

    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    private Stage stage;
    private Skin skin;
    private Game game;

    private Button basicScreen;
    private Button simpleImage;
    private Button complexShapes;
    private Button bottles;
    private Button movement;
    private Button ships;
    private Button runningMan;

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
        stage  = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
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
        stage.getViewport().update(width, height, true);
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

    }


    @Override
    public void changed(ChangeEvent event, Actor actor) {
        if (actor.equals(basicScreen)) {
            game.setScreen(new Box2DExperimentsBasicScreen());
        } else if (actor.equals(simpleImage)) {
            game.setScreen(new Box2DExperimentsSimpleImage());
        } else if (actor.equals(complexShapes)) {
            game.setScreen(new Box2DExperimentsComplexShapes());
        } else if (actor.equals(bottles)) {
            game.setScreen(new Box2DExperimentsBottles());
        } else if (actor.equals(ships)) {
            game.setScreen(new Box2DExerimentsShips());
        } else if (actor.equals(movement)) {
            game.setScreen(new Box2DExperimentsMovement());
        }
    }

    private void rebuildStage() {
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        stage.clear();
        Stack stack = new Stack();
        stack.add(getMenuTable());
        stage.addActor(stack);
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

        basicScreen.addListener(this);
        simpleImage.addListener(this);
        complexShapes.addListener(this);
        bottles.addListener(this);
        ships.addListener(this);
        movement.addListener(this);
        return table;
    }
}
