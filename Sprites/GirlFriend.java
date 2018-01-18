package Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AdamWar;

import Scenes.Hud;
import Screens.PlayScreen;

public class GirlFriend extends StaticObject {

	public GirlFriend(Array<StaticObject> friendList , World world , float x, float y, float weight, float height , Shape shape){
		super(friendList , world , x , y , weight , height , shape , "girlfriend");
	}

	public void update(float dt){
		if (!isDestroyed) {
			this.setRegion(animation.getKeyFrame(stateTimer, true));
			stateTimer += dt;
		} else {
			AdamWar.manager.get("audio/sound/hittarget.wav", Sound.class).play(0.5f);
		    Hud.addScore(50);
			this.b2body.getWorld().destroyBody(this.b2body);
			staticObjectList.removeValue(this, false);
		}
	}
	public void destory() {
		isDestroyed = true;
	}

	@Override
	void defineStaticObject(Shape shape) {
		// TODO Auto-generated method stub
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2);
		b2body = world.createBody(bdef);
		FixtureDef fd = new FixtureDef();
		fd.density = 5f;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;
		fd.shape = shape;
		fd.filter.categoryBits = AdamWar.GIRL_BIT;
		fd.filter.maskBits = AdamWar.SUN_BIT;
		fd.isSensor = true;
		b2body.createFixture(fd);
		b2body.setUserData(this);
	}
 
}
