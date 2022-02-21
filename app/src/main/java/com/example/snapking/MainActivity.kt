package com.example.snapking

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.snapking.Firebase.User
import com.example.snapking.modelo.Modelo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.security.Principal


class MainActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var emailUsuario: EditText
    private lateinit var passUsuario: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnLoginGoogle: SignInButton
    private val RC_SIGN_IN: Int = 123
    private lateinit var modelo: Modelo


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instanciasFirebase()
        instanciasGoogle()
        instancias()
        acciones()
        //modelo.activityTransparent(this@Login)
    }

    override fun onStart() {
        super.onStart()
        /*
        if (mAuth.currentUser != null) {
            startActivity(Intent(this,PrincipalActivity::class.java))
            finish()
        }*/
    }

    private fun instanciasGoogle() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("400573421580-j01bf8u90qre7cbrevpg94qganlh7vla.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun instanciasFirebase() {
        mAuth = FirebaseAuth.getInstance() //Autenticacion de Firebase
    }

    private fun instancias() {
        emailUsuario = findViewById(R.id.userEd)
        passUsuario = findViewById(R.id.passwdEd)
        btnLogin = findViewById(R.id.btnLogin)
        btnLoginGoogle = findViewById(R.id.sign_in_button)
        modelo = Modelo()
    }

    private fun acciones() {
        btnLogin.setOnClickListener(this)
        btnLoginGoogle.setOnClickListener(this)
    }


    private fun signInFirebase(email: String, password: String) {
        // Validamos los datos introducidos
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, getString(R.string.email_introducir), Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, getString(R.string.pass_introducir), Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            passUsuario.error = getString(R.string.pass_longitud)
            return
        }
        Toast.makeText(applicationContext, getString(R.string.dialog_sesion), Toast.LENGTH_SHORT).show()
        //modelo.showDialog(this@MainActivity, getString(R.string.dialog_sesion))

        mAuth.signInWithEmailAndPassword(email, password).
        addOnCompleteListener(this) {task ->

            if(task.isSuccessful) {
                //modelo.hideDialog()
                    User.crearInstance(mAuth)
                startActivity(Intent(this@MainActivity, PrincipalActivity::class.java))
                finish()
            }
            else {
                //modelo.hideDialog()
                Toast.makeText(applicationContext,
                    getString(R.string.error_autenticacion_usuario), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInGoogle() {
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                //Inicio de sesion correcto, autenticamos con firebase
                val account= task.getResult(ApiException::class.java)

                firebaseAuthGooogle(account)

            } catch (e: ApiException) {
                Toast.makeText(this@MainActivity, getString(R.string.error_conexion), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthGooogle(account: GoogleSignInAccount) {
        Toast.makeText(this@MainActivity, getString(R.string.dialog_sesion), Toast.LENGTH_LONG).show()

        //modelo.showDialog(this@MainActivity, getString(R.string.dialog_sesion))

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->

            if(task.isSuccessful) {
                //modelo.hideDialog()
                    User.crearInstance(mAuth)
                startActivity(Intent(this@MainActivity, PrincipalActivity::class.java))
                finish()
            }
            else {
                //modelo.hideDialog()
                Toast.makeText(this@MainActivity, getString(R.string.error_login), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(p0: View?) {

        when (p0?.id) {

            R.id.btnLogin -> {

                if(modelo.compruebaConexion(applicationContext)) {
                    signInFirebase(emailUsuario.text.toString(), passUsuario.text.toString())
                }
                else {
                    Toast.makeText(this@MainActivity, getString(R.string.error_conexion), Toast.LENGTH_LONG).show()
                }
            }

            R.id.sign_in_button -> {

                if(modelo.compruebaConexion(applicationContext)) {
                    signInGoogle()
                }
                else {
                    Toast.makeText(this@MainActivity, getString(R.string.error_conexion), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}