package uk.co.redfruit.gdx.experiments;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uk.co.redfruit.gdx.experiments.box2d.BaseScreen;

/**
 * Created by paul on 16/02/16.
 */
public class LibGDXExperimentsParticles implements Screen {

    private String TAG = "LibGDXExperimentsParticles";

    private Game game;
    private SpriteBatch batch;
    private ParticleEffect pe;
    private OrthographicCamera camera;

    public LibGDXExperimentsParticles(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("particles/basic.p"), Gdx.files.internal(""));
        //pe.scaleEffect(20f);
        pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        Gdx.app.log(TAG, "Name: " + pe.getEmitters().first().getName());
        Gdx.app.log(TAG, "Duration: " + pe.getEmitters().first().duration);
        Gdx.app.log(TAG, "Position: " + pe.getEmitters().first().getX() + ", " + pe.getEmitters().first().getY());
        pe.start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        pe.update(delta);
        batch.begin();
        pe.draw(batch);
        batch.end();
        if (pe.isComplete()) {
            Gdx.app.log(TAG, "particle effect completed");
            pe.reset();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.update();
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
}
