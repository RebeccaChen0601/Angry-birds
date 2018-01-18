package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AdamWar;

import Screens.PlayScreen;

public class DebugMouseTracker implements Disposable{
	  public Stage stage;
	  private Viewport gameViewPort;
	  private Viewport viewPort;
	  private Integer worldTimer;
//	  private PlayScreen ps;
	  Vector3 mouse_position;
	  public int mouseXtoScreen;
	  public int mouseYtoScreen;
	  public int mouseXtoWorld;
	  public int mouseYtoWorld;
	 public Label mouseXtoScreenLabel;
	 public  Label mouseYtoScreenLabel;
	 public Label mouseXtoWorldLabel;
	 public Label mouseYtoWorldLabel;
	 Table table;
	  
	  public DebugMouseTracker(SpriteBatch sb, Viewport vp){
		  mouse_position = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
//		  mouse_position.
		  gameViewPort = vp;
		  viewPort = new FitViewport(AdamWar.V_WIDTH, AdamWar.V_HEIGHT, new OrthographicCamera());
		  stage = new Stage(viewPort, sb);
		  table = new Table();  
		  table.bottom();
		  table.setFillParent(true);
		  mouseXtoScreenLabel = new Label("x to Scr:"+" "+mouseXtoScreen, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		  mouseYtoScreenLabel = new Label("y to Scr:"+" "+mouseYtoScreen, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		  mouseXtoWorldLabel = new Label("x to Wor:"+" "+mouseXtoWorld, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		  mouseYtoWorldLabel = new Label("y to Wor:"+" "+mouseXtoWorld, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		  table.add(mouseXtoScreenLabel).expandX().padRight(5);
		  table.add(mouseYtoScreenLabel).expandX().padRight(5);
		  table.row();
		  table.add(mouseXtoWorldLabel).expandX().padRight(5);
		  table.add(mouseYtoWorldLabel).expandX().padRight(5);
		  
		  stage.addActor(table);
	  }
	  public void update(){
		  mouse_position.set(Gdx.input.getX(),Gdx.input.getY(),0);
		  gameViewPort.unproject(mouse_position);
		  mouseXtoScreen = (int) ((mouse_position.x - gameViewPort.getCamera().position.x + gameViewPort.getWorldWidth() / 2) * AdamWar.PPM);
		  mouseYtoScreen = (int) ((mouse_position.y - gameViewPort.getCamera().position.y + gameViewPort.getWorldHeight() / 2) * AdamWar.PPM);
		  mouseXtoWorld =  (int)(mouse_position.x * AdamWar.PPM);
		  mouseYtoWorld = (int)(mouse_position.y * AdamWar.PPM);
		  mouseXtoScreenLabel.setText("x to Scr:"+" "+mouseXtoScreen);
		  mouseYtoScreenLabel.setText("y to Scr:"+" "+mouseYtoScreen);
		  mouseXtoWorldLabel.setText("x to Wor:"+" "+mouseXtoWorld);
		  mouseYtoWorldLabel.setText("y to Wor:"+" "+mouseYtoWorld);
	  }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
	}
}
