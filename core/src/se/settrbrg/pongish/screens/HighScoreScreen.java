package se.settrbrg.pongish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import se.settrbrg.pongish.Assets;
import se.settrbrg.pongish.Main;
import se.settrbrg.pongish.Settings;

public class HighScoreScreen extends ScreenAdapter {

    private Main game;
    private String[] highScores;
    private float xOffset = 0;
    private GlyphLayout glyphLayout = new GlyphLayout();

    public HighScoreScreen (Main game) {
        this.game = game;
        Assets.highScore.setPosition(Assets.screenSize.x*0.5f - Assets.highScore.getRegionWidth()*0.5f, Assets.screenSize.y*0.8f - Assets.highScore.getRegionHeight()*0.5f);

        Assets.setInfoText("Escape to go back");

        highScores = new String[5];
        for (int i = 0; i < 5; i++) {
            highScores[i] = i + 1 + ". " + Settings.highScores[i];
            glyphLayout.setText(Assets.font, highScores[i]);
            xOffset = Math.max(glyphLayout.width, xOffset);
        }
        xOffset = Assets.screenSize.x*0.5f - xOffset / 2;
    }

    public void update () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            Assets.playSound(Assets.jumpSound);
            game.setScreen(new MainMenuScreen(game));
            return;
        }
    }

    public void draw () {
        game.spriteBatch.begin();
        Assets.highScore.draw(game.spriteBatch);
        float y = Assets.screenSize.y*0.6f;
        for (int i = 0; i < 5; i++) {
            Assets.font.draw(game.spriteBatch, highScores[i], xOffset, y);
            y -= Assets.font.getLineHeight();
        }
        game.spriteBatch.end();
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
