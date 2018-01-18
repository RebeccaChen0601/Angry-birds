package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import Screens.PlayScreen;
import Screens.StartScreen;
import Tools.LevelManager;

public class AdamWar extends Game {
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 600;
	public static final float PPM = 100;
	public SpriteBatch batch;
	public static final short NOTHING_BIT = 0;
	public static final short SENSOR_BIT = 8;
	public static final short PIG_BIT = 1;
	public static final short SUN_BIT = 2;
	public static final short GROUND_BIT= 4;
	public static final short GIRL_BIT = 16;
	public static final short ALLTHING_BIT = PIG_BIT | SUN_BIT | GROUND_BIT;
	public static AssetManager manager;
	
	public void create () {
		
		batch = new SpriteBatch();

		manager = new AssetManager();
		manager.load("audio/music/main-theme.mp3", Music.class);
		manager.load("audio/sound/fly.mp3",Sound.class);
		manager.load("audio/sound/collision.wav",Sound.class);
		manager.load("audio/sound/hittarget.wav",Sound.class);
		manager.load("audio/sound/pig.mp3",Sound.class);
		manager.load("Skin/craftacular/skin/craftacular-ui.atlas", TextureAtlas.class);
		manager.finishLoading();
		setScreen(new StartScreen(this));
	}

	
	public void render () {
		super.render();
	}
	@Override
    public void dispose(){
    	batch.dispose();
    	manager.dispose();
    }
}
