package se.settrbrg.pongish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import se.settrbrg.pongish.Assets;
import se.settrbrg.pongish.Main;
import se.settrbrg.pongish.Settings;

import java.util.Set;

public class MainMenuScreen extends ScreenAdapter {

    private Main game;
    private Rectangle playBox;
    private Rectangle highScoreBox;
    private Rectangle helpBox;
    private Rectangle toggleBox;
    private int menuChoice = 0;

    public MainMenuScreen (Main game) {
        this.game = game;

        Assets.setInfoText("Escape to Exit");

        Assets.logo.setScale(2f);
        Assets.logo.setPosition(Assets.screenSize.x*0.5f - Assets.logo.getRegionWidth()*0.5f, Assets.screenSize.y*0.8f - Assets.logo.getRegionHeight()*0.5f);
        Assets.play.setPosition(Assets.screenSize.x*0.5f - Assets.play.getRegionWidth()*0.5f, Assets.screenSize.y*0.5f - Assets.play.getRegionHeight()*0.5f);
        Assets.highScore.setPosition(Assets.screenSize.x*0.5f - Assets.highScore.getRegionWidth()*0.5f, Assets.screenSize.y*0.4f - Assets.highScore.getRegionHeight()*0.5f);
        Assets.help.setPosition(Assets.screenSize.x*0.5f - Assets.help.getRegionWidth()*0.5f, Assets.screenSize.y*0.3f - Assets.help.getRegionHeight()*0.5f);

        playBox = new Rectangle(Assets.play.getX(), Assets.play.getY(), Assets.play.getRegionWidth(), Assets.play.getRegionHeight());
        highScoreBox = new Rectangle(Assets.highScore.getX(), Assets.highScore.getY(), Assets.highScore.getRegionWidth(), Assets.highScore.getRegionHeight());
        helpBox = new Rectangle(Assets.help.getX(), Assets.help.getY(), Assets.help.getRegionWidth(), Assets.help.getRegionHeight());
        toggleBox = new Rectangle(Assets.screenSize.x*0.25f, Assets.screenSize.y*0.15f, 210f, 40f);
        game.camera.up.set(0f,1f,0f);
    }

    public void update () {
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {

            Assets.playSound(Assets.jumpSound);

            if (menuChoice == 0) {
                game.setScreen(new GameScreen(game));
                return;
            }
            if (menuChoice == 1) {
                game.setScreen(new HighScoreScreen(game));
                return;
            }
            if (menuChoice == 2) {
                game.setScreen(new HelpScreen(game));
                return;
            }
            if (menuChoice == 3) {
                if (Settings.soundEnabled && Settings.musicEnabled) {
                    Settings.musicEnabled = false;
                } else if (Settings.soundEnabled && !Settings.musicEnabled) {
                    Settings.soundEnabled = false;
                } else {
                    Settings.soundEnabled = true;
                    Settings.musicEnabled = true;
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.W)) {
            Assets.playSound(Assets.clickSound);
            menuChoice--;
            if (menuChoice < 0) {
                menuChoice = 3;
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.S)) {
            Assets.playSound(Assets.clickSound);
            menuChoice++;
            if (menuChoice > 3) {
                menuChoice = 0;
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
            Assets.playSound(Assets.clickSound);
            Gdx.app.exit();
        }
    }

    public void draw () {
        game.spriteBatch.begin();
        Assets.logo.draw(game.spriteBatch);
        Assets.play.draw(game.spriteBatch);
        Assets.highScore.draw(game.spriteBatch);
        Assets.help.draw(game.spriteBatch);
        Assets.font.draw(game.spriteBatch, "Music/Sound: " + ((Settings.musicEnabled)?"On":"Off") + "/" + ((Settings.soundEnabled)?"On":"Off") , toggleBox.getX()+10f, Assets.screenSize.y*0.2f);

        game.spriteBatch.end();

        game.shapeRenderer.begin(ShapeType.Line);
        game.shapeRenderer.setColor(Color.WHITE);

        switch (menuChoice) {
            case 0:
                game.shapeRenderer.rect(playBox.getX(), playBox.getY(), playBox.getWidth(), playBox.getHeight());
                break;
            case 1:
                game.shapeRenderer.rect(highScoreBox.getX(), highScoreBox.getY(), highScoreBox.getWidth(), highScoreBox.getHeight());
                break;
            case 2:
                game.shapeRenderer.rect(helpBox.getX(), helpBox.getY(), helpBox.getWidth(), helpBox.getHeight());
                break;
            case 3:
                game.shapeRenderer.rect(toggleBox.getX(), toggleBox.getY(), toggleBox.getWidth(), toggleBox.getHeight());
                break;

            default:
                break;
        }
        game.shapeRenderer.end();
    }

    @Override
    public void render (float delta) {
        update();
        draw();
    }

    @Override
    public void pause () {
        Settings.save();
    }
}
