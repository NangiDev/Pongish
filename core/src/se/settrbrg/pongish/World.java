package se.settrbrg.pongish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class World {

    private Main game;
    private List<Entity> collisionEntities;
    private List<Entity> nonCollisionEntities;
    public List<Entity> effectRenderEntities;
    public int score;
    private float cameraRumbleAngle;
    private float rumbleDurance;
    public boolean attachInvisibleBall;
    private float timer;

    public World(Main game) {
        this.game = game;
        collisionEntities = new ArrayList<Entity>();
        nonCollisionEntities = new ArrayList<Entity>();
        effectRenderEntities = new ArrayList<Entity>();

        score = 0;

        collisionEntities.add(Assets.roof);
        collisionEntities.add(Assets.floor);

        nonCollisionEntities.add(Assets.midLine);
        //nonCollisionEntities.add(Assets.contactRectangle);

        attachInvisibleBall = true;

        cameraRumbleAngle = 0f;
        rumbleDurance = 0.1f;
        timer = rumbleDurance;

        resetBoard();
    }

    private void resetBoard() {
        Assets.paddleLeft.setPosition(Assets.screenSize.cpy().scl(0.1f, 0.5f));
        Assets.paddleRight.setPosition(Assets.screenSize.cpy().scl(0.9f, 0.5f));
        resetBall();
    }

    public void resetBall() {
        Assets.ball.setPosition(Assets.screenSize.cpy().scl(0.5f));
        Assets.ball.direction.set(-1f, getRandomFloat(1f, -1f)).nor();
        Assets.ball.velocity = 0f;
    }

    private float getRandomFloat(float max, float min) {
        return Assets.random.nextFloat() * (max - min) + min;
    }

    public void update(float delta) {
        updateBall(delta);
        updateInvisibleBall(delta);
        movePaddle(delta);
        moveAiPaddle(delta);
        rumbleCamera(delta);
        updateExplosions();
        checkBallCollision();
    }

    private void updateExplosions() {
        for (Iterator<Entity> it = effectRenderEntities.iterator(); it.hasNext(); ) {
            Entity e = it.next();
            e.width += e.velocity;
            e.height += e.velocity;
            e.x -= e.velocity*0.5f;
            e.y -= e.velocity*0.5f;
            e.color.sub(0.05f,0.05f,0.05f,0f);

            if (e.color.equals(Color.BLACK)) {
                it.remove();
            }

            //System.out.println(e.velocity);
        }
    }

    private void rumbleCamera(float delta) {
        if (timer < rumbleDurance) {
            if (cameraRumbleAngle < 0) {
                cameraRumbleAngle = getRandomFloat(3f, 0f);
            } else {
                cameraRumbleAngle = getRandomFloat(0f, -3f);
            }
        } else {
            cameraRumbleAngle = 0f;
        }
        game.camera.up.set(0f,1f,0f);
        game.camera.rotate(cameraRumbleAngle);

        timer += delta;
    }

    private void moveAiPaddle(float delta) {
        if (Assets.invisibleBall.direction.x > 0f && !attachInvisibleBall && Assets.ball.getX() > Assets.screenSize.x*0.45f) {
            if (Assets.invisibleBall.getY() + Assets.invisibleBall.getHeight()/2 < Assets.paddleRight.getY() + Assets.paddleRight.getHeight()/2) {
                Assets.paddleRight.moveDown(delta);
            } else if (Assets.invisibleBall.getY() + Assets.invisibleBall.getHeight()/2 > Assets.paddleRight.getY() + Assets.paddleRight.getHeight()/2) {
                Assets.paddleRight.moveUp(delta);
            }
        }
    }

    private void updateBall(float delta) {
        Assets.ball.addVelocity(10f);
        Assets.ball.updatePosition(delta);
    }

    private void updateInvisibleBall(float delta) {
        if(attachInvisibleBall) {
            Assets.invisibleBall.direction.set(Assets.ball.direction.x, Assets.ball.direction.y);
            Assets.invisibleBall.setPosition(Assets.ball.getX(), Assets.ball.getY());
        } else {
            Assets.invisibleBall.addVelocity(Assets.invisibleBall.maxVelocity);
            Assets.invisibleBall.updatePosition(delta);
        }
    }

    private void checkBallCollision() {
        for (Entity entity: collisionEntities) {
            if (Assets.ball.isCollision(entity)) {
                Assets.ball.collision(entity);

                if(entity.equals(Assets.paddleLeft)) {
                    Assets.playSound(Assets.hitSound);
                    timer = 0f;
                    score++;
                    attachInvisibleBall = false;
                } else if(entity.equals(Assets.paddleRight)) {
                    timer = 0f;
                    Assets.playSound(Assets.hitSound);
                } else {
                    Assets.playSound(Assets.jumpSound);
                }

                createEffectRectangle();

            }
            if (Assets.invisibleBall.getX() > Assets.screenSize.x || Assets.invisibleBall.getX() < 0) {
                attachInvisibleBall = true;
            }

            if (!attachInvisibleBall && Assets.invisibleBall.isCollision(entity)) {
                if(entity.equals(Assets.paddleRight)) {
                    attachInvisibleBall = true;
                } else {
                    Assets.invisibleBall.collision(entity);
                }
            }
        }
    }

    private void createEffectRectangle() {
        Entity e = new Entity(ShapeRenderer.ShapeType.Filled, new Color().add(getRandomFloat(1f, 0f), getRandomFloat(1f, 0f), getRandomFloat(1f, 0f), 1f))
                        .setPosition(Assets.contactRectangle.getPosition(new Vector2()))
                        .setSize(Assets.contactRectangle.width, Assets.contactRectangle.height);
        e.velocity = Assets.random.nextFloat() * (50f - 15f) + 15f;

        effectRenderEntities.add(e);
    }

    private void movePaddle(float delta) {
        Assets.paddleLeft.velocity = 0f;
        Assets.paddleLeft.direction.set(0f,0f);
        if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))) {
            Assets.paddleLeft.moveUp(delta);
        }
        if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))) {
            Assets.paddleLeft.moveDown(delta);
        }
    }

    public void render() {
        game.shapeRenderer.setAutoShapeType(true);
        game.shapeRenderer.begin();

        for ( Entity e : effectRenderEntities) {
            game.shapeRenderer.set(e.shapeType);
            game.shapeRenderer.setColor(e.color);
            game.shapeRenderer.rect(e.getX(),e.getY(),e.width,e.height);
        }

        for ( Entity e: collisionEntities) {
            game.shapeRenderer.set(e.shapeType);
            game.shapeRenderer.setColor(e.color);
            game.shapeRenderer.rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        }

        for ( Entity e: nonCollisionEntities) {
            game.shapeRenderer.set(e.shapeType);
            game.shapeRenderer.setColor(e.color);
            game.shapeRenderer.rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        }
        game.shapeRenderer.end();
    }

    public void addNonCollisionEntity(Entity entity) {
        nonCollisionEntities.add(entity);
    }
    public void addCollisionEntity(Entity entity) {
        collisionEntities.add(entity);
    }
}
