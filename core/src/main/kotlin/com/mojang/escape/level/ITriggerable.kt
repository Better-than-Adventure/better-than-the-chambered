package com.mojang.escape.level

import com.mojang.escape.entities.Entity

interface ITriggerable {
    val triggerId: Int
    var triggerState: Int
    
    fun onTrigger(level: Level, source: Entity?, state: Int)
}