package com.mojang.escape.level.block;

import com.mojang.escape.*;
import com.mojang.escape.entities.Item;
import com.mojang.escape.gui.BasicSprite;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;

public class ChestBlock extends Block {
	private boolean open = false;
	private Sprite chestSprite;

	public ChestBlock() {
		setTex(1);
		setBlocksMotion(true);

		chestSprite = new BasicSprite(0, 0, 0, 8 * 2 + 0, Art.INSTANCE.getCol(0xffff00));
		addSprite(chestSprite);
	}

	public boolean use(Level level, Item item) {
		if (open) return false;

		chestSprite.setTex(chestSprite.getTex() + 1);
		open = true;

		level.getLoot(getId());
		Sound.Companion.getTreasure().play();

		return true;
	}
}
