package se.settrbrg.pongish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.ScreenAdapter;
import se.settrbrg.pongish.*;

public class HelpScreen extends ScreenAdapter{

    private Main game;
    private World world;

    public HelpScreen(Main game) {
        this.game = game;
        Assets.setInfoText("");

        world = new World(game);

        Assets.midLine.setY(0);

        world.addCollisionEntity(Assets.paddleLeft);
        world.addNonCollisionEntity(Assets.ball);
    }

    public void update (float delta) {
        handleOutOfScreen();

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
            Assets.playSound(Assets.jumpSound);
            game.setScreen(new MainMenuScreen(game));
            return;
        }

        world.update(delta);
    }

    private void handleOutOfScreen() {
        if (Assets.ball.getX() < 0) {
            Assets.playSound(Assets.highJumpSound);
            Assets.ball.setX(Assets.screenSize.x);
            Assets.ball.direction.y = -Assets.ball.direction.y;
        }
        if (Assets.ball.getX() > Assets.screenSize.x) {
            Assets.playSound(Assets.jumpSound);
            Assets.ball.direction.set(-1f, Assets.random.nextFloat()).nor();
        }
    }

    public void draw () {
        world.render();
        game.spriteBatch.begin();
        game.spriteBatch.draw(Assets.helpMenuItems, Assets.screenSize.x*0.75f - Assets.helpMenuItems.getWidth()*0.5f, Assets.screenSize.y*0.5f - Assets.helpMenuItems.getHeight()*0.5f);
        game.spriteBatch.end();
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
