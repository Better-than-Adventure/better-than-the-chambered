package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;

public class SwitchBlock extends SolidBlock {
	private boolean pressed = false;

	public SwitchBlock() {
		setTex(2);
	}

	public boolean use(Level level, Item item) {
		pressed = !pressed;
		if (pressed) setTex(3);
		else setTex(2);
		
		level.trigger(getId(), pressed);
		if (pressed)
			Sound.Companion.getClick1().play();
		else
			Sound.Companion.getClick2().play();

		return true;
	}
}
