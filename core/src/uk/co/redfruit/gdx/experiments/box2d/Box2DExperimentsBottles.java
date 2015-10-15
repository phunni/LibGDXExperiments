package uk.co.redfruit.gdx.experiments.box2d;

import aurelienribon.bodyeditor.BodyEditorLoader;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

/**
 * Created by paul on 18/09/15.
 */
public class Box2DExperimentsBottles extends BaseScreen {

    private static final String TAG = "Box2DExperimentsBottles";

    private static final float BOTTLE_WIDTH = 2;

    private Texture texture;
    private Sprite sprite;

    private BodyEditorLoader loader;

    private Array<Bottle> bottles = new Array<Bottle>();

    public Box2DExperimentsBottles(Game game) {
        super(game);
        texture = new Texture(Gdx.files.internal("images/bottle.png"));
        sprite = new Sprite(texture);
        loader = new BodyEditorLoader(Gdx.files.internal("box2d/bottle.json"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (Bottle bottle : bottles) {
            Vector2 bottlePos = bottle.body.getPosition().sub(bottle.origin);
            sprite.setPosition(bottlePos.x, bottlePos.y);
            sprite.setBounds(bottlePos.x, bottlePos.y, BOTTLE_WIDTH, BOTTLE_WIDTH);
            sprite.setOrigin(bottle.origin.x, bottle.origin.y);
            sprite.setRotation(bottle.body.getAngle() * MathUtils.radiansToDegrees);
            sprite.draw(batch);
        }

        batch.end();


        debugRenderer.render(world, camera.combined);

        world.step(1f / 60f, 6, 2);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        float x = target.x;
        float y = target.y;

        createBottle(x, y);

        Gdx.app.log(TAG, "Bodies: " + world.getBodyCount());

        return true;
    }

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
    }

    private void createBottle(float x, float y) {

        BodyDef bodyDef = getDynamicBodyDef();
        bodyDef.position.set(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f;

        Body bottle = world.createBody(bodyDef);

        loader.attachFixture(bottle, "bottle", fixtureDef, BOTTLE_WIDTH);
        bottles.add(new Bottle(sprite, bottle, loader.getOrigin("bottle", BOTTLE_WIDTH).cpy()));

    }

    private class Bottle {

        private Sprite sprite;
        private Body body;
        private Vector2 origin;

        public Bottle(Sprite sprite, Body body, Vector2 origin) {
            this.sprite = sprite;
            this.body = body;
            this.origin = origin;
        }


    }
}
