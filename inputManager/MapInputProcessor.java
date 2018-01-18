package inputManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AdamWar;

import Sprites.Warrior;

public class MapInputProcessor implements InputProcessor {
	// ShapeRenderer sr = new ShapeRenderer();
	// private static TiledMap map;
	private float gravityScale;
	private Warrior selectedWarrior;
	private Vector3 last_touch_down = new Vector3();
	private static Vector2 sling_position;

	public static Vector2 getSling_position() {
		return sling_position;
	}

	public static void setSling_position(Vector2 sling_position) {
		MapInputProcessor.sling_position = sling_position;

	}

	// private Vector2 backUp_position = new Vector2();
	private Vector2 center = new Vector2();
	private Vector3 point = new Vector3();
	private Vector3 point2 = new Vector3();
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	public boolean draw;

	public MapInputProcessor() {

	}

	public Viewport getGamePort() {
		return gamePort;
	}

	public void setGamePort(Viewport gamePort) {
		this.gamePort = gamePort;
	}

	private Body groundBody;
	private Body hitBody = null;
	private World world;

	public Body getGroundBody() {
		return groundBody;
	}

	public void setGroundBody(Body groundBody) {
		this.groundBody = groundBody;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	Vector3 testPoint = new Vector3();

	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture(Fixture fixture) {
			// if the hit fixture's body is the ground body
			// we ignore it
			if (fixture.getBody() == groundBody)
				return true;
			// if (fixture.get)
			// if the hit point is inside the fixture of the body
			// we report it
			if (fixture.testPoint(testPoint.x, testPoint.y) && fixture.getBody().getUserData() != null
					&& fixture.getBody().getUserData().getClass() == Warrior.class) {
				hitBody = fixture.getBody();
				selectedWarrior = (Warrior) hitBody.getUserData();
				if (selectedWarrior.isShooted) {
					selectedWarrior = null;
					hitBody = null;
					return false;
				}
				center.set(hitBody.getWorldCenter());
				hitBody.setType(BodyType.StaticBody);
				for (Fixture f : hitBody.getFixtureList()) {
					f.setSensor(true);
				}
				return false;
			} else
				return true;
		}
	};

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub

		last_touch_down.set(screenX, screenY, 0);
		// translate the mouse coordinates to world coordinates
		if (button == Input.Buttons.LEFT) {
			testPoint.set(screenX, screenY, 0);
			this.gamePort.unproject(testPoint);

			// ask the world which bodies are within the given
			// bounding box around the mouse pointer
			hitBody = null;
			world.QueryAABB(callback, testPoint.x - 0.1f, testPoint.y - 0.1f, testPoint.x + 0.1f, testPoint.y + 0.1f);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// if a mouse joint exists we simply destroy it
		if (selectedWarrior != null && selectedWarrior.isCollideSling && !selectedWarrior.isOnSling) {
			hitBody.setTransform(sling_position, 0);
			draw = false;
			selectedWarrior.isOnSling = true;
			selectedWarrior.isCollideSling = false;
			selectedWarrior = null;
			hitBody.setLinearVelocity(new Vector2(0f, 0f));
			hitBody.setAngularVelocity(0f);
			hitBody.setType(BodyType.StaticBody);
		} else if (selectedWarrior != null && !selectedWarrior.isCollideSling && !selectedWarrior.isOnSling) {
			for (Fixture f : hitBody.getFixtureList()) {
				f.setSensor(false);
			}
			hitBody.setType(BodyType.DynamicBody);
			hitBody.setTransform(center.x, center.y, 0);
			draw = false;
			selectedWarrior = null;
		} else if (selectedWarrior != null && selectedWarrior.isOnSling) {
			for (Fixture f : hitBody.getFixtureList()) {
				f.setSensor(false);
			}
			hitBody.setType(BodyType.DynamicBody);
			selectedWarrior.isShooted = true;
			selectedWarrior.isOnSling = false;
			AdamWar.manager.get("audio/sound/fly.mp3", Sound.class).play(1f);
			hitBody.applyLinearImpulse(getForceVector(screenX, screenY), hitBody.getWorldCenter(), true);
			draw = false;
			selectedWarrior = null;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		// if a mouse joint exists we simply update
		// the target of the joint based on the new
		// mouse coordinates
		if (hitBody == null)
			moveCamera(screenX, screenY);
		else {

			this.getGamePort().unproject(point2.set(screenX, screenY, 0));
			hitBody.setTransform(this.getGamePort().unproject(new Vector2(screenX, screenY)),
					-getRadian(center, point2));
			if (selectedWarrior.isOnSling)
				draw = true;
		}
		return false;
	}

	private void moveCamera(int touch_x, int touch_y) {
		// TODO Auto-generated method stub
		Vector3 new_position = getNewCameraPosition(touch_x, touch_y);

		if (!cameraOutOfLimit(new_position))

			this.getCamera().translate(new_position.sub(getCamera().position).scl(0.01f));

		last_touch_down.set(touch_x, touch_y, 0);

	}

	private boolean cameraOutOfLimit(Vector3 new_position) {
		// TODO Auto-generated method stub
		// getGamePort().unproject(new_position);
		// int x_left_limit = 0;
		// int x_right_limit = 960;
		// int y_bottom_limit = WINDOW_HEIGHT / 2;
		// int y_top_limit = terrain.getHeight() - WINDOW_HEIGHT / 2;
		//
		// if( position.x < x_left_limit || position.x > x_right_limit )
		// return true;
		// else if( position.y < y_bottom_limit || position.y > y_top_limit )
		// return true;
		// else
		// return false;

		return false;
	}

	private Vector3 getNewCameraPosition(int x, int y) {
		// TODO Auto-generated method stub
		Vector3 new_position = last_touch_down;
		new_position.sub(x, y, 0);
		new_position.y = -new_position.y;
		new_position.add(getCamera().position);
		return new_position;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public OrthographicCamera getCamera() {
		return gameCam;
	}

	public void setCamera(OrthographicCamera gameCam) {
		this.gameCam = gameCam;
	}

	public float getRadian(Vector2 v, Vector3 v2) {
		return MathUtils.atan2(v2.x - v.x, v2.y - v.y);
	}

	public Vector2 getForceVector(int x, int y) {
		// TODO Auto-generated method stub
		Vector3 new_position = new Vector3();
		new_position.set(last_touch_down);
		point.set(new_position);
		this.getGamePort().unproject(new_position);
		new_position.sub(this.getGamePort().unproject(new Vector3(x, y, 0)));
		return new Vector2(new_position.x * 3, new_position.y * 3);
	}

	public void moveSun() {

	}

	public void dragSun() {

	}

	public void dispose() {
	}

	public void render() {
		if (draw) {
			ShapeRenderer sr = new ShapeRenderer();
			sr.setColor(Color.BLACK);
			sr.setProjectionMatrix(this.getCamera().combined);
			sr.begin(ShapeType.Filled);
			sr.rectLine(center, new Vector2(point2.x, point2.y), 0.03f);
			sr.end();
			sr.dispose();
		}
		return;
	}
}
