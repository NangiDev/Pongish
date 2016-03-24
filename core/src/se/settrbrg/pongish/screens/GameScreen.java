package se.settrbrg.pongish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import se.settrbrg.pongish.*;
import java.util.Iterator;

public class GameScreen extends ScreenAdapter {
    private static final int GAME_READY = 0;
    private static final int GAME_RUNNING = 1;
    private static final int GAME_PAUSED = 2;
    private static final int GAME_OVER = 3;

    private Main game;
    private World world;
    private int state;
    private int oldState;
    private int menuChoice;

    private Rectangle resumeBox;
    private Rectangle quitBox;

    public GameScreen(Main game) {
        this.game = game;
        world = new World(game);
        Assets.setInfoText("Press Enter to start");
        state = GAME_READY;
        oldState = state;
        menuChoice = 0;

        Assets.midLine.setY(Assets.screenSize.y*0.25f);

        world.addCollisionEntity(Assets.paddleLeft);
        world.addCollisionEntity(Assets.paddleRight);
        //world.addNonCollisionEntity(Assets.invisibleBall);
        world.addNonCollisionEntity(Assets.ball);
        world.addNonCollisionEntity(Assets.leftWall);
        world.addNonCollisionEntity(Assets.rightWall);

        Assets.gameOver.setPosition(Assets.screenSize.x*0.5f - Assets.gameOver.getRegionWidth()*0.5f, Assets.screenSize.y*0.8f - Assets.gameOver.getRegionHeight()*0.5f);
        Assets.resume.setPosition(Assets.screenSize.x*0.5f - Assets.resume.getRegionWidth()*0.5f, Assets.screenSize.y*0.6f - Assets.resume.getRegionHeight()*0.5f);
        Assets.quit.setPosition(Assets.screenSize.x*0.5f - Assets.quit.getRegionWidth()*0.5f, Assets.screenSize.y*0.5f - Assets.quit.getRegionHeight()*0.5f);

        resumeBox = new Rectangle(Assets.resume.getX(), Assets.resume.getY(), Assets.resume.getRegionWidth(), Assets.resume.getRegionHeight());
        quitBox = new Rectangle(Assets.quit.getX(), Assets.quit.getY(), Assets.quit.getRegionWidth(), Assets.quit.getRegionHeight());
    }

    public void update (float delta) {
        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(delta);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }

        if (state != GAME_PAUSED) {
            oldState = state;
        }
    }

    private void togglePause() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
            Assets.playSound(Assets.coinSound);
            if (state != GAME_PAUSED) {
                state = GAME_PAUSED;
            } else {
                state = oldState;
            }
        }
    }

    private void updateReady () {
        togglePause();
        world.effectRenderEntities.clear();
        world.attachInvisibleBall = true;
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            Assets.playSound(Assets.clickSound);
            state = GAME_RUNNING;
            Assets.setInfoText("");
        }
        game.camera.up.set(0f,1f,0f);
        world.resetBall();
    }

    private void updateRunning(float delta) {
        togglePause();
        world.update(delta);
        handleOutOfScreen();
        Assets.setInfoText("" + world.score);
    }

    private void updatePaused () {
        togglePause();
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {

            if (menuChoice == 0) {
                Assets.playSound(Assets.coinSound);
                state = oldState;
            }
            if (menuChoice == 1) {
                Assets.playSound(Assets.jumpSound);
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.W)) {
            Assets.playSound(Assets.clickSound);
            menuChoice--;
            if (menuChoice < 0) {
                menuChoice = 1;
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.S)) {
            Assets.playSound(Assets.clickSound);
            menuChoice++;
            if (menuChoice > 1) {
                menuChoice = 0;
            }
        }
    }

    private void updateGameOver() {
        game.camera.up.set(0f,1f,0f);
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Assets.playSound(Assets.jumpSound);
            game.setScreen(new MainMenuScreen(game));
            Settings.addScore(world.score);
            return;
        }
    }

    private void handleOutOfScreen() {
        if (Assets.ball.getX() < 0) {
            Assets.playSound(Assets.highJumpSound);
            state = GAME_OVER;
        }
        if (Assets.ball.getX() > Assets.screenSize.x) {
            Assets.playSound(Assets.highJumpSound);
            state = GAME_READY;
            world.score += 10;
        }
    }

    public void draw () {
        world.render();

        game.spriteBatch.begin();
        switch (state) {
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
            case GAME_PAUSED:
                presentPaused();
                break;
            case GAME_OVER:
                presentGameOver();
                break;
        }
        game.spriteBatch.end();

        game.shapeRenderer.setAutoShapeType(true);
        game.shapeRenderer.begin();
        switch (state) {
            case GAME_READY:
                presentShapeReady();
                break;
            case GAME_RUNNING:
                presentShapeRunning();
                break;
            case GAME_PAUSED:
                presentShapePaused();
                break;
            case GAME_OVER:
                presentShapeGameOver();
                break;
        }
        game.shapeRenderer.end();
    }

    private void presentReady () {
    }

    private void presentRunning () {
    }

    private void presentPaused () {
        Assets.resume.draw(game.spriteBatch);
        Assets.quit.draw(game.spriteBatch);
    }

    private void presentGameOver () {
        Assets.gameOver.draw(game.spriteBatch);
        Assets.quit.draw(game.spriteBatch);
    }

    private void presentShapeReady () {
    }

    private void presentShapeRunning () {
        /*for (Entity e : world.effectRenderEntities) {
            game.shapeRenderer.set(e.shapeType);
            game.shapeRenderer.setColor(e.color);
            game.shapeRenderer.rect(e.getX(),e.getY(),e.width,e.height);
        }*/
    }

    private void presentShapePaused () {
        game.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(Color.WHITE);
        switch (menuChoice) {
            case 0:
                game.shapeRenderer.rect(resumeBox.getX(), resumeBox.getY(), resumeBox.getWidth(), resumeBox.getHeight());
                break;
            case 1:
                game.shapeRenderer.rect(quitBox.getX(), quitBox.getY(), quitBox.getWidth(), quitBox.getHeight());
                break;

            default:
                break;
        }
    }

    private void presentShapeGameOver () {
        game.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(Color.WHITE);
        game.shapeRenderer.rect(quitBox.getX(), quitBox.getY(), quitBox.getWidth(), quitBox.getHeight());
    }

    @Override
    public void render (float delta) {
        update(delta);
        draw();
    }

    @Override
    public void pause () {
        Settings.save();
    }
}
