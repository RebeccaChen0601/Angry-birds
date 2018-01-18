package Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AdamWar;

import Screens.PlayScreen;

public abstract class StaticObject extends Sprite{
    protected Array<StaticObject> staticObjectList;
    protected float stateTimer;
	protected World world;
	protected Animation<TextureRegion> animation;
	protected String regionName;
	protected Body b2body;
	protected boolean isDestroyed;
	public StaticObject(Array<StaticObject> staticObjectList , World world , float x, float y, float Width , float Height , Shape shape, String regionName){
		this.world = world;
		this.staticObjectList = staticObjectList;
		stateTimer = 0;
		this.setBounds(x, y, Width / AdamWar.PPM, Height / AdamWar.PPM);
		defineStaticObject(shape);
		animation = new Animation<TextureRegion>(0.1f,PlayScreen.getSpriteSheet().findRegions(regionName));
	}
	abstract void defineStaticObject(Shape shape);
	public abstract void update(float dt);


}
