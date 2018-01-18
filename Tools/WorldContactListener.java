package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.AdamWar;

import Sprites.GirlFriend;
import Sprites.Pig;
import Sprites.Warrior;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int code = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch(code){
       
        case AdamWar.SUN_BIT | AdamWar.GIRL_BIT: 
        	if (fixA.getFilterData().categoryBits == AdamWar.GIRL_BIT) {
				((GirlFriend) fixA.getBody().getUserData()).destory();
			} else {
				((GirlFriend) fixB.getBody().getUserData()).destory();
			}
        	break;
        
        
	  case AdamWar.SUN_BIT | AdamWar.PIG_BIT: 
      	if (fixA.getFilterData().categoryBits == AdamWar.PIG_BIT) {
				((Pig) fixA.getBody().getUserData()).isHited();
			} else {
				((Pig) fixB.getBody().getUserData()).isHited();
			}
      	break;
      }

	}

	@Override
	public void endContact(Contact contact) {
	     
		// TODO Auto-generated method stub
		Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
		 int code = fixA.getFilterData().categoryBits| fixB.getFilterData().categoryBits;
	        switch(code){
	        case AdamWar.SENSOR_BIT | AdamWar.SUN_BIT:
			if (fixA.getFilterData().categoryBits == AdamWar.SUN_BIT) {
				((Warrior) fixA.getBody().getUserData()).destory();
			} else {
				((Warrior) fixB.getBody().getUserData()).destory();
			}
			break;
	        case AdamWar.PIG_BIT | AdamWar.SENSOR_BIT:
				if (fixA.getFilterData().categoryBits == AdamWar.PIG_BIT) {
					((Pig) fixA.getBody().getUserData()).destory();
				} else {
					((Pig) fixB.getBody().getUserData()).destory();
				}
				break;
	        case AdamWar.SUN_BIT | AdamWar.PIG_BIT: 
	          	if (fixA.getFilterData().categoryBits == AdamWar.PIG_BIT) {
	    				((Pig) fixA.getBody().getUserData()).isHited = false;
	    			} else {
	    				((Pig) fixB.getBody().getUserData()).isHited = false;
	    			}
	          	break;
	        case AdamWar.GROUND_BIT | AdamWar.SUN_BIT:
                AdamWar.manager.get("audio/sound/collision.wav", Sound.class).play(0.3f);
				break;
	        }
	        
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
