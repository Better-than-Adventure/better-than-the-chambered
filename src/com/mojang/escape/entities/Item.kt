package com.mojang.escape.entities

enum class Item(val icon: Int, val color: Int, val itemName: String, val description: String) {
    None(       -1, 0xFFC363,   "",             ""),
    PowerGlove( 0,  0xFFC363,   "Power Glove",  "Smaaaash!!"),
    Pistol(     1,  0xEAEAEA,   "Pistol",       "Pew, pew, pew!"),
    Flippers(   2,  0x7CBBFF,   "Flippers",     "Splish splash!"),
    Cutters(    3,  0xCCCCCC,   "Cutters",      "Snip, snip!"),
    Skates(     4,  0xAE70FF,   "Skates",       "Sharp!"),
    Key(        5,  0xFF4040,   "Key",          "How did you get this?"),
    Potion(     6,  0x4aFF47,   "Potion",       "Healthy!");
}