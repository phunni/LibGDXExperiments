package uk.co.redfruit.gdx.experiments.box2d;

import aurelienribon.bodyeditor.BodyEditorLoader;
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
 * Created by paul on 21/09/15.
 */
public class Box2DExerimentsShips extends BaseScreen {

    private static final String TAG = "Box2DExerimentsShips";

    private static final float SHIP_WIDTH = 1;

    private Texture texture;
    private Sprite sprite;

    private BodyEditorLoader loader;

    private Array<Ship> ships = new Array<Ship>();

    public Box2DExerimentsShips() {
        super();
        texture = new Texture(Gdx.files.internal("images/ship.png"));
        sprite = new Sprite(texture);
        loader = new BodyEditorLoader(Gdx.files.internal("box2d/ship.json"));
    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (Ship ship : ships) {
            Vector2 shipPos = ship.body.getPosition().sub(ship.origin);
            sprite.setPosition(shipPos.x, shipPos.y);
            sprite.setBounds(shipPos.x, shipPos.y, SHIP_WIDTH, SHIP_WIDTH * sprite.getHeight()/sprite.getWidth());
            sprite.setOrigin(ship.origin.x, ship.origin.y);
            sprite.setRotation(ship.body.getAngle() * MathUtils.radiansToDegrees);
            sprite.draw(batch);
        }

        batch.end();

        debugRenderer.render(world, camera.combined);

        world.step(1f / 60f, 6, 2);

    }

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        float x = target.x;
        float y = target.y;

        createShip(x, y);

        Gdx.app.log(TAG, "Bodies: " + world.getBodyCount());

        return true;
    }

    private void createShip(float x, float y) {
        BodyDef bodyDef = getDynamicBodyDef();
        bodyDef.position.set(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.2f;

        Body ship = world.createBody(bodyDef);

        loader.attachFixture(ship, "ship", fixtureDef, SHIP_WIDTH);
        ships.add(new Ship(sprite, ship, loader.getOrigin("ship", SHIP_WIDTH).cpy()));
    }

    private class Ship {

        private Sprite sprite;
        private Body body;
        private Vector2 origin;

        public Ship(Sprite sprite, Body body, Vector2 origin) {
            this.sprite = sprite;
            this.body = body;
            this.origin = origin;
        }

    }
}
