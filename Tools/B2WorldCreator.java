package Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AdamWar;

import Screens.PlayScreen;
import Sprites.GirlFriend;
import Sprites.Pig;
import Sprites.StaticObject;
import Sprites.Warrior;

public class B2WorldCreator {
	private PlayScreen screen;
	private Array<Warrior> sunList;
	private Array<Pig> pigList;
	private Array<StaticObject> friendList;
	private Vector2 areaPosition;
	private Rectangle area;

	public Array<Warrior> getSunList() {
		return sunList;
	}

	public void setSunList(Array<Warrior> sunList) {
		this.sunList = sunList;
	}

	public B2WorldCreator(World world, TiledMap map, Viewport vp, PlayScreen screen) {
		this.screen = screen;
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		for (MapObject object : map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / AdamWar.PPM,
					(rect.getY() + rect.getHeight() / 2) / AdamWar.PPM);
			body = world.createBody(bdef);
			shape.setAsBox(rect.getWidth() / 2 / AdamWar.PPM, rect.getHeight() / 2 / AdamWar.PPM);
			fdef.shape = shape;
			fdef.friction = (float) 10;
			fdef.filter.maskBits = AdamWar.ALLTHING_BIT;
			fdef.filter.categoryBits = AdamWar.GROUND_BIT;
			body.createFixture(fdef);
		}
		for (MapObject object : map.getLayers().get("blocks").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / AdamWar.PPM,
					(rect.getY() + rect.getHeight() / 2) / AdamWar.PPM);
			body = world.createBody(bdef);
			shape.setAsBox(rect.getWidth() / 2 / AdamWar.PPM, rect.getHeight() / 2 / AdamWar.PPM);
			fdef.shape = shape;
			fdef.friction = (float) 10;
			fdef.filter.maskBits = AdamWar.ALLTHING_BIT;
			fdef.filter.categoryBits = AdamWar.GROUND_BIT;
			body.createFixture(fdef);
		}
		for (MapObject object : map.getLayers().get("sensor").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / AdamWar.PPM,
					(rect.getY() + rect.getHeight() / 2) / AdamWar.PPM);
			body = world.createBody(bdef);
			shape.setAsBox(rect.getWidth() / 2 / AdamWar.PPM, rect.getHeight() / 2 / AdamWar.PPM);
			fdef.shape = shape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = AdamWar.SENSOR_BIT;
			body.createFixture(fdef);
		}
		for (MapObject object : map.getLayers().get("Area").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			area = new Rectangle(rect.getX() / AdamWar.PPM, rect.getY() / AdamWar.PPM, rect.getWidth() / AdamWar.PPM,
					rect.getHeight() / AdamWar.PPM);
			setAreaPosition(area.getCenter(new Vector2()));
		}
		sunList = new Array<Warrior>();
		for (MapObject object : map.getLayers().get("angrySun").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			sunList.add(new Warrior(world, rect.getX() / AdamWar.PPM, rect.getY() / AdamWar.PPM, rect.getWidth() / AdamWar.PPM , rect.getHeight() / AdamWar.PPM , getSunList()));
		}
		Warrior.setArea(area);
		pigList = new Array<Pig>();
		for (MapObject object : map.getLayers().get("pig").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			pigList.add(new Pig(world, rect.getX() / AdamWar.PPM, rect.getY() / AdamWar.PPM, rect.getWidth() / AdamWar.PPM , rect.getHeight() / AdamWar.PPM , getPigList()));
		}
		friendList = new Array<StaticObject>();
		for (MapObject object : map.getLayers().get("girlfriend").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			shape.setAsBox(rect.getWidth() / 2 / AdamWar.PPM, rect.getHeight() / 2 / AdamWar.PPM);
			friendList.add(new GirlFriend(getFriendList(), world, rect.getX() / AdamWar.PPM, rect.getY() / AdamWar.PPM,
					rect.getWidth(), rect.getHeight(), shape));
		}
	}

	public Vector2 getAreaPosition() {
		return areaPosition;
	}

	public void setAreaPosition(Vector2 areaPosition) {
		this.areaPosition = areaPosition;
	}

	public Array<StaticObject> getFriendList() {
		return friendList;
	}

	public void setFriendList(Array<StaticObject> friendList) {
		this.friendList = friendList;
	}

	public Array<Pig> getPigList() {
		return pigList;
	}

	public void setPigList(Array<Pig> pigList) {
		this.pigList = pigList;
	}
}
