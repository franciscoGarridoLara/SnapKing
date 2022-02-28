package com.example.snapking.BaseDatos

import com.example.snapking.Wrapper.WrapperUsuarioLobby

interface IGetUsers {
    fun OnCallBack(lista:ArrayList<WrapperUsuarioLobby>)
}