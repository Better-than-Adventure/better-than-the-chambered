package com.mojang.escape.network

interface INetworkable {
    val objectId: Int
    val removed: Boolean
}