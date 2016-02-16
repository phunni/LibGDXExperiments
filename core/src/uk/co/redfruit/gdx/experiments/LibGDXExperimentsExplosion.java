package uk.co.redfruit.gdx.experiments;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import uk.co.redfruit.gdx.experiments.box2d.BaseScreen;

/**
 * Created by paul on 20/10/15.
 */
public class LibGDXExperimentsExplosion extends BaseScreen {
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 4;

    Animation walkAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    Sprite currentFrameSprite;

    float stateTime;

    public LibGDXExperimentsExplosion(Game game) {
        super(game);
    }

    @Override
    public void show() {
        walkSheet = new Texture(Gdx.files.internal("images/explosion.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.050f, walkFrames);
        batch = new SpriteBatch();
        stateTime = 0f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        currentFrameSprite = new Sprite(currentFrame);
        currentFrameSprite.setSize(5, 5);
        currentFrameSprite.setPosition(5, 5);
        batch.begin();
        currentFrameSprite.draw(batch);
        batch.end();
    }
}
