package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Entity;

public class PressurePlateBlock extends Block {
	public boolean pressed = false;

	public PressurePlateBlock() {
		setFloorTex(2);
	}

	public void tick() {
		super.tick();
		double r = 0.2;
		boolean steppedOn = getLevel().containsBlockingNonFlyingEntity(getX() - r, getY() - r, getX() + r, getY() + r);
		if (steppedOn != pressed) {
			pressed = steppedOn;
			if (pressed) setFloorTex(3);
			else setFloorTex(2);

			getLevel().trigger(getId(), pressed);
			if (pressed)
				Sound.Companion.getClick1().play();
			else
				Sound.Companion.getClick2().play();
		}
	}

	public double getFloorHeight(Entity e) {
		if (pressed) return -0.02;
		else return 0.02;
	}
}
