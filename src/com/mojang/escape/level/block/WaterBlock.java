package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.entities.*;

public class WaterBlock extends Block {
	int steps = 0;

	public WaterBlock() {
		setBlocksMotion(true);
	}

	public void tick() {
		super.tick();
		steps--;
		if (steps <= 0) {
			setFloorTex(8 + getRandom().nextInt(3));
			setFloorCol(Art.INSTANCE.getCol(0x0000ff));
			steps = 16;
		}
	}

	public boolean blocks(Entity entity) {
		if (entity instanceof Player) {
			if (((Player) entity).getSelectedItem() == Item.Flippers) return false;
		}
		if (entity instanceof Bullet) return false;
		return getBlocksMotion();
	}

	public double getFloorHeight(Entity e) {
		return -0.5;
	}

	public double getWalkSpeed(Player player) {
		return 0.4;
	}

}
