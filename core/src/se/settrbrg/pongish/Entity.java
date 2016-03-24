package se.settrbrg.pongish;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity extends Rectangle {

    public ShapeType shapeType;
    public Color color;
    public Vector2 direction;
    public float velocity;
    public float maxVelocity;

    public Entity(ShapeType shapeType, Color color) {
        this.shapeType = shapeType;
        this.color = color;
        direction = new Vector2(0f, 0f);
        velocity = 0f;
        maxVelocity = 0f;
    }

    public Entity setSize(float width, float height) {
        this.width = width;
        this.height = height;

        return this;
    }

    public Entity setPosition(Vector2 position) {
        this.x = position.x - width * 0.5f;
        this.y = position.y - height * 0.5f;

        return this;
    }

    public void moveUp(float delta) {
        velocity = maxVelocity;
        direction.set(0f,1f);
        if (getY() > Assets.screenSize.y - getHeight() - 16f) {
            velocity = 0f;
        }
        updatePosition(delta);
    }

    public void moveDown(float delta) {
        velocity = maxVelocity;
        direction.set(0f,-1f);
        if (getY() < 16f) {
            velocity = 0f;
        }
        updatePosition(delta);
    }

    public void updatePosition(float delta) {
        setX(getX() + velocity * direction.x * delta);
        setY(getY() + velocity * direction.y * delta);
    }

    public void addVelocity(float scalar) {
        if (velocity < maxVelocity) {
            velocity += scalar;
        }
    }

    public boolean isCollision(Entity entity) {
        if (Intersector.intersectRectangles(this, entity, Assets.contactRectangle)) {
            return true;
        }
        return false;
    }

    public void collision(Entity entity) {
        Assets.normalVector = getEntityFaceNormal(Assets.normalVector, entity, Assets.contactRectangle);
        this.direction.set(getReflection(this.direction, Assets.normalVector));
        //this.direction.set(this.direction.add(getTransferedForce(entity)));
        this.direction.add(new Vector2(0f, 0.25f).scl(entity.direction.y)).nor();
        this.direction.y = MathUtils.clamp(this.direction.y, -0.9f, 0.9f);
        this.direction.nor();
        this.setPosition(this.getX() - (Assets.contactRectangle.width+1f)*Assets.normalVector.x, this.getY() - (Assets.contactRectangle.height+1f)*Assets.normalVector.y);
    }

    private Vector2 getTransferedForce(Entity entity) {
        return entity.direction.cpy().scl(Assets.ball.velocity).nor();
    }

    //return Vect1 - 2 * normal * (normal.dot(Vect1))
    private Vector2 getReflection(Vector2 direction, Vector2 normal) {
        float normDotDir = normal.cpy().dot(direction);
        return direction.sub(normal.scl(2*normDotDir)).nor();
    }

    private Vector2 getEntityFaceNormal(Vector2 normal, Entity entity, Entity contact) {
        //Left or Right
        if (contact.width < contact.height) {
            if (contact.getX() + contact.getWidth()*0.5f < entity.getX() + entity.getWidth()*0.5f) { //Left
                normal.set(-1f,0f);
            } else { //Right
                normal.set(1f,0f);
            }
        } else { //Up or Down
            if (contact.getY() > entity.getY()) { //Up
                normal.set(0f,-1f);
            } else { //Down
                normal.set(0f,-1f);
            }
        }

        return normal;
    }
}