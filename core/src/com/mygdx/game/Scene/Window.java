package com.mygdx.game.Scene;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.I18NBundle;
import com.mygdx.game.MainSuperJumper;
import com.mygdx.game.Screens.Screens;

public abstract class Window extends Group {
	public static final float DURACION_ANIMATION = .5f;
	protected Screens screen;
	protected I18NBundle idiomas;
	protected MainSuperJumper game;

	private boolean isVisible = false;

	public Window(Screens currentScreen, float width, float height, float positionY) {
		screen = currentScreen;
		game = currentScreen.game;
		setSize(width, height);
		setY(positionY);

	}

	public void showScene(Stage stage) {

		setOrigin(getWidth() / 2f, getHeight() / 2f);
		setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f);

		setScale(.5f);
		addAction(Actions.sequence(Actions.scaleTo(1, 1, DURACION_ANIMATION)));

		isVisible = true;
		stage.addActor(this);

	}

	public boolean isVisible() {
		return isVisible;
	}

	public void hide() {
		remove();
	}
}
