package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AdamWar;

import Screens.PlayScreen;
import Sprites.Warrior.State;
import Tools.BodyEditorLoader;

public class Pig extends Sprite {
	public enum State {
		happy , injury, dead
	};
	private Array<Pig> pigList;
	private State currentState;
	private State previousState;
	private float stateTimer;
	private int life; 
	private Vector2 origin;
	private World world;
	private Animation<TextureRegion> happyAnimation;
	private Animation<TextureRegion> injuryAnimation;
	private Animation<TextureRegion> deadAnimation;
	public boolean isDestroyed;
	public boolean isHited;
	public boolean isHitedBySelf;
	private Body b2body;
	public Pig(World world, float x, float y, float Height , float Width ,Array<Pig> pigList) {
		this.pigList = pigList;
		life = 3;
		stateTimer = 0;
		happyAnimation = new Animation<TextureRegion>(0.5f, PlayScreen.getSpriteSheet().findRegions("pig_happy"));
		injuryAnimation = new Animation<TextureRegion>(0.5f, PlayScreen.getSpriteSheet().findRegions("pig_injury"));
		deadAnimation = new Animation<TextureRegion>(0.5f, PlayScreen.getSpriteSheet().findRegions("pig_dead"));
		this.world = world;
		this.setBounds(x, y, Height , Width );
		definePig();

	}
	private void definePig() {
		// TODO Auto-generated method stub
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("pigBody"));
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		FixtureDef fd = new FixtureDef();
		fd.density = 3f;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;
		fd.filter.categoryBits = AdamWar.PIG_BIT;
		fd.filter.maskBits = AdamWar.GROUND_BIT | AdamWar.SENSOR_BIT | AdamWar.SUN_BIT | AdamWar.SENSOR_BIT | AdamWar.PIG_BIT;	
		b2body.setTransform(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, 0);
		b2body.setUserData(this);
		loader.attachFixture(b2body, "Pig", fd, this.getWidth());
		origin = loader.getOrigin("Pig", this.getWidth()).cpy();
	}
	private TextureRegion getFrame(float dt) {
		currentState = getState();
		TextureRegion region;
        
		switch (currentState) {
		case happy:
			region = happyAnimation.getKeyFrame(stateTimer, true);
			break;
		case injury:
			region = injuryAnimation.getKeyFrame(stateTimer, true);
			break;
		default:
			region = deadAnimation.getKeyFrame(stateTimer, true);
			break;
		}

		stateTimer = previousState == currentState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}

	private State getState() {
		// TODO Auto-generated method stub
		if (life == 3) {
			return State.happy;
		} else if (life == 2)
			return State.injury;
		else 
			return State.dead;
	}
	public void destory() {
		isDestroyed = true;
	}
	public void isHited(){
		if(!isHited){
		this.life--;
		isHited = true;
		}
	}
	public void update(float dt){
		if(life <= 0)
			this.destory();
		if (!isDestroyed) {
			this.setRegion(getFrame(dt));
			Vector2 pos = b2body.getPosition().sub(origin);
			this.setPosition(pos.x, pos.y);
			this.setOrigin(origin.x, origin.y);
			this.setRotation((float) Math.toDegrees(b2body.getAngle()));
			stateTimer += dt;
		} else {
//            this.b2body.d
			this.b2body.getWorld().destroyBody(this.b2body);
			pigList.removeValue(this, false);
		}
		
	}
}
