package com.mojang.escape.gui;

import java.util.Random;

import com.mojang.escape.*;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.block.Block;

public class Screen extends Bitmap {
	private static final int PANEL_HEIGHT = 29;

	private Bitmap testBitmap;
	private Bitmap3D viewport;

	public Screen(int width, int height) {
		super(width, height);

		viewport = new Bitmap3D(width, height - PANEL_HEIGHT);

		Random random = new Random();
		testBitmap = new Bitmap(64, 64);
		for (int i = 0; i < 64 * 64; i++) {
			testBitmap.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		}
	}

	int time = 0;

	public void render(Game game, boolean hasFocus) {
		if (game.level == null) {
			fill(0, 0, width, height, 0);
		} else {
			boolean itemUsed = game.player.getItemUseTime() > 0;
			Item item = game.player.getItems()[game.player.getSelectedSlot()];

			if (game.getPauseTime() > 0) {
				fill(0, 0, width, height, 0);
				String[] messages = { "Entering " + game.level.getName(), };
				for (int y = 0; y < messages.length; y++) {
					draw(messages[y], (width - messages[y].length() * 6) / 2, (viewport.height - messages.length * 8) / 2 + y * 8 + 1, 0x111111);
					draw(messages[y], (width - messages[y].length() * 6) / 2, (viewport.height - messages.length * 8) / 2 + y * 8, 0x555544);
				}
			} else {
				viewport.render(game);
				viewport.postProcess(game.level);

				Block block = game.level.getBlock((int) (game.player.getX() + 0.5), (int) (game.player.getZ() + 0.5));
				if (block.getMessages() != null && hasFocus) {
					for (int y = 0; y < block.getMessages().length; y++) {
						viewport.draw(block.getMessages()[y], (width - block.getMessages()[y].length() * 6) / 2, (viewport.height - block.getMessages().length * 8) / 2 + y * 8 + 1, 0x111111);
						viewport.draw(block.getMessages()[y], (width - block.getMessages()[y].length() * 6) / 2, (viewport.height - block.getMessages().length * 8) / 2 + y * 8, 0x555544);
					}
				}

				draw(viewport, 0, 0);
				int xx = (int) (game.player.getTurnBob() * 32);
				int yy = (int) (Math.sin(game.player.getBobPhase() * 0.4) * 1 * game.player.getBob() + game.player.getBob() * 2);

				if (itemUsed) xx = yy = 0;
				xx += width / 2;
				yy += height - PANEL_HEIGHT - 15 * 3;
				if (item != Item.None) {
					scaleDraw(Art.INSTANCE.getItems(), 3, xx, yy, 16 * item.getIcon() + 1, 16 + 1 + (itemUsed ? 16 : 0), 15, 15, Art.INSTANCE.getCol(item.getColor()));
				}

				if (game.player.getHurtTime() > 0 || game.player.getDead()) {
					double offs = 1.5 - game.player.getHurtTime() / 30.0;
					Random random = new Random(111);
					if (game.player.getDead()) offs = 0.5;
					for (int i = 0; i < pixels.length; i++) {
						double xp = ((i % width) - viewport.width / 2.0) / width * 2;
						double yp = ((i / width) - viewport.height / 2.0) / viewport.height * 2;

						if (random.nextDouble() + offs < Math.sqrt(xp * xp + yp * yp)) pixels[i] = (random.nextInt(5) / 4) * 0x550000;
					}
				}
			}

			draw(Art.INSTANCE.getPanel(), 0, height - PANEL_HEIGHT, 0, 0, width, PANEL_HEIGHT, Art.INSTANCE.getCol(0x707070));

			draw("å", 3, height - 26 + 0, 0x00ffff);
			draw("" + game.player.getKeys() + "/4", 10, height - 26 + 0, 0xffffff);
			draw("Ä", 3, height - 26 + 8, 0xffff00);
			draw("" + game.player.getLoot(), 10, height - 26 + 8, 0xffffff);
			draw("Å", 3, height - 26 + 16, 0xff0000);
			draw("" + game.player.getHealth(), 10, height - 26 + 16, 0xffffff);

			for (int i = 0; i < 8; i++) {
				Item slotItem = game.player.getItems()[i];
				if (slotItem != Item.None) {
					draw(Art.INSTANCE.getItems(), 30 + i * 16, height - PANEL_HEIGHT + 2, slotItem.getIcon() * 16, 0, 16, 16, Art.INSTANCE.getCol(slotItem.getColor()));
					if (slotItem == Item.Pistol) {
						String str = "" + game.player.getAmmo();
						draw(str, 30 + i * 16 + 17 - str.length() * 6, height - PANEL_HEIGHT + 1 + 10, 0x555555);
					}
					if (slotItem == Item.Potion) {
						String str = "" + game.player.getPotions();
						draw(str, 30 + i * 16 + 17 - str.length() * 6, height - PANEL_HEIGHT + 1 + 10, 0x555555);
					}
				}
			}

			draw(Art.INSTANCE.getItems(), 30 + game.player.getSelectedSlot() * 16, height - PANEL_HEIGHT + 2, 0, 48, 17, 17, Art.INSTANCE.getCol(0xffffff));

			draw(item.getItemName(), 26 + (8 * 16 - item.getItemName().length() * 4) / 2, height - 9, 0xffffff);
		}

		if (game.getMenu() != null) {
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = (pixels[i] & 0xfcfcfc) >> 2;
			}			
			game.getMenu().render(this);
		}

		if (!hasFocus) {
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = (pixels[i] & 0xfcfcfc) >> 2;
			}
			if (System.currentTimeMillis() / 450 % 2 != 0) {
				String msg = "Click to focus!";
				draw(msg, (width - msg.length() * 6) / 2, height / 3 + 4, 0xffffff);
			}
		}
	}
}