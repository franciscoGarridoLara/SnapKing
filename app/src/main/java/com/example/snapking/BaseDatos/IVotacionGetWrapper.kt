package com.example.snapking.BaseDatos

import com.example.snapking.Wrapper.WrapperUsuarioPartida

interface IVotacionGetWrapper {
    fun OncallBack(lista:ArrayList<WrapperUsuarioPartida>)
}