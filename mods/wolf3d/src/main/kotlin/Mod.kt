import com.mojang.escape.Game
import com.mojang.escape.mods.IMod
import com.mojang.escape.entities.Item
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.mods.wolf3d.level.Wolf3DLevel
import com.mojang.escape.toTranslatable

class Mod: IMod {
    override val name: StringUnit = "gametype.wolf3d".toTranslatable()

    override fun getFirstLevel(game: Game): Level {
        return Wolf3DLevel.loadWolf3DLevel(game, Wolf3DLevel.gameMaps.levelHeaders.first())
    }

    override fun onNewGame(game: Game) {
        game.player!!.items[0] = Item.PowerGlove
        game.player!!.items[1] = Item.Pistol
        game.player!!.ammo = 20
    }
}