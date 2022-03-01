package com.example.snapking.BaseDatos

import com.example.snapking.modelo.Usuario
import com.example.snapking.modelo.WrapperUsuario

interface IGetUsuarios {
    fun OnCallBack(usuarios:ArrayList<WrapperUsuario>)
}