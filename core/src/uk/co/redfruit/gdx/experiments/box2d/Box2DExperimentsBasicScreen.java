package uk.co.redfruit.gdx.experiments.box2d;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Box2DExperimentsBasicScreen extends BaseScreen  {

    public final static float SCALE = 32f;
	public final static float INV_SCALE = 1f/SCALE;
	public final static float VP_WIDTH = 800f * INV_SCALE;
	public final static float VP_HEIGHT = 480f * INV_SCALE;

    private static final String TAG = "Box2DExperimentsBasicScreen";

    private Viewport viewport;



    //Box
    private Body box;
    private BodyDef boxBodyDef;
    private FixtureDef boxFixtureDef;
    private PolygonShape square;

    public Box2DExperimentsBasicScreen(Game game) {
        super(game);

        viewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight);

        // Shape for square
        square = new PolygonShape();
        // 1 meter-sided square
        square.setAsBox(0.5f, 0.5f);

        init(VP_WIDTH / 2, (float) (VP_HEIGHT - 0.25));
    }

    private void init(float x, float y) {
        boxBodyDef = getDynamicBodyDef();

        // Fixture definition for our box
        boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = square;
        boxFixtureDef.density = 0.8f;
        boxFixtureDef.friction = 0.8f;
        boxFixtureDef.restitution = 0.55f;

        //boxBodyDef.position.set(viewport.getWorldWidth() / 2 * INV_SCALE,
        //       (viewport.getWorldHeight() - 0.25f) * INV_SCALE);
        boxBodyDef.position.set(x, y);
        box = world.createBody(boxBodyDef);
        box.createFixture(boxFixtureDef);

        Gdx.app.log(TAG, "Bodies: " + world.getBodyCount());
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1f / 60f, 6, 4);

        debugRenderer.render(world, camera.combined);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
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
        debugRenderer.dispose();
        world.dispose();
        square.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        float x = target.x;
        float y = target.y;
        init(x, y);
        return true;
    }
}
