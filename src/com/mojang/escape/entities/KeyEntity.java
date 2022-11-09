package com.mojang.escape.entities;

import com.mojang.escape.*;
import com.mojang.escape.gui.BasicSprite;
import com.mojang.escape.gui.Sprite;

public class KeyEntity extends Entity {
	public static final int COLOR = Art.INSTANCE.getCol(0x00ffff);
	private Sprite sprite;
	private double y, ya;

	public KeyEntity(double x, double z) {
		this.x = x;
		this.z = z;
		y = 0.5;
		ya = 0.025;
		sprite = new BasicSprite(0, 0, 0, 16 + 3, COLOR);
		sprites.add(sprite);
	}

	public void tick() {
		move();
		y += ya;
		if (y < 0) y = 0;
		ya -= 0.005;
		sprite.setY(y);
	}

	protected void collide(Entity entity) {
		if (entity instanceof Player) {
			Sound.Companion.getKey().play();
			((Player) entity).keys++;
			remove();
		}
	}
}
