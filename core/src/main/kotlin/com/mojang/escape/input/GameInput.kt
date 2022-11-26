package com.mojang.escape.input

import com.mojang.escape.lang.StringUnit
import com.mojang.escape.toTranslatable

enum class GameInput(val displayName: StringUnit, val inputs: HashSet<Input>, val continuous: Boolean) {
    FORWARD("input.game.forward".toTranslatable(), hashSetOf(Input.Keyboard.KEY_W, Input.Keyboard.KEY_UP, Input.Keyboard.KEY_KP_8), true),
    BACKWARD("input.game.backward".toTranslatable(), hashSetOf(Input.Keyboard.KEY_S, Input.Keyboard.KEY_DOWN, Input.Keyboard.KEY_KP_2), true),
    STRAFE_LEFT("input.game.strafeLeft".toTranslatable(), hashSetOf(Input.Keyboard.KEY_A), true),
    STRAFE_RIGHT("input.game.strafeRight".toTranslatable(), hashSetOf(Input.Keyboard.KEY_D), true),
    TURN_LEFT("input.game.turnLeft".toTranslatable(), hashSetOf(Input.Keyboard.KEY_Q, Input.Keyboard.KEY_LEFT, Input.Keyboard.KEY_KP_4), true),
    TURN_RIGHT("input.game.turnRight".toTranslatable(), hashSetOf(Input.Keyboard.KEY_E, Input.Keyboard.KEY_RIGHT, Input.Keyboard.KEY_KP_6), true),
    STRAFE_MOD("input.game.strafeMod".toTranslatable(), hashSetOf(Input.Keyboard.KEY_LEFT_SHIFT, Input.Keyboard.KEY_LEFT_CONTROL, Input.Keyboard.KEY_LEFT_ALT), true),
    USE("input.game.use".toTranslatable(), hashSetOf(Input.Keyboard.KEY_SPACE, Input.Mouse.MOUSE_1), false),
    PRIMARY("input.game.primary".toTranslatable(), hashSetOf(Input.Keyboard.KEY_SPACE, Input.Mouse.MOUSE_1), false),
    SECONDARY("input.game.secondary".toTranslatable(), hashSetOf(Input.Mouse.MOUSE_2), false),
    PAUSE("input.game.pause".toTranslatable(), hashSetOf(Input.Keyboard.KEY_ESCAPE), false),
    SLOT_1("input.game.slot.1".toTranslatable(), hashSetOf(Input.Keyboard.KEY_1), false),
    SLOT_2("input.game.slot.2".toTranslatable(), hashSetOf(Input.Keyboard.KEY_2), false),
    SLOT_3("input.game.slot.3".toTranslatable(), hashSetOf(Input.Keyboard.KEY_3), false),
    SLOT_4("input.game.slot.4".toTranslatable(), hashSetOf(Input.Keyboard.KEY_4), false),
    SLOT_5("input.game.slot.5".toTranslatable(), hashSetOf(Input.Keyboard.KEY_5), false),
    SLOT_6("input.game.slot.6".toTranslatable(), hashSetOf(Input.Keyboard.KEY_6), false),
    SLOT_7("input.game.slot.7".toTranslatable(), hashSetOf(Input.Keyboard.KEY_7), false),
    SLOT_8("input.game.slot.8".toTranslatable(), hashSetOf(Input.Keyboard.KEY_8), false),
    SLOT_LAST("input.game.slot.last".toTranslatable(), hashSetOf(), true),
    SLOT_NEXT("input.game.slot.next".toTranslatable(), hashSetOf(), true),
}

enum class MenuInput(val displayName: StringUnit, val inputs: HashSet<Input>) {
    UP("input.game.up".toTranslatable(), hashSetOf(Input.Keyboard.KEY_UP)),
    DOWN("input.game.down".toTranslatable(), hashSetOf(Input.Keyboard.KEY_DOWN)),
    LEFT("input.game.left".toTranslatable(), hashSetOf(Input.Keyboard.KEY_LEFT)),
    RIGHT("input.game.right".toTranslatable(), hashSetOf(Input.Keyboard.KEY_RIGHT)),
    CONFIRM("input.game.confirm".toTranslatable(), hashSetOf(Input.Keyboard.KEY_ENTER, Input.Keyboard.KEY_SPACE)),
    BACK("input.game.back".toTranslatable(), hashSetOf(Input.Keyboard.KEY_ESCAPE));
}