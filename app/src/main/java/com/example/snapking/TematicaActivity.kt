package com.example.snapking

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.snapking.BaseDatos.*
import com.example.snapking.Firebase.User
import com.example.snapking.databinding.ActivityTematicaBinding
import com.example.snapking.modelo.Ronda
import com.example.snapking.modelo.Tematica
import com.example.snapking.modelo.WrapperSala
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TematicaActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityTematicaBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var outputDirectory: File
    private  var savedUri:Uri? =null
    var numeroronda=0
    private var wraperSala:WrapperSala?=null
    var ronda : Ronda? = null
    var fotoLocal:String?=null
    val PHOTO_EXTENSION=".jpg"
    var counter = object : CountDownTimer(60000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            Log.d("TEMATICA ACTIVITY","segundos restantes:" + millisUntilFinished / 1000)

            var segundos = (millisUntilFinished/1000).toInt()

            BaseDatos.getInstance()!!.actualizarTiempo(wraperSala!!.id,segundos)

            viewBinding.tvTiempo.setText(segundos.toString())
        }

        override fun onFinish() {
            Log.d("TEMATICA ACTIVITY", "CountDown finalizado!")
            viewBinding.tvTiempo.setText("TIEMPO!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_tematica)

        outputDirectory = getOutputDirectory(this)

        viewBinding = ActivityTematicaBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        Log.d("TEMATICA ACTIVITY","INICIANDO TEMATICA ACTIVITY")

        //cerrar la actividad anterior.
        LobbyActivity.activityActual!!.finish()

        cogerWrapperSala()
        crearRonda()
        pedirPermisos()
        setListeners()
        inicializarInterfaz()
        iniciarJuego()


        cameraExecutor = Executors.newSingleThreadExecutor()


    }
    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }

    private fun pasarActividad(){
        var intent=Intent(applicationContext,VotacionActivity::class.java)
        intent.putExtra("wrapersala",Gson().toJson(wraperSala) )
        startActivity(intent)
        finish()
    }
    private fun iniciarJuego() {
        inciarCountDown(true)
        comprobarStatusPartida()
    }

    private fun comprobarStatusPartida() {

        val postListener = object : ValueEventListener {
            var escuchar = true
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                BaseDatos.getInstance()!!.comprobarStatusSala(wraperSala!!.id, object : IGetStatusSala{
                    override fun OnCallBack(status: Boolean) {
                        Log.d("TEMATICA ACTIVITY","Status sala: " + status.toString())
                        if(!status)
                        {
                            var intent = Intent(this@TematicaActivity,PrincipalActivity::class.java)
                            startActivity(intent)
                            finish()
                            escuchar = false
                        }

                    }
                })
                if(!escuchar){
                    Log.d("TEMATICA ACTIVITY", "STATUS = FALSE, SALIENDO A PRINCIPAL.")
                    BaseDatos.getInstance()!!.reference.child("salas").removeEventListener(this)
                    var intent = Intent(this@TematicaActivity,PrincipalActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Mandar a la principal activity.
                Log.w("ACTIVITY LOBBY", "loadPost:onCancelled", databaseError.toException())
                var intent = Intent(this@TematicaActivity,PrincipalActivity::class.java)
                startActivity(intent)
                finish()
            }


        }

        BaseDatos.getInstance()!!.reference.child("salas").addValueEventListener(postListener)
    }

    private fun inciarCountDown(encender : Boolean) {
        if (wraperSala!!.sala.anfitrion.equals(User.getInstancia()!!.printToken().toString())) {
            if(encender)
                counter.start()
            else
                counter.cancel()
        }else
        {
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    BaseDatos.getInstance()!!.getSegundos(wraperSala!!.id, object : IGetSegundos{
                        override fun OnCallBack(segundos: Int) {
                            Log.d("TEMATICA ACTIVITY", "Segundos desde el servidor.")
                            if(segundos > 0){
                                viewBinding.tvTiempo.setText(segundos.toString())
                            }else{
                                viewBinding.tvTiempo.setText("TIEMPO!")
                            }
                        }
                    })
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    //Mandar a la principal activity.
                    Log.w("ACTIVITY LOBBY", "loadPost:onCancelled", databaseError.toException())
                    var intent = Intent(this@TematicaActivity,PrincipalActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }

            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).child("ronda").child("tiempo").addValueEventListener(postListener)
        }

        }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if(keyCode==event.keyCode)
            {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Salir de la partida")
                builder.setMessage("¿Quieres salir de la partida?")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                    //llamar a base de datos para eliminar la sala.
                    try {
                        BaseDatos.getInstance()?.elminarJugadorSala(wraperSala!!.id, User.getInstancia()!!.printToken())
                    } catch (e: NullPointerException) {
                    }
                    inciarCountDown(false)

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



    private fun crearRonda() {
        if(wraperSala!!.sala?.anfitrion.equals(User.getInstancia()!!.printToken())) {
            //Si el usuario es anfitrion entonces crea una ronda y la inserta en la base de datos.
            BaseDatos.getInstance()!!.crearRonda(wraperSala!!.id, object : IGetRonda{
                override fun OnCallBack(rondaDB: Ronda) {
                    Log.d(TAG, rondaDB.toString())
                    ronda = rondaDB

                    BaseDatos.getInstance()!!.getTematicaById(ronda!!.id_tematica, object : IGetTematica{
                        override fun OnCallBack(tematica: Tematica) {
                            setTematica(tematica)
                        }
                    })

                }

            })


        }else{
//            while(ronda == null){
//                BaseDatos.getInstance()!!.getRondaByIdSala(wraperSala!!.id, object : IGetRonda{
//                    override fun OnCallBack(rondaDB: Ronda) {
//                        ronda = rondaDB
//                    }
//                })
//            }
        }

    }

    private fun setTematica(tematica : Tematica){
        viewBinding.tvTematica.setText(tematica.nombre)
    }

    private fun cogerWrapperSala(){
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
        wraperSala= Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"),type)
        Log.d("TEMATICA ACTIVITY", wraperSala.toString())
    }


    private fun inicializarInterfaz() {


        viewBinding.btnBack.visibility = View.INVISIBLE
        viewBinding.btnAcept.visibility = View.INVISIBLE
    }

    private fun setListeners() {
        viewBinding.btnPhoto.setOnClickListener {
            takePhoto()

            viewBinding.btnBack.visibility = View.VISIBLE
            viewBinding.btnPhoto.visibility = View.INVISIBLE
            viewBinding.btnAcept.visibility = View.VISIBLE
        }



        viewBinding.btnBack.setOnClickListener {
            onBackPressed()
            viewBinding.btnBack.visibility = View.INVISIBLE
            viewBinding.btnPhoto.visibility= View.VISIBLE
            viewBinding.btnAcept.visibility = View.INVISIBLE}

        viewBinding.btnAcept.setOnClickListener {
            viewBinding.btnPhoto.visibility= View.VISIBLE
            viewBinding.btnAcept.visibility = View.INVISIBLE
            viewBinding.btnBack.visibility = View.INVISIBLE
            pasarActividad()


        }


    }

    private fun pedirPermisos() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }


    public fun goBack() {


    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {

            put(MediaStore.MediaColumns.DISPLAY_NAME, name)

            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
               Log.d("-------------dsd", put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SnapKingPhotos").toString())
            }
        }
        val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(photoFile)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    BaseDatos.getInstance()?.subirfoto(savedUri!!,wraperSala!!.id,User.getInstancia()!!.printToken(),numeroronda, object : IGetSuccessSubirFoto{
                        override fun OnCallBack(url: String) {
                            switchFragment(url)
                        }

                    })

                    Log.d(TAG, msg)



                }
            }
        )
    }
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(baseFolder, User.getInstancia()!!.printToken()+extension)

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }
    private fun switchFragment(url : String){

        var fragment: Fragment?= null

        fragment = ImageFragment(url)


        fragmentTransaction= supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.cameraLayout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()








    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,

                ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}