package uk.co.redfruit.gdx.experiments;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import uk.co.redfruit.gdx.experiments.box2d.BaseScreen;

/**
 * Created by paul on 16/02/16.
 */
public class LibGDXExperimentsParticles extends BaseScreen {

    private String TAG = "LibGDXExperimentsParticles";

    private ParticleEffect pe;

    public LibGDXExperimentsParticles(Game game) {
        super(game);
    }

    @Override
    public void show() {
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("particles/basic.p"), Gdx.files.internal(""));
        pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        Gdx.app.log(TAG, "Name: " + pe.getEmitters().first().getName());
        Gdx.app.log(TAG, "Duration: " + pe.getEmitters().first().duration);
        Gdx.app.log(TAG, "Position: " + pe.getEmitters().first().getX() + ", " + pe.getEmitters().first().getY());
        pe.start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        pe.update(delta);
        batch.begin();
        pe.draw(batch);
        batch.end();
        if (pe.isComplete()) {
            Gdx.app.log(TAG, "particle effect completed");
            pe.reset();
        }
    }
}
