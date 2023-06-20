package se.settrbrg.pongish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Application;
import se.settrbrg.pongish.screens.MainMenuScreen;

public class Main extends Game {
	public OrthographicCamera camera;
	private FitViewport viewport;
	public ShapeRenderer shapeRenderer;
	public SpriteBatch spriteBatch;

	@Override
	public void create () {
		Settings.load();
		Assets.load();

		if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
			Settings.musicEnabled = false;
		}

		camera = new OrthographicCamera(Assets.screenSize.x, Assets.screenSize.y);
		camera.position.set(Assets.screenSize.x*0.5f, Assets.screenSize.y*0.5f, 0f);
		viewport = new FitViewport(Assets.screenSize.x, Assets.screenSize.y, camera);
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		camera.update();
		super.render();

		if (Gdx.app.getType() != Application.ApplicationType.WebGL) {
			drawInfoText();
		}

		if (Settings.musicEnabled) {
			Assets.music.play();
		} else {
			Assets.music.pause();
		}
	}

	private void drawInfoText() {
		spriteBatch.begin();
		Assets.font.draw(spriteBatch, Assets.infoText, Assets.xOffset, 60f);
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width,height);
	}
}
