package com.mygdx.game.Scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Assets;
import com.mygdx.game.Game.GameScreen;
import com.mygdx.game.Game.WorldGame;
import com.mygdx.game.Screens.MainMenuScreen;

public class WindowPause extends Window {

	TextButton btMenu, btResume;
	WorldGame oWorld;

	int buttonSize = 55;

	public WindowPause(final GameScreen currentScreen) {
		super(currentScreen, 350, 280, 300);
		oWorld = currentScreen.oWorld;

		Label lbPause = new Label("Pause", Assets.labelStyleGrande);
		lbPause.setFontScale(1.5f);
		lbPause.setAlignment(Align.center);
		lbPause.setPosition(getWidth() / 2f - lbPause.getWidth() / 2f, 230);
		addActor(lbPause);

		initButtons();

		Table content = new Table();

		content.defaults().expandX().uniform().fill();

		content.add(btResume);
		content.row().padTop(20);
		content.add(btMenu);

		content.pack();
		content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 50);

		addActor(content);
	}

	private void initButtons() {
		btMenu = new TextButton("Menu", Assets.textButtonStyleGrande);
		btMenu.pad(15);

		screen.addEfectoPress(btMenu);
		btMenu.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(MainMenuScreen.class, game);
			};
		});

		btResume = new TextButton("Resume", Assets.textButtonStyleGrande);
		btResume.pad(15);

		screen.addEfectoPress(btResume);
		btResume.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();

			};
		});
	}

	@Override
	public void showScene(Stage stage) {
		super.showScene(stage);
	}

	@Override
	public void hide() {
		((GameScreen) screen).setRunning();
		super.hide();
	}

}
