package com.mygdx.game.Game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.mygdx.game.Assets;
import com.mygdx.game.Object.Enemy;
import com.mygdx.game.Object.Item;
import com.mygdx.game.Object.Coin;
import com.mygdx.game.Object.Player;
import com.mygdx.game.Object.Platform;
import com.mygdx.game.Object.PlataformPiece;
import com.mygdx.game.Screens.Screens;

public class WorldGameRender {
	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	public WorldGame world;
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private Box2DDebugRenderer boxRender;

	public WorldGameRender(SpriteBatch batch, WorldGame world) {
		this.world = world;
		this.batch = batch;

		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

		boxRender = new Box2DDebugRenderer();
	}

	public void unprojectToWorldCoords(Vector3 touchPoint) {
		cam.unproject(touchPoint);
	}

	public void render(float delta) {
		if (world.state == WorldGame.STATE_RUNNING)
			cam.position.y = world.player.position.y;

		if (cam.position.y < Screens.WORLD_HEIGHT / 2f) {
			cam.position.y = Screens.WORLD_HEIGHT / 2f;
		}

		cam.update();
		batch.setProjectionMatrix(cam.combined);

		batch.begin();

		renderPlayer();
		renderPlatforms();
		renderPlatformPieces();
		renderCoins();
		renderItems();
		renderEnemy();

		batch.end();

		boxRender.render(world.world, cam.combined);
	}

	private void renderPlayer() {
		AtlasRegion keyframe = null;

		Player obj = world.player;

		if (obj.velocity.y > 0)
			keyframe = Assets.personajeJump;
		else
			keyframe = Assets.personajeStand;

		if (obj.velocity.x > 0)
			batch.draw(keyframe, obj.position.x + Player.DRAW_WIDTH / 2f, obj.position.y - Player.DRAW_HEIGTH / 2f,
					-Player.DRAW_WIDTH / 2f, Player.DRAW_HEIGTH / 2f, -Player.DRAW_WIDTH, Player.DRAW_HEIGTH, 1, 1, obj.angleDeg);

		else
			batch.draw(keyframe, obj.position.x - Player.DRAW_WIDTH / 2f, obj.position.y - Player.DRAW_HEIGTH / 2f,
					Player.DRAW_WIDTH / 2f, Player.DRAW_HEIGTH / 2f, Player.DRAW_WIDTH, Player.DRAW_HEIGTH, 1, 1, obj.angleDeg);

		if (obj.isJetPack) {
			batch.draw(Assets.jetpack, obj.position.x - .45f / 2f, obj.position.y - .7f / 2f, .45f, .7f);

			TextureRegion fireFrame = Assets.jetpackFire.getKeyFrame(obj.durationJetPack, true);
			batch.draw(fireFrame, obj.position.x - .35f / 2f, obj.position.y - .95f, .35f, .6f);

		}
		if (obj.isBubble) {
			batch.draw(Assets.bubble, obj.position.x - .5f, obj.position.y - .5f, 1, 1);
		}

	}

	private void renderPlatforms() {
		Iterator<PlataformPiece> i = world.platformArray.iterator();
		while (i.hasNext()) {
			PlataformPiece obj = i.next();

			AtlasRegion keyframe = null;

			if (obj.tipo == PlataformPiece.TIPO_ROMPIBLE) {
				switch (obj.color) {
					case PlataformPiece.COLOR_BEIGE:
						keyframe = Assets.plataformaBeigeBroken;
						break;
					case PlataformPiece.COLOR_BLUE:
						keyframe = Assets.plataformaBlueBroken;
						break;
					case PlataformPiece.COLOR_GRAY:
						keyframe = Assets.plataformaGrayBroken;
						break;
					case PlataformPiece.COLOR_GREEN:
						keyframe = Assets.plataformaGreenBroken;
						break;
					case PlataformPiece.COLOR_MULTICOLOR:
						keyframe = Assets.plataformaMulticolorBroken;
						break;
					case PlataformPiece.COLOR_PINK:
						keyframe = Assets.plataformaPinkBroken;
						break;

				}
			}
			else {
				switch (obj.color) {
					case PlataformPiece.COLOR_BEIGE:
						keyframe = Assets.plataformaBeige;
						break;
					case PlataformPiece.COLOR_BLUE:
						keyframe = Assets.plataformaBlue;
						break;
					case PlataformPiece.COLOR_GRAY:
						keyframe = Assets.plataformaGray;
						break;
					case PlataformPiece.COLOR_GREEN:
						keyframe = Assets.plataformaGreen;
						break;
					case PlataformPiece.COLOR_MULTICOLOR:
						keyframe = Assets.plataformaMulticolor;
						break;
					case PlataformPiece.COLOR_PINK:
						keyframe = Assets.plataformaPink;
						break;
					case PlataformPiece.COLOR_BEIGE_LIGHT:
						keyframe = Assets.plataformaBeigeLight;
						break;
					case PlataformPiece.COLOR_BLUE_LIGHT:
						keyframe = Assets.plataformaBlueLight;
						break;
					case PlataformPiece.COLOR_GRAY_LIGHT:
						keyframe = Assets.plataformaGrayLight;
						break;
					case PlataformPiece.COLOR_GREEN_LIGHT:
						keyframe = Assets.plataformaGreenLight;
						break;
					case PlataformPiece.COLOR_MULTICOLOR_LIGHT:
						keyframe = Assets.plataformaMulticolorLight;
						break;
					case PlataformPiece.COLOR_PINK_LIGHT:
						keyframe = Assets.plataformaPinkLight;
						break;
				}

			}
			batch.draw(keyframe, obj.position.x - PlataformPiece.DRAW_WIDTH_NORMAL / 2f, obj.position.y - PlataformPiece.DRAW_HEIGTH_NORMAL / 2f,
					PlataformPiece.DRAW_WIDTH_NORMAL, PlataformPiece.DRAW_HEIGTH_NORMAL);
		}
	}

