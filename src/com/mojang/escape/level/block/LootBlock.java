package com.mojang.escape.level.block;

import com.mojang.escape.*;
import com.mojang.escape.entities.*;
import com.mojang.escape.gui.BasicSprite;
import com.mojang.escape.gui.Sprite;

public class LootBlock extends Block {
	private boolean taken = false;
	private Sprite sprite;

	public LootBlock() {
		sprite = new BasicSprite(0, 0, 0, 16 + 2, Art.INSTANCE.getCol(0xffff80));
		addSprite(sprite);
		setBlocksMotion(true);
	}

	public void addEntity(Entity entity) {
		super.addEntity(entity);
		if (!taken && entity instanceof Player) {
			sprite.setRemoved(true);
			taken = true;
			setBlocksMotion(false);
			((Player) entity).loot++;
			Sound.Companion.getPickup().play();
			
		}
	}

	public boolean blocks(Entity entity) {
		if (entity instanceof Player) return false;
		return getBlocksMotion();
	}
}
