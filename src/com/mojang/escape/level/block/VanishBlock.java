package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Item;
import com.mojang.escape.gui.RubbleSprite;
import com.mojang.escape.level.Level;

public class VanishBlock extends SolidBlock {
	private boolean gone = false;

	public VanishBlock() {
		setTex(1);
	}

	public boolean use(Level level, Item item) {
		if (gone) return false;

		gone = true;
		setBlocksMotion(false);
		setSolidRender(false);
		Sound.Companion.getCrumble().play();

		for (int i = 0; i < 32; i++) {
			RubbleSprite sprite = new RubbleSprite();
			sprite.setCol(getCol());
			addSprite(sprite);
		}

		return true;
	}
}
