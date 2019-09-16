package com.example.veljko.projekat

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import java.sql.SQLException
import android.os.Build
import android.view.View.GONE

class SixthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sixth)

        val casovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzPeteAktivnosti")
        val imePrethodneAktivnosti = intent.getStringExtra("imeAktivnosti")
        val db = Raspored(this)
        val btnSacuvaj = findViewById<Button>(R.id.sacuvaj)
        val btnPocetna = findViewById<Button>(R.id.pocetna)
        val btnZatvori = findViewById<Button>(R.id.zatvori)

        val adUpozorenje: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(this)
        }

        adUpozorenje.setTitle("Нов распоред")
                .setMessage("Да ли сте сигурни да желите заменити постојећи распоред?")
                .setPositiveButton(android.R.string.yes) { _, _ ->


                    val upis = db.writableDatabase
                    try {
                        upis.execSQL("""DELETE FROM ${Raspored.TABLE_NAME}""")
                    } catch (e: SQLException){
                        e.printStackTrace()
                    }

                    for(cas in casovi)
                        db.insertData(cas)
                    Toast.makeText(this, "Uspesno sacuvano", Toast.LENGTH_LONG).show()
                    btnSacuvaj.visibility = GONE
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                    Toast.makeText(this, "Uspesno otkazano", Toast.LENGTH_LONG).show()
                }
                .setIcon(android.R.drawable.ic_dialog_alert)

        //ako smo pristupili aktivnosti iz login, znaci da korisnik zeli da pogleda zapamcene podatke, nema potrebe da cuva nista
        if(imePrethodneAktivnosti == "Login")
            btnSacuvaj.visibility = GONE
        else
            adUpozorenje.show()

        btnSacuvaj.setOnClickListener {
            adUpozorenje.show()
        }

        btnPocetna.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        btnZatvori.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("EXIT", true)
            startActivity(intent)
        }
    }
}
