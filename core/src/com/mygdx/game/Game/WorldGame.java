package com.mygdx.game.Game;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.game.Settings;
import com.mygdx.game.Object.Enemy;
import com.mygdx.game.Object.Item;
import com.mygdx.game.Object.Coin;
import com.mygdx.game.Object.Player;
import com.mygdx.game.Object.Platform;
import com.mygdx.game.Object.PlataformPiece;
import com.mygdx.game.Screens.Screens;

public class WorldGame {
	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	final public static int STATE_RUNNING = 0;
	final public static int STATE_GAMEOVER = 1;
	int state;

	public World world;

	Player player;
	private Array<Body> bodyArray;
	Array<PlataformPiece> platformArray;
	Array<Platform> platformPiecesArray;
	Array<Coin> coinArray;
	Array<Enemy> enemyArray;
	Array<Item> itemArray;

	public int coins;
	public int maxDistance;
	float gameWorldHeight;

	public WorldGame() {
		world = new World(new Vector2(0, -9.8f), true);
		world.setContactListener(new Collision());

		bodyArray = new Array<Body>();
		platformArray = new Array<PlataformPiece>();
		platformPiecesArray = new Array<Platform>();
		coinArray = new Array<Coin>();
		enemyArray = new Array<Enemy>();
		itemArray = new Array<Item>();

		state = STATE_RUNNING;

		createFloor();
		createPlayer();

		gameWorldHeight = player.position.y;
		createNextPart();
	}

	private void createNextPart() {
		float y = gameWorldHeight + 2;

		for (int i = 0; gameWorldHeight < (y + 10); i++) {
			gameWorldHeight = y + (i * 2);

			createPlatform(gameWorldHeight);
			createPlatform(gameWorldHeight);

			if (MathUtils.random(100) < 5)
				Coin.createMoneda(world, coinArray, gameWorldHeight);

			if (MathUtils.random(20) < 5)
				Coin.createUnaMoneda(world, coinArray, gameWorldHeight + .5f);

			if (MathUtils.random(20) < 5)
				createEnemy(gameWorldHeight + .5f);

			if (MathUtils.random(50) < 5)
				createItem(gameWorldHeight + .5f);
		}

	}

	/**
	 * The floor only appears 1 time, at the beginning of the game
	 */
	private void createFloor() {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		Body body = world.createBody(bd);

		EdgeShape shape = new EdgeShape();
		shape.set(0, 0, Screens.WORLD_WIDTH, 0);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;

		body.createFixture(fixutre);
		body.setUserData("piso");

		shape.dispose();

	}

