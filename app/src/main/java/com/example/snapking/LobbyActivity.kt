package com.example.snapking

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.snapking.databinding.ActivityLobbyBinding
import com.example.snapking.modelo.WrapperSala
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent

import android.content.SharedPreferences
import android.os.Handler

import android.preference.PreferenceManager
import androidx.core.content.ContextCompat
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.Firebase.User


class LobbyActivity : AppCompatActivity() {
    var wraperSala:WrapperSala?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recicle
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
         wraperSala=Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"),type)
        Log.d("-------------------activity lobby",wraperSala.toString())


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if(keyCode==event.keyCode)
            {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Salir de la sala")
                builder.setMessage("¿Quieres salir de la sala?")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                    //llamar a base de datos para eliminar la sala.
                    BaseDatos.getInstance()?.elminarJugadorSala(wraperSala!!.id, User.getInstancia()!!.printToken())

                    startActivity(Intent(this,PrincipalActivity::class.java))
                    finish()
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.no, Toast.LENGTH_SHORT).show()
                }

                builder.show()

            }

        }
        return super.onKeyDown(keyCode, event)
    }
}