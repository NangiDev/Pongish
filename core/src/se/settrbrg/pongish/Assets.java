package se.settrbrg.pongish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Assets {

    public static Vector2 screenSize;
    public static Random random;
    public static Texture menuItems;
    public static Texture helpMenuItems;
    public static Sprite logo;
    public static Sprite gameOver;
    public static Sprite highScore;
    public static Sprite resume;
    public static Sprite quit;
    public static Sprite play;
    public static Sprite help;
    public static BitmapFont font;
    public static String infoText;
    public static float xOffset;

    public static Entity ball;
    public static Entity invisibleBall;
    public static Entity leftWall;
    public static Entity rightWall;
    public static Entity roof;
    public static Entity floor;
    public static Entity paddleLeft;
    public static Entity paddleRight;
    public static Entity midLine;

    public static Entity contactRectangle;
    public static Vector2 normalVector;

    private static GlyphLayout glyphLayout;

    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;

    public static void load () {
        screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        random = new Random();

        menuItems = new Texture(Gdx.files.internal("data/menuAssets.png"));
        helpMenuItems = new Texture(Gdx.files.internal("data/helpMenu.png"));

        logo = new Sprite(new TextureRegion(menuItems, 35, 10, 200, 35));
        gameOver = new Sprite(new TextureRegion(menuItems, 0, 65, 267, 35));
        highScore = new Sprite(new TextureRegion(menuItems, 0, 120, 267, 35));
        resume = new Sprite(new TextureRegion(menuItems, 45, 175, 180, 35));
        quit = new Sprite(new TextureRegion(menuItems, 80, 230, 110, 35));
        play = new Sprite(new TextureRegion(menuItems, 80, 285, 110, 35));
        help = new Sprite(new TextureRegion(menuItems, 80, 340, 110, 35));
        font = new BitmapFont(Gdx.files.internal("data/font/font.fnt"), Gdx.files.internal("data/font/font.png"), false);
        glyphLayout = new GlyphLayout();
        xOffset = 0f;
        infoText = "";

        leftWall = new Entity(ShapeRenderer.ShapeType.Filled, Color.WHITE)
                .setSize(32,screenSize.y + 64)
                .setPosition(new Vector2(-16, screenSize.y *0.5f));
        rightWall = new Entity(ShapeRenderer.ShapeType.Filled, Color.WHITE)
                .setSize(32,screenSize.y + 64)
                .setPosition(new Vector2(screenSize.x+16, screenSize.y *0.5f));
        roof = new Entity(ShapeRenderer.ShapeType.Filled, Color.WHITE)
                .setSize(screenSize.x + 64,32)
                .setPosition(new Vector2(screenSize.x*0.5f, screenSize.y + 16f));
        floor = new Entity(ShapeRenderer.ShapeType.Filled, Color.WHITE)
                .setSize(screenSize.x + 64,32)
                .setPosition(new Vector2(screenSize.x*0.5f, -16f));
        paddleLeft = new Entity(ShapeRenderer.ShapeType.Filled, Color.WHITE)
                .setSize(16,96)
                .setPosition(screenSize.cpy().scl(0.1f, 0.5f));
        paddleLeft.maxVelocity = 800f;
        paddleRight = new Entity(ShapeRenderer.ShapeType.Filled, Color.WHITE)
                .setSize(16,96)
                .setPosition(screenSize.cpy().scl(0.9f, 0.5f));
        paddleRight.maxVelocity = 750f;
        ball = new Entity(ShapeRenderer.ShapeType.Filled, Color.WHITE)
                .setSize(16,16)
                .setPosition(screenSize.cpy().scl(0.5f));
        ball.maxVelocity = 600f;
        invisibleBall = new Entity(ShapeRenderer.ShapeType.Filled, Color.RED)
                .setSize(16,16)
                .setPosition(screenSize.cpy().scl(0.5f));
        invisibleBall.maxVelocity = 1000f;

        midLine = new Entity(ShapeRenderer.ShapeType.Filled, Color.WHITE)
                .setSize(2f, screenSize.y)
                .setPosition(screenSize.cpy().scl(0.5f, 0.5f));

        contactRectangle = new Entity(ShapeRenderer.ShapeType.Line, Color.RED);
        normalVector = new Vector2().setZero();

        music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.25f);

        jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
        highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));
    }

    public static void setInfoText(String text) {
        xOffset =0f;
        infoText = text;
        glyphLayout.setText(font, infoText);
        xOffset = Math.max(glyphLayout.width, xOffset);
        xOffset = screenSize.x*0.5f - xOffset / 2;
    }

    public static void playSound (Sound sound) {
        if (Settings.soundEnabled) sound.play(1);
    }

}