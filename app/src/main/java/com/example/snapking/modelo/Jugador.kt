package com.example.snapking.modelo


data class Jugador(var id: String, var ready:Boolean, var punto:Float,var etapa:Etapa) {
    constructor() : this("", true,0f,Etapa.LOBBY) {
    }
}