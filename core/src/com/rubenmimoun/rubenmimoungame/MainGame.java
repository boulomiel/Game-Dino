package com.rubenmimoun.rubenmimoungame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.screens.BossScreen;

public class MainGame extends Game {

	private SpriteBatch batch;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		GameManager.getInstance().initializeGameData();
		setScreen(new BossScreen(this));
	}



	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();

	}


	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public Screen getScreen() {
		return super.getScreen();
	}
}
