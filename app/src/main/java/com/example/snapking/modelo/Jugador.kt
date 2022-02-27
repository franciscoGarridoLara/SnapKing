package com.example.snapking.modelo


data class Jugador(var id: String, var ready:Boolean, var punto:Int) {
    constructor() : this("", true,0) {
    }
}