package mx.edu.ittepic.ladm_u4_tarea2_content_providers_kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.telecom.Call
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import javax.crypto.spec.DESKeySpec

class MainActivity : AppCompatActivity() {

    val yesPermissionReadCallsLogs = 100
    val yesPermissionMakeCall = 200
    var regCalls =ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE) , yesPermissionMakeCall)
        }

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG) , yesPermissionReadCallsLogs)
        }

        btnCall.setOnClickListener {
            val inteCall = Intent(Intent.ACTION_CALL)
            inteCall.data  = Uri.parse("tel: ${numberCall.text.toString()}")
            startActivity(inteCall)
        }


        btnnums.setOnClickListener {
            call_logs()
        }

    }//End on Create

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == yesPermissionMakeCall){
            Toast.makeText(this,"Permisos para rlizar llamadas fueron otorgados" , Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun call_logs() {
            var data=""
            var cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.TYPE)

        if(cursor!!.moveToFirst()) {
            var name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            var number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)

            do {
                data = "Nombre: ${cursor.getString(name)}\nTelefono: ${cursor.getString(number)}\nTipo: ${cursor.getString(type)}"
                regCalls.add(data)

            }while (cursor.moveToNext())

            lisContacts.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,regCalls)

        }else{
            data = "No tienes llamadas Recientes"
        }

        }//End method call_log


}//End Class
