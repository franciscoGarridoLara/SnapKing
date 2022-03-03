package com.example.snapking.BaseDatos

import com.example.snapking.Wrapper.WrapperUsuarioPartida
import com.example.snapking.modelo.Usuario

interface IGetJugadorGanador {
    fun oncallBack(ganador:WrapperUsuarioPartida)
}