	private void createPlayer() {
		player = new Player(2.4f, .5f);

		BodyDef bd = new BodyDef();
		bd.position.set(player.position.x, player.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = world.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Player.WIDTH / 2f, Player.HEIGTH / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 10;
		fixture.friction = 0;
		fixture.restitution = 0;

		body.createFixture(fixture);
		body.setUserData(player);
		body.setFixedRotation(true);

		shape.dispose();
	}

	private void createPlatform(float y) {

		PlataformPiece oPlat = Pools.obtain(PlataformPiece.class);
		oPlat.init(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(1));

		BodyDef bd = new BodyDef();
		bd.position.set(oPlat.position.x, oPlat.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = world.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(PlataformPiece.WIDTH_NORMAL / 2f, PlataformPiece.HEIGTH_NORMAL / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;

		body.createFixture(fixture);
		body.setUserData(oPlat);
		platformArray.add(oPlat);

		shape.dispose();

	}

	/**
	 * The breakable platform is 2 frames
	 * @param
	 */
	private void createPlatformPiece(PlataformPiece oPlat) {
		createPlatformPieceByType(oPlat, Platform.TYPE_LEFT);
		createPlatformPieceByType(oPlat, Platform.TYPE_RIGHT);

	}

	private void createPlatformPieceByType(PlataformPiece platform, int type) {
		Platform piece;
		float x;
		float angularVelocity = 100;

		if (type == Platform.TYPE_LEFT) {
			x = platform.position.x - Platform.WIDTH_NORMAL / 2f;
			angularVelocity *= -1;
		}
		else {
			x = platform.position.x + Platform.WIDTH_NORMAL / 2f;
		}

		piece = Pools.obtain(Platform.class);
		piece.init(x, platform.position.y, type, platform.color);

		BodyDef bd = new BodyDef();
		bd.position.set(piece.position.x, piece.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = world.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Platform.WIDTH_NORMAL / 2f, Platform.HEIGTH_NORMAL / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;

		body.createFixture(fixture);
		body.setUserData(piece);
		body.setAngularVelocity(MathUtils.degRad * angularVelocity);
		platformPiecesArray.add(piece);

		shape.dispose();
	}

	private void createEnemy(float y) {
		Enemy oEn = Pools.obtain(Enemy.class);
		oEn.init(MathUtils.random(Screens.WORLD_WIDTH), y);

		BodyDef bd = new BodyDef();
		bd.position.set(oEn.position.x, oEn.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = world.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;

		body.createFixture(fixture);
		body.setUserData(oEn);
		body.setGravityScale(0);

		float velocidad = MathUtils.random(1f, Enemy.VELOCIDAD_X);

		if (MathUtils.randomBoolean())
			body.setLinearVelocity(velocidad, 0);
		else
			body.setLinearVelocity(-velocidad, 0);
		enemyArray.add(oEn);

		shape.dispose();
	}

	private void createItem(float y) {
		Item item = Pools.obtain(Item.class);
		item.init(MathUtils.random(Screens.WORLD_WIDTH), y);

		BodyDef bd = new BodyDef();
		bd.position.set(item.position.x, item.position.y);
		bd.type = BodyType.StaticBody;
		Body oBody = world.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Item.WIDTH / 2f, Item.HEIGHT / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		oBody.createFixture(fixture);
		oBody.setUserData(item);
		shape.dispose();
		itemArray.add(item);
	}

	public void update(float delta, float acelX) {
		world.step(delta, 8, 4);

		deleteObjects();

		if (player.position.y + 10 > gameWorldHeight) {
			createNextPart();
		}

		world.getBodies(bodyArray);
		Iterator<Body> i = bodyArray.iterator();

		while (i.hasNext()) {
			Body body = i.next();
			if (body.getUserData() instanceof Player) {
				updatePlayer(body, delta, acelX);
			}
			else if (body.getUserData() instanceof PlataformPiece) {
				updatePlatform(body, delta);
			}
			else if (body.getUserData() instanceof Platform) {
				updatePlatformPiece(body, delta);
			}
			else if (body.getUserData() instanceof Coin) {
				updateCoin(body, delta);
			}
			else if (body.getUserData() instanceof Enemy) {
				updateEnemy(body, delta);
			}
			else if (body.getUserData() instanceof Item) {
				updateItem(body, delta);
			}

		}

		if (maxDistance < (player.position.y * 10)) {
			maxDistance = (int) (player.position.y * 10);
		}

		if (player.state == Player.STATE_NORMAL && maxDistance - (5.5f * 10) > (player.position.y * 10)) {
			player.die();
		}
		if (player.state == Player.STATE_DEAD && maxDistance - (25 * 10) > (player.position.y * 10)) {
			state = STATE_GAMEOVER;
		}

	}

	private void deleteObjects() {
		world.getBodies(bodyArray);
		Iterator<Body> i = bodyArray.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (!world.isLocked()) {

				if (body.getUserData() instanceof PlataformPiece) {
					PlataformPiece oPlat = (PlataformPiece) body.getUserData();
					if (oPlat.state == PlataformPiece.STATE_DESTROY) {
						platformArray.removeValue(oPlat, true);
						world.destroyBody(body);
						if (oPlat.tipo == PlataformPiece.TIPO_ROMPIBLE)
							createPlatformPiece(oPlat);
						Pools.free(oPlat);
					}
				}
				else if (body.getUserData() instanceof Coin) {
					Coin coin = (Coin) body.getUserData();
					if (coin.state == Coin.STATE_TAKEN) {
						coinArray.removeValue(coin, true);
						world.destroyBody(body);
						Pools.free(coin);
					}
				}
				else if (body.getUserData() instanceof Platform) {
					Platform oPiez = (Platform) body.getUserData();
					if (oPiez.state == Platform.STATE_DESTROY) {
						platformPiecesArray.removeValue(oPiez, true);
						world.destroyBody(body);
						Pools.free(oPiez);
					}
				}
				else if (body.getUserData() instanceof Enemy) {
					Enemy oEnemy = (Enemy) body.getUserData();
					if (oEnemy.state == Enemy.STATE_DEAD) {
						enemyArray.removeValue(oEnemy, true);
						world.destroyBody(body);
						Pools.free(oEnemy);
					}
				}
				else if (body.getUserData() instanceof Item) {
					Item oItem = (Item) body.getUserData();
					if (oItem.state == Item.STATE_TAKEN) {
						itemArray.removeValue(oItem, true);
						world.destroyBody(body);
						Pools.free(oItem);
					}
				}
				else if (body.getUserData().equals("piso")) {
					if (player.position.y - 5.5f > body.getPosition().y || player.state == Player.STATE_DEAD) {
						world.destroyBody(body);
					}
				}
			}
		}
	}

	private void updatePlayer(Body body, float delta, float acelX) {
		player.update(body, delta, acelX);
	}

	private void updatePlatform(Body body, float delta) {
		PlataformPiece obj = (PlataformPiece) body.getUserData();
		obj.update(delta);
		if (player.position.y - 5.5f > obj.position.y) {
			obj.setDestroy();
		}
	}

	private void updatePlatformPiece(Body body, float delta) {
		Platform obj = (Platform) body.getUserData();
		obj.update(delta, body);
		if (player.position.y - 5.5f > obj.position.y) {
			obj.setDestroy();
		}

	}

	private void updateCoin(Body body, float delta) {
		Coin obj = (Coin) body.getUserData();
		obj.update(delta);
		if (player.position.y - 5.5f > obj.position.y) {
			obj.take();
		}

	}

	private void updateEnemy(Body body, float delta) {
		Enemy obj = (Enemy) body.getUserData();
		obj.update(body, delta);
		if (player.position.y - 5.5f > obj.position.y) {
			obj.hit();
		}

	}

	private void updateItem(Body body, float delta) {
		Item obj = (Item) body.getUserData();
		obj.update(delta);
		if (player.position.y - 5.5f > obj.position.y) {
			obj.take();
		}
	}

	class Collision implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Player)
				beginContactPersonaje(a, b);
			else if (b.getBody().getUserData() instanceof Player)
				beginContactPersonaje(b, a);
		}

		private void beginContactPersonaje(Fixture fixPersonaje, Fixture fixOtraCosa) {
			Object otraCosa = fixOtraCosa.getBody().getUserData();

			if (otraCosa.equals("piso")) {
				player.jump();

				if (player.state == Player.STATE_DEAD) {
					state = STATE_GAMEOVER;
				}
			}
			else if (otraCosa instanceof PlataformPiece) {
				PlataformPiece obj = (PlataformPiece) otraCosa;

				if (player.velocity.y <= 0) {
					player.jump();
					if (obj.tipo == PlataformPiece.TIPO_ROMPIBLE) {
						obj.setDestroy();
					}
				}

			}
			else if (otraCosa instanceof Coin) {
				Coin obj = (Coin) otraCosa;
				obj.take();
				coins++;
				player.jump();
			}
			else if (otraCosa instanceof Enemy) {
				player.hit();
			}
			else if (otraCosa instanceof Item) {
				Item obj = (Item) otraCosa;
				obj.take();

				switch (obj.tipo) {
					case Item.TIPO_BUBBLE:
						player.setBubble();
						break;
					case Item.TIPO_JETPACK:
						player.setJetPack();
						break;
					case Item.TIPO_GUN:
						Settings.numBullets += 10;
						break;

				}

			}

		}

		@Override
		public void endContact(Contact contact) {

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Player)
				preSolveHero(a, b, contact);
			else if (b.getBody().getUserData() instanceof Player)
				preSolveHero(b, a, contact);

		}

		private void preSolveHero(Fixture fixPersonaje, Fixture otraCosa, Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa instanceof PlataformPiece) {
				// Si va para arriba atraviesa la plataforma

				PlataformPiece obj = (PlataformPiece) oOtraCosa;

				float ponyY = fixPersonaje.getBody().getPosition().y - .30f;
				float pisY = obj.position.y + PlataformPiece.HEIGTH_NORMAL / 2f;

				if (ponyY < pisY)
					contact.setEnabled(false);

				if (obj.tipo == PlataformPiece.TIPO_NORMAL && player.state == Player.STATE_DEAD) {
					contact.setEnabled(false);
				}

			}

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub

		}

	}

}
