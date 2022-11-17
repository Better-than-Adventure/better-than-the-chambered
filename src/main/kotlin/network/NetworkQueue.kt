package com.mojang.escape.network

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashSet
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible

object NetworkQueue {
    class Networked<T: Any>(val propertyId: Int, private var value: T) {
        operator fun getValue(thisRef: INetworkable, property: KProperty<*>): T {
            return value
        }
        operator fun setValue(thisRef: INetworkable, property: KProperty<*>, value: T) {
            if (this.value != value) {
                this.value = value
                val propertyId = ((property as KProperty1<INetworkable,*>).apply { isAccessible = true }.getDelegate(thisRef) as Networked<*>).propertyId

                if (updates[thisRef.objectId] == null) updates[thisRef.objectId] = hashMapOf()
                updates[thisRef.objectId]!![propertyId] = value
                queue += Pair(thisRef.objectId, propertyId)
            }
        }
    }
    class Remover {
        private var removed = false
        operator fun getValue(thisRef: INetworkable, property: KProperty<*>): Boolean {
            return removed
        }
        operator fun setValue(thisRef: INetworkable, property: KProperty<*>, value: Boolean) {
            removed = value
            if (removed) {
                networkables.remove(thisRef.objectId)
            }
        }
    }
    private val updates: HashMap<Int, HashMap<Int, Any>> = HashMap()
    private val queue = linkedSetOf<Pair<Int, Int>>()
    private val networkables = hashMapOf<Int, INetworkable>()

    private var nextObjectId = 0
    fun getObjectId(networkable: INetworkable): Int {
        networkables[nextObjectId] = networkable
        return nextObjectId++
    }
}