package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.entities.*;
import com.mojang.escape.gui.BasicSprite;
import com.mojang.escape.gui.Sprite;

public class SpiritWallBlock extends Block {
	public SpiritWallBlock() {
		setFloorTex(7);
		setCeilTex(7);
		setBlocksMotion(true);
		for (int i = 0; i < 6; i++) {
			double x = (getRandom().nextDouble() - 0.5);
			double y = (getRandom().nextDouble() - 0.7) * 0.3;
			double z = (getRandom().nextDouble() - 0.5);
			addSprite(new BasicSprite(x, y, z, 4 * 8 + 6 + getRandom().nextInt(2), Art.INSTANCE.getCol(0x202020)));
		}
	}

	public boolean blocks(Entity entity) {
		if (entity instanceof Bullet) return false;
		return super.blocks(entity);
	}
}
