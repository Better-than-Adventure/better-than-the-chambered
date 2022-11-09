package com.mojang.escape.level.block;

import com.mojang.escape.*;
import com.mojang.escape.entities.*;
import com.mojang.escape.gui.BasicSprite;
import com.mojang.escape.gui.Sprite;

public class LadderBlock extends Block {
	private static final int LADDER_COLOR = 0xDB8E53;
	public boolean wait;

	public LadderBlock(boolean down) {
		if (down) {
			setFloorTex(1);
			addSprite(new BasicSprite(0, 0, 0, 8 + 3, Art.INSTANCE.getCol(LADDER_COLOR)));
		} else {
			setCeilTex(1);
			addSprite(new BasicSprite(0, 0, 0, 8 + 4, Art.INSTANCE.getCol(LADDER_COLOR)));
		}
	}

	public void removeEntity(Entity entity) {
		super.removeEntity(entity);
		if (entity instanceof Player) {
			wait = false;
		}
	}

	public void addEntity(Entity entity) {
		super.addEntity(entity);

		if (!wait && entity instanceof Player) {
			getLevel().switchLevel(getId());
			Sound.Companion.getLadder().play();
		}
	}
}
