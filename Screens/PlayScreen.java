package Screens;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AdamWar;

import Scenes.DebugMouseTracker;
import Scenes.GameOverStage;
import Scenes.Hud;
import Sprites.GirlFriend;
import Sprites.Pig;
import Sprites.StaticObject;
import Sprites.Warrior;
import Tools.B2WorldCreator;
import Tools.WorldContactListener;
import inputManager.MapInputProcessor;

public class PlayScreen implements Screen {
	private boolean gameOver;
	public Music music;
	static TextureAtlas spriteSheet;
	public static String mapPath;
	public AdamWar game;
	private MapInputProcessor mapInput;
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	private OrthographicCamera gameCam2;
	private Hud hud;
	private GameOverStage gs;
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	// Box2d variables
	private World world;
	private Box2DDebugRenderer b2dr;
	private Body groundBody;
	private B2WorldCreator creator;
	DebugMouseTracker mouseTracker;

	public PlayScreen(AdamWar game, String mapPath) {
		init(game, mapPath);
	}

	// @Override
	// public void show() {
	//
	// }
	public void update(float dt) {

		mouseTracker.update();

		world.step(1 / 60f, 6, 2);

		hud.update();

		for (Warrior sun : creator.getSunList())
			sun.update(dt);
		
		for (StaticObject gf : creator.getFriendList())
			gf.update(dt);

		for (Pig pig : creator.getPigList())
			pig.update(dt);

		if (creator.getSunList().size == 0) {
			gameOver = true;
		}

	}

	public void renderGame() {

		gamePort.apply();

		renderer.setView(gameCam);

		renderer.render();

		game.batch.setProjectionMatrix(gameCam.combined);

		game.batch.begin();

		for (Warrior sun : creator.getSunList())
			sun.draw(game.batch);

		for (StaticObject gf : creator.getFriendList())
			gf.draw(game.batch);

		for (Pig pig : creator.getPigList())
			pig.draw(game.batch);

		game.batch.end();

		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

		mouseTracker.stage.draw();

		hud.stage.draw();

		mapInput.render();

	}

	public void render(float delta) {
		
		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderGame();
		
		b2dr.render(world, gameCam.combined);
		
		if (gameOver && gs == null) {
			gs = new GameOverStage(this);
		}
		
		if(gs != null)
			gs.render(delta);
		
	}

	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	public void pause() {
		// TODO Auto-generated method stub
	}

	public void resume() {
		// TODO Auto-generated method stub
	}

	public void hide() {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

		hud.dispose();
		map.dispose();
		renderer.dispose();
		spriteSheet.dispose();
		mouseTracker.dispose();
		music.dispose();
		b2dr.dispose();
		world.dispose();

	}

	public static TextureAtlas getSpriteSheet() {
		return spriteSheet;
	}

	public static void setSpriteSheet(TextureAtlas spriteSheet) {
		PlayScreen.spriteSheet = spriteSheet;
	}

	@Override
	public void show() {

		// TODO Auto-generated method stub

	}

	public void init(AdamWar game, String mapPath) {
		this.game = game;
		this.mapPath = mapPath;
		game.batch = new SpriteBatch();
		spriteSheet = new TextureAtlas("spritesheet/sun.txt");
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(AdamWar.V_WIDTH / AdamWar.PPM, AdamWar.V_HEIGHT / AdamWar.PPM, gameCam);
		mapInput = new MapInputProcessor();
		mapInput.setCamera(gameCam);
		mapInput.setGamePort(gamePort);
		gameCam2 = new OrthographicCamera();
		hud = new Hud(game.batch);
		mapLoader = new TmxMapLoader();
		map = mapLoader.load(mapPath);
		renderer = new OrthogonalTiledMapRenderer(map, 1 / AdamWar.PPM);
		world = new World(new Vector2(0, -10), true);
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		// System.out.println(gamePort.getWorldWidth());
		world.setContactListener(new WorldContactListener());
		b2dr = new Box2DDebugRenderer();
		creator = new B2WorldCreator(world, map, gamePort, this);
		// gf = new GirlFriend(world,1f,1f);
		mouseTracker = new DebugMouseTracker(game.batch, gamePort);
		mapInput.setWorld(world);
		// mapInput.setGravityScale(1.0f);
		BodyDef bodyDef = new BodyDef();
		groundBody = world.createBody(bodyDef);
		mapInput.setGroundBody(this.groundBody);
		mapInput.setGamePort(gamePort);
		MapInputProcessor.setSling_position(creator.getAreaPosition());
		Gdx.input.setInputProcessor(mapInput);
		music = AdamWar.manager.get("audio/music/main-theme.mp3", Music.class);
		music.setVolume(0.5f);
		music.isLooping();
		music.play();
	}
    
	public Viewport getGamePort() {
		return gamePort;
	}

	public void setGamePort(Viewport gamePort) {
		this.gamePort = gamePort;
	}

}
