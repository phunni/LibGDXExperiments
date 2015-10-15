package uk.co.redfruit.gdx.experiments.box2d;

import aurelienribon.bodyeditor.BodyEditorLoader;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by paul on 22/09/15.
 */
public class Box2DExperimentsMovement extends BaseScreen {

    private static final String TAG = "Box2DExperimentsMovement";

    private static final float SHIP_WIDTH = 2;
    private static float SHIP_SPEED = 4;

    private Sprite sprite;
    private Texture texture;
    private Body ship;

    private boolean movingRight;
    private boolean movingLeft;

    private BodyEditorLoader loader;


    public Box2DExperimentsMovement(Game game) {
        super(game);
        texture = new Texture(Gdx.files.internal("images/ship.png"));
        sprite = new Sprite(texture);
        loader = new BodyEditorLoader(Gdx.files.internal("box2d/ship.json"));
        createSideWalls();
        createShip(8, 1f);
    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (movingLeft) {
            moveLeft();
        } else if (movingRight) {
            moveRight();
        }

        if (!movingLeft && !movingRight) {
            ship.setLinearVelocity(0, ship.getLinearVelocity().y);
        }

        Vector2 origin = loader.getOrigin("ship", SHIP_WIDTH).cpy();
        Vector2 shipPos = ship.getPosition().sub(origin);
        sprite.setPosition(shipPos.x, shipPos.y);
        sprite.setBounds(shipPos.x, shipPos.y, SHIP_WIDTH, SHIP_WIDTH * sprite.getHeight() / sprite.getWidth());
        sprite.setOrigin(origin.x, origin.y);
        sprite.setRotation(ship.getAngle() * MathUtils.radiansToDegrees);

        batch.begin();
        sprite.draw(batch);
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
    public boolean keyDown(int keycode) {
        super.keyDown(keycode);
        if (keycode == Input.Keys.LEFT) {
            movingLeft = true;
        } else if (keycode == Input.Keys.RIGHT) {
            movingRight = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            movingLeft = false;
        } else if (keycode == Input.Keys.RIGHT) {
            movingRight = false;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 target = getTarget(screenX, screenY);
        if (target.x < WORLD_WIDTH / 2) {
            movingLeft = true;
        } else if (target.x > WORLD_WIDTH / 2) {
            movingRight = true;
        }
        return true;
    }



    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 target = getTarget(screenX, screenY);
        if (target.x < WORLD_WIDTH / 2) {
            movingLeft = false;
        } else if (target.x > WORLD_WIDTH / 2) {
            movingRight = false;
        }
        return true;
    }

    private Vector2 getTarget(int screenX, int screenY) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(target.x, target.y);
    }

    private void createShip(float x, float y) {
        BodyDef bodyDef = getDynamicBodyDef();
        bodyDef.position.set(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.2f;

        ship = world.createBody(bodyDef);

        loader.attachFixture(ship, "ship", fixtureDef, SHIP_WIDTH);
    }

    private void moveRight() {
        Vector2 velocity = ship.getLinearVelocity();
        ship.setLinearVelocity(SHIP_SPEED, velocity.y);
    }

    private void moveLeft() {
        Vector2 velocity = ship.getLinearVelocity();
        ship.setLinearVelocity(-SHIP_SPEED, velocity.y);
    }

    private void createSideWalls() {
        float halfHeight = WORLD_HEIGHT;
        float halfWidth = 0.05f;

        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.StaticBody;

        wallsDef.position.set(halfWidth, halfHeight);

        Body leftWall = world.createBody(wallsDef);

        wallsDef.position.set(WORLD_WIDTH, halfHeight);

        Body rightWall = world.createBody(wallsDef);
        PolygonShape wallBox = new PolygonShape();
        wallBox.setAsBox(halfWidth, halfHeight);

        leftWall.createFixture(wallBox, 0.0f);
        rightWall.createFixture(wallBox, 0.0f);

        wallBox.dispose();
    }
}
