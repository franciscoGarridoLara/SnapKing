package com.example.snapking.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Jugador(var id: String, var ready:Boolean, var punto:Int) {
    constructor() : this("", true,0) {
    }
}