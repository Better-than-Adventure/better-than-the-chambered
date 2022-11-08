package com.mojang.escape.menu

import com.mojang.escape.Game
import com.mojang.escape.gui.Bitmap

abstract class Menu {
    abstract fun render(target: Bitmap);

    abstract fun tick(game: Game, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean);
}