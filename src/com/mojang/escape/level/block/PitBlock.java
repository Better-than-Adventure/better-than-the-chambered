package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.*;
import com.mojang.escape.gui.BasicSprite;

public class PitBlock extends Block {
	private boolean filled = false;

	public PitBlock() {
		setFloorTex(1);
		setBlocksMotion(true);
	}

	public void addEntity(Entity entity) {
		super.addEntity(entity);
		if (!filled && entity instanceof BoulderEntity) {
			entity.remove();
			filled = true;
			setBlocksMotion(false);
			addSprite(new BasicSprite(0, 0, 0, 8 + 2, BoulderEntity.COLOR));
			Sound.Companion.getThud().play();
		}
	}

	public boolean blocks(Entity entity) {
		if (entity instanceof BoulderEntity) return false;
		return getBlocksMotion();
	}
}
