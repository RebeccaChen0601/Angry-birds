package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AdamWar;

import Screens.PlayScreen;
import Tools.B2WorldCreator;
import Tools.BodyEditorLoader;


public class Warrior extends Sprite {
	public enum State {
		angry, happy
	};
	private Array<Warrior> sunList;
	private State currentState;
	private State previousState;
	private float stateTimer;
	private Vector2 origin;
	private World world;
	private Animation<TextureRegion> angryAnimation;
	private Animation<TextureRegion> happyAnimation;
	public boolean isDestroyed;
	public boolean isOnSling = false;
	public boolean isShooted = false;
	public boolean isCollideSling = false;
	private static Rectangle area;
	private Body b2body;

	public Warrior(World world, float x, float y, float Height , float Width , Array<Warrior> sunList) {
		this.sunList = sunList;
		stateTimer = 0;
		angryAnimation = new Animation<TextureRegion>(0.1f, PlayScreen.getSpriteSheet().findRegions("angry"));
		happyAnimation = new Animation<TextureRegion>(0.1f, PlayScreen.getSpriteSheet().findRegions("happy"));
		this.world = world;
		this.setBounds(x, y,Height, Width);
		defineWarrior();
	}

	public void update(float dt) {
		if(area.contains(this.b2body.getWorldCenter())){
			this.isCollideSling = true;
		}else{
			this.isCollideSling = false;
		}
		if(this.isShooted && this.b2body.getLinearVelocity().x == 0 && this.b2body.getLinearVelocity().y == 0)
			this.destory();
		if (!isDestroyed) {
			this.setRegion(getFrame(dt));
			Vector2 pos = b2body.getPosition().sub(origin);
			this.setPosition(pos.x, pos.y);
			this.setOrigin(origin.x, origin.y);
			this.setRotation((float) Math.toDegrees(b2body.getAngle()));
		} else {
			this.b2body.getWorld().destroyBody(this.b2body);
			sunList.removeValue(this, false);
		}
	}
	private TextureRegion getFrame(float dt) {
		currentState = getState();
		TextureRegion region;

		switch (currentState) {
		case happy:
			region = happyAnimation.getKeyFrame(stateTimer, true);
			break;
		case angry:
			region = angryAnimation.getKeyFrame(stateTimer, true);
			break;
		default:
			region = happyAnimation.getKeyFrame(stateTimer, true);
		}

		stateTimer = previousState == currentState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}

	private State getState() {
		// TODO Auto-generated method stub
		if (this.b2body.getLinearVelocity().x != 0 && this.b2body.getLinearVelocity().y != 0) {
			return State.angry;
		} else
			return State.happy;
	}
	private void defineWarrior() {
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("sunBody"));
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		FixtureDef fd = new FixtureDef();
		fd.density = 5f;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;
		fd.filter.categoryBits = AdamWar.SUN_BIT;
		fd.filter.maskBits = AdamWar.GROUND_BIT | AdamWar.SENSOR_BIT | AdamWar.SUN_BIT | AdamWar.GIRL_BIT | AdamWar.PIG_BIT;	
		b2body.setTransform(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, 0);
		b2body.setUserData(this);
		System.out.println(this.getWidth());
		loader.attachFixture(b2body, "Sun", fd, this.getWidth());
		origin = loader.getOrigin("Sun", this.getWidth()).cpy();
	}

	public void putOnSling(){
		isOnSling = true;
	}
	public void destory() {
		isDestroyed = true;
	}
	
	public static Rectangle getArea() {
		return area;
	}

	public static void setArea(Rectangle area) {
		Warrior.area = area;
	}
}
