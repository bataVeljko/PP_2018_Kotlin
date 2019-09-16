package com.example.veljko.projekat

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity() {

    companion object{
        var FILE_NAME = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //ako dobijemo flag iz poslednje aktivnosti, znaci da korisnik zeli da zatvori program
        if(intent.getBooleanExtra("EXIT", false)) finish()

        val btnNovi = findViewById<Button>(R.id.noviRaspored)
        val btnPostojeci = findViewById<Button>(R.id.postojeciRaspored)
        val db = Raspored(this)

        btnNovi.setOnClickListener {
            FILE_NAME = applicationContext.filesDir.path.toString() + "LastModifiedDate.txt"
            val file = File(FILE_NAME)
            if(!file.exists()){
                file.createNewFile()
                file.writeText("01.01.1970.", Charset.defaultCharset())
                Log.d("Tag", "Napravljen")
            }
            else {
                if (file.readText(Charset.defaultCharset()) == ""){
                    file.writeText("01.01.1970.", Charset.defaultCharset())
                    Log.d("Tag", "Prepravljen")
                }
                // Samo za vezbu trenutno, izbrisati kasnije
                else{
                    file.writeText("01.01.2020.", Charset.defaultCharset())

                }
                Log.d("Tag", "Postoji")
            }

            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        btnPostojeci.setOnClickListener {
            val intent = Intent(this@LoginActivity, FifthActivity::class.java)
            val lista = db.readData()
            intent.putParcelableArrayListExtra("filtriraniCasoviIzPocetneAktivnosti" ,ArrayList(lista))
            startActivity(intent)
        }
    }
}