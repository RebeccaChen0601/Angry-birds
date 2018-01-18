package Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AdamWar;

import Screens.PlayScreen;
import Tools.LevelManager;

public class Hud implements Disposable{
	
  public Stage stage;
  private Viewport viewPort;
   
  private Integer worldTimer;
  private static Integer score;
  
 
  private static Label scoreLabel;
  private Label worldLabel;
  private Label birdsLabel;
  private Label levelLabel;
  
  public Hud(SpriteBatch sb){
	  worldTimer = 300;
	  score = 0;
	 
	  viewPort = new FitViewport(AdamWar.V_WIDTH, AdamWar.V_HEIGHT, new OrthographicCamera());
	  stage = new Stage(viewPort, sb);
	  
	  Table table = new Table();
	  table.top();
	  table.setFillParent(true);
	  
	
	  scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
	  if(PlayScreen.mapPath.equals(LevelManager.LEVEL_ONE))
	  levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
	  if(PlayScreen.mapPath.equals(LevelManager.LEVEL_TWO))
	  levelLabel = new Label("1-2", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
	  
	  worldLabel =  new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
	  birdsLabel = new Label("AngryBird", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
	  
	  table.add(birdsLabel).expandX().padTop(10);
	  table.add(worldLabel).expandX().padTop(10);
	  table.row();
	  table.add(scoreLabel).expandX().padTop(10);
	  table.add(levelLabel).expandX().padTop(10);
	  
	  stage.addActor(table);
  }
  public void update(){
	  
	  scoreLabel.setText(String.format("%06d", score));
  }
  
  public static void addScore(int value){
	  score += value; 
  }

@Override
public void dispose() {
	// TODO Auto-generated method stub
	stage.dispose();
}
}