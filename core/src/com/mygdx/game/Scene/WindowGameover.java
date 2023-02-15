package com.mygdx.game.Scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Assets;
import com.mygdx.game.Settings;
import com.mygdx.game.Game.GameScreen;
import com.mygdx.game.Game.WorldGame;
import com.mygdx.game.Screens.MainMenuScreen;

public class WindowGameover extends Window {

	TextButton btMenu, btTryAgain;
	WorldGame oWorld;

	int buttonSize = 55;

	public WindowGameover(final GameScreen currentScreen) {
		super(currentScreen, 350, 400, 250);
		oWorld = currentScreen.oWorld;

		Label lbShop = new Label("Game over!", Assets.labelStyleGrande);
		lbShop.setFontScale(1.5f);
		lbShop.setAlignment(Align.center);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 350);
		addActor(lbShop);

		initButtons();

		final Table scoreTable = new Table();
		scoreTable.setSize(getWidth(), 130);
		scoreTable.setY(230);

		Label lbScore = new Label("Current Score", Assets.labelStyleChico);
		lbScore.setAlignment(Align.left);

		Label lblNumScore = new Label(oWorld.maxDistance + "", Assets.labelStyleChico);
		lblNumScore.setAlignment(Align.right);

		Label lbBestScore = new Label("Best Score", Assets.labelStyleChico);
		lbScore.setAlignment(Align.left);

		Label lbBestNumScore = new Label(Settings.bestScore + "", Assets.labelStyleChico);
		lblNumScore.setAlignment(Align.right);

		scoreTable.pad(10);
		scoreTable.add(lbScore).left();
		scoreTable.add(lblNumScore).right().expand();

		scoreTable.row();
		scoreTable.add(lbBestScore).left();
		scoreTable.add(lbBestNumScore).right().expand();

		addActor(scoreTable);

		Table content = new Table();

		content.defaults().expandX().uniform().fill();

		content.add(btTryAgain);
		content.row().padTop(20);
		content.add(btMenu);

		content.pack();
		content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 60);

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

		btTryAgain = new TextButton("Try again", Assets.textButtonStyleGrande);
		btTryAgain.pad(15);

		screen.addEfectoPress(btTryAgain);
		btTryAgain.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(GameScreen.class, game);
			};
		});
	}

	@Override
	public void showScene(Stage stage) {
		super.showScene(stage);
	}

}
