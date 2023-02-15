package com.mygdx.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Assets;
import com.mygdx.game.MainSuperJumper;
import com.mygdx.game.Settings;
import com.mygdx.game.Scene.WindowGameover;
import com.mygdx.game.Scene.WindowPause;
import com.mygdx.game.Screens.Screens;

public class GameScreen extends Screens {
	static final int STATE_RUNNING = 2;
	static final int STATE_PAUSED = 3;
	static final int STATE_GAME_OVER = 4;
	static int state;

	public WorldGame oWorld;
	private WorldGameRender renderer;

	Label lbDistancia, lbMonedas;

	Button btPause;

	WindowPause ventanPause;

	public GameScreen(MainSuperJumper game) {
		super(game);

		ventanPause = new WindowPause(this);

		oWorld = new WorldGame();
		renderer = new WorldGameRender(batcher, oWorld);

		state = STATE_RUNNING;
		Settings.numeroVecesJugadas++;

		//stage.setDebugAll(true);

		Table menuMarcador = new Table();
		menuMarcador.setSize(SCREEN_WIDTH, 40);
		menuMarcador.setY(SCREEN_HEIGHT - menuMarcador.getHeight());

		lbMonedas = new Label("", Assets.labelStyleGrande);
		lbDistancia = new Label("", Assets.labelStyleGrande);

		menuMarcador.add(new Image(new TextureRegionDrawable(Assets.coin))).left().padLeft(5);
		menuMarcador.add(lbMonedas).left();
		menuMarcador.add(lbDistancia).center().expandX();
		menuMarcador.add(new Image(new TextureRegionDrawable(Assets.gun))).height(45).width(30).left();

		btPause = new Button(Assets.btPause);
		btPause.setSize(35, 35);
		btPause.setPosition(SCREEN_WIDTH - 40, SCREEN_HEIGHT - 80);
		addEfectoPress(btPause);
		btPause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setPaused();
			}
		});

		stage.addActor(menuMarcador);
		stage.addActor(btPause);
	}

	@Override
	public void update(float delta) {
		switch (state) {
			case STATE_RUNNING:
				updateRunning(delta);
				break;
			case STATE_GAME_OVER:
				updateGameOver(delta);
				break;
		}

	}

	private void updateRunning(float delta) {

		float acelX = 0;

		acelX = -(Gdx.input.getAccelerometerX() / 3f);

		if (Gdx.input.isKeyPressed(Keys.A))
			acelX = -1;
		else if (Gdx.input.isKeyPressed(Keys.D))
			acelX = 1;

		oWorld.update(delta, acelX);

		lbMonedas.setText("x" + oWorld.coins);
		lbDistancia.setText("Score " + oWorld.maxDistance);

		if (oWorld.state == WorldGame.STATE_GAMEOVER) {
			setGameover();
		}

	}

	private void updateGameOver(float delta) {
		oWorld.update(delta, 0);

	}

	@Override
	public void draw(float delta) {
		batcher.begin();
		batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher.end();

		if (state != STATE_PAUSED) {
			renderer.render(delta);
		}
	}

	private void setPaused() {
		if (state == STATE_RUNNING) {
			state = STATE_PAUSED;
			ventanPause.showScene(stage);
		}
	}

	public void setRunning() {
		state = STATE_RUNNING;

	}

	private void setGameover() {
		state = STATE_GAME_OVER;
		Settings.setBestScore(oWorld.maxDistance);
		new WindowGameover(this).showScene(stage);

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			if (ventanPause.isVisible())
				ventanPause.hide();
			else
				setPaused();
			return true;
		}
		return super.keyDown(keycode);
	}
}


