package com.mygdx.game.Object;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Screens.Screens;

public class Player {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DEAD = 1;
	public int state;

	public final static float DRAW_WIDTH = .75f;
	public final static float DRAW_HEIGTH = .8f;

	public final static float WIDTH = .4f;
	public final static float HEIGTH = .6f;

	final float VELOCITY_JUMP = 10.5f;
	final float VELOCITY_X = 5;

	public final float DURATION_BUBBLE = 3;
	public float durationBubble;

	public final float DURATION_JETPACK = 3;
	public float durationJetPack;

	final public Vector2 position;
	public Vector2 velocity;
	public float angleDeg;

	public float stateTime;

	boolean didJump;
	public boolean isBubble;
	public boolean isJetPack;

	public Player(float x, float y) {
		position = new Vector2(x, y);
		velocity = new Vector2();

		stateTime = 0;
		state = STATE_NORMAL;
	}

	public void update(Body body, float delta, float acelX) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;

		velocity = body.getLinearVelocity();

		if (state == STATE_NORMAL) {

			if (didJump) {
				didJump = false;
				stateTime = 0;
				velocity.y = VELOCITY_JUMP;

			}

			velocity.x = acelX * VELOCITY_X;

			if (isBubble) {
				durationBubble += delta;
				if (durationBubble >= DURATION_BUBBLE) {
					durationBubble = 0;
					isBubble = false;
				}
			}

			if (isJetPack) {
				durationJetPack += delta;
				if (durationJetPack >= DURATION_JETPACK) {
					durationJetPack = 0;
					isJetPack = false;
				}
				velocity.y = VELOCITY_JUMP;
			}

		}
		else {
			body.setAngularVelocity(MathUtils.degRad * 360);
			velocity.x = 0;
		}

		body.setLinearVelocity(velocity);

		if (position.x >= Screens.WORLD_WIDTH) {
			position.x = 0;
			body.setTransform(position, 0);
		}
		else if (position.x <= 0) {
			position.x = Screens.WORLD_WIDTH;
			body.setTransform(position, 0);
		}

		angleDeg = body.getAngle() * MathUtils.radDeg;

		velocity = body.getLinearVelocity();
		stateTime += delta;

	}

	public void jump() {
		didJump = true;
	}

	public void hit() {
		if (state == STATE_NORMAL && !isBubble && !isJetPack) {
			state = STATE_DEAD;
			stateTime = 0;

		}
	}

	public void die() {
		if (state == STATE_NORMAL) {
			state = STATE_DEAD;
			stateTime = 0;
		}
	}

	public void setBubble() {
		if (state == STATE_NORMAL) {
			isBubble = true;
			durationBubble = 0;
		}
	}

	public void setJetPack() {
		if (state == STATE_NORMAL) {
			isJetPack = true;
			durationJetPack = 0;
		}
	}
}