	private void renderPlatformPieces() {
		Iterator<Platform> i = world.platformPiecesArray.iterator();
		while (i.hasNext()) {
			Platform obj = i.next();

			AtlasRegion keyframe = null;

			if (obj.tipo == Platform.TYPE_LEFT) {
				switch (obj.color) {
					case PlataformPiece.COLOR_BEIGE:
						keyframe = Assets.plataformaBeigeLeft;
						break;
					case PlataformPiece.COLOR_BLUE:
						keyframe = Assets.plataformaBlueLeft;
						break;
					case PlataformPiece.COLOR_GRAY:
						keyframe = Assets.plataformaGrayLeft;
						break;
					case PlataformPiece.COLOR_GREEN:
						keyframe = Assets.plataformaGreenLeft;
						break;
					case PlataformPiece.COLOR_MULTICOLOR:
						keyframe = Assets.plataformaMulticolorLeft;
						break;
					case PlataformPiece.COLOR_PINK:
						keyframe = Assets.plataformaPinkLeft;
						break;

				}
			}
			else {
				switch (obj.color) {
					case PlataformPiece.COLOR_BEIGE:
						keyframe = Assets.plataformaBeigeRight;
						break;
					case PlataformPiece.COLOR_BLUE:
						keyframe = Assets.plataformaBlueRight;
						break;
					case PlataformPiece.COLOR_GRAY:
						keyframe = Assets.plataformaGrayRight;
						break;
					case PlataformPiece.COLOR_GREEN:
						keyframe = Assets.plataformaGreenRight;
						break;
					case PlataformPiece.COLOR_MULTICOLOR:
						keyframe = Assets.plataformaMulticolorRight;
						break;
					case PlataformPiece.COLOR_PINK:
						keyframe = Assets.plataformaPinkRight;
						break;

				}
			}

			batch.draw(keyframe, obj.position.x - Platform.DRAW_WIDTH_NORMAL / 2f, obj.position.y - Platform.DRAW_HEIGTH_NORMAL
							/ 2f, Platform.DRAW_WIDTH_NORMAL / 2f, Platform.DRAW_HEIGTH_NORMAL / 2f, Platform.DRAW_WIDTH_NORMAL,
					Platform.DRAW_HEIGTH_NORMAL, 1, 1, obj.angleDeg);

		}
	}

	private void renderCoins() {
		Iterator<Coin> i = world.coinArray.iterator();
		while (i.hasNext()) {
			Coin obj = i.next();

			batch.draw(Assets.coin, obj.position.x - Coin.DRAW_WIDTH / 2f, obj.position.y - Coin.DRAW_HEIGHT / 2f, Coin.DRAW_WIDTH,
					Coin.DRAW_HEIGHT);
		}

	}

	private void renderItems() {
		Iterator<Item> i = world.itemArray.iterator();
		while (i.hasNext()) {
			Item obj = i.next();

			TextureRegion keyframe = null;

			switch (obj.tipo) {
				case Item.TIPO_BUBBLE:
					keyframe = Assets.bubbleSmall;
					break;
				case Item.TIPO_JETPACK:
					keyframe = Assets.jetpackSmall;
					break;
				case Item.TIPO_GUN:
					keyframe = Assets.gun;
					break;

			}

			batch.draw(keyframe, obj.position.x - Item.DRAW_WIDTH / 2f, obj.position.y - Item.DRAW_HEIGHT / 2f, Item.DRAW_WIDTH, Item.DRAW_HEIGHT);

		}

	}

	private void renderEnemy() {
		Iterator<Enemy> i = world.enemyArray.iterator();
		while (i.hasNext()) {
			Enemy obj = i.next();

			TextureRegion keyframe = Assets.enemigo.getKeyFrame(obj.stateTime, true);

			batch.draw(keyframe, obj.position.x - Enemy.DRAW_WIDTH / 2f, obj.position.y - Enemy.DRAW_HEIGHT / 2f, Enemy.DRAW_WIDTH,
					Enemy.DRAW_HEIGHT);
		}

	}
}


