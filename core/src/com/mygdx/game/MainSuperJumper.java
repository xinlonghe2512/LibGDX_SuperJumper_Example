package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Screens.MainMenuScreen;
import com.mygdx.game.Screens.Screens;

public class MainSuperJumper extends Game {
	public Stage stage;
	public SpriteBatch batcher;

	public MainSuperJumper() {
	}

	@Override
	public void create() {
		stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

		batcher = new SpriteBatch();
		Settings.load();
		Assets.load();

		setScreen(new MainMenuScreen(this));
	}
}
