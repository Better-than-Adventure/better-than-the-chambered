import com.mojang.escape.Game
import com.mojang.escape.mods.IMod
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.mods.prelude.level.PNGLevel
import com.mojang.escape.mods.prelude.level.provider.PNGLevelProvider
import com.mojang.escape.toTranslatable

class Mod: IMod {
    override val name: StringUnit = "gametype.prelude".toTranslatable()
    override val levelProvider = PNGLevelProvider()

    override fun onNewGame(game: Game) {
        // Do nothing
    }
}