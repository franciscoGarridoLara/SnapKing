package com.example.snapking.BaseDatos

import com.example.snapking.modelo.Usuario

interface IGetUsuarios {
    fun OnCallBack(usuarios:ArrayList<Usuario>)
}