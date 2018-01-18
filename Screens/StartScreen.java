package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AdamWar;

import Tools.LevelManager;

public class StartScreen implements Screen {

//	StartScreen sc;
	Stage stage;
	private Viewport gamePort;
	private AdamWar game;
	TextureAtlas atlas = AdamWar.manager.get("Skin/craftacular/skin/craftacular-ui.atlas", TextureAtlas.class);
	private Skin skin = new Skin(Gdx.files.internal("Skin/craftacular/skin/craftacular-ui.json"), atlas);
//	private OrthographicCamera camera;;
	private Label title;
    private Texture background;
	public StartScreen(final AdamWar game) {
		// TODO Auto-generated constructor stub
	
		    this.game = game;
		gamePort = new FitViewport(AdamWar.V_WIDTH + 150f, AdamWar.V_HEIGHT);
		    stage = new Stage(gamePort);
	        Gdx.input.setInputProcessor(stage);// Make the stage consume events
		    title = new Label("Angry sun!", skin);
		    title.setBounds(350f, 350f, 400f, 150f);
		    title.setColor(Color.YELLOW);
		    title.setFontScale(1.5f);
		    stage.addActor(title);
	        TextButton newGameButton = new TextButton("New game", skin); // Use the initialized skin
	        newGameButton.setPosition(400,300);
	        newGameButton.setBounds(375,250, 200 , 50);
	        
	        newGameButton.addListener(new ChangeListener() {
				public void changed(ChangeEvent event, Actor actor) {
			        dispose();
			        game.setScreen(new PlayScreen(game , LevelManager.LEVEL_ONE ));
				}
			});
	        
	        stage.addActor(newGameButton);
	        TextButton newGameButton2 = new TextButton("Quit game", skin); // Use the initialized skin
	        newGameButton2.setPosition(400,300);
	        newGameButton2.setBounds(375, 150, 200 ,50);
	        
	        newGameButton2.addListener(new ChangeListener() {
				public void changed(ChangeEvent event, Actor actor) {
					Gdx.app.exit();
				}
			});
	        stage.addActor(newGameButton2);
	        background = new Texture(Gdx.files.internal("backGroundImage/background.png"));
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub\
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
//       skin.dispose();
       stage.dispose();
       background.dispose();
	}

}
