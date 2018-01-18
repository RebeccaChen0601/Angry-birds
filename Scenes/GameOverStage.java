package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.AdamWar;

import Screens.PlayScreen;
import Tools.LevelManager;

public class GameOverStage implements Disposable {
	public Stage stage;
	private PlayScreen screen;
	TextureAtlas atlas = AdamWar.manager.get("Skin/craftacular/skin/craftacular-ui.atlas", TextureAtlas.class);
	Skin skin = new Skin(Gdx.files.internal("Skin/craftacular/skin/craftacular-ui.json"), atlas);
	TextButton button;
	TextButton button2;
	TextButton button3;

	Label title;

	public GameOverStage(final PlayScreen screen) {
		// TODO Auto-generated constructor stub

		this.screen = screen;
		stage = new Stage(new FitViewport(AdamWar.V_WIDTH + 150f, AdamWar.V_HEIGHT));
		
		Gdx.input.setInputProcessor(stage);

		title = new Label("Game Over", skin);
		title.setBounds(320f, 300f, 200f, 120f);
		title.setFontScale(1.5f);
		title.setColor(Color.ROYAL);

		button = new TextButton("Restart", skin, "default");
		button2 = new TextButton("next", skin, "default");
		button3 = new TextButton("quit", skin, "default");

		button.setColor(Color.BLUE);
		button2.setColor(Color.GOLD);
		button3.setColor(Color.FOREST);

		button.setWidth(150f);
		button.setHeight(40f);
		button.setPosition(150f, 225f);

		button.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				screen.music.stop();
				screen.dispose();
				screen.game.setScreen(new PlayScreen(screen.game, screen.mapPath));
				skin.dispose();
				stage.dispose();
			}
		});

		button2.setWidth(150f);
		button2.setHeight(40f);
		button2.setPosition(400f, 225f);

		button2.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				screen.music.stop();
				screen.dispose();
				if (screen.mapPath.equals(LevelManager.LEVEL_ONE))
					screen.game.setScreen(new PlayScreen(screen.game, LevelManager.LEVEL_TWO));
				else
					screen.game.setScreen(new PlayScreen(screen.game, LevelManager.LEVEL_ONE));

				stage.dispose();
				skin.dispose();
			}
		});

		button3.setWidth(150f);
		button3.setHeight(40f);
		button3.setPosition(650f, 225f);

		button3.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});

		stage.addActor(button);
		stage.addActor(button2);
		stage.addActor(button3);
		stage.addActor(title);

	}

	public void update(float dt) {

	}

	public void render(float dt) {
		stage.act(dt);
		stage.draw();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		skin.dispose();
		stage.dispose();
	}
}
