package com.example.veljko.projekat

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import java.util.ArrayList

class SecondActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //liste predmeta
        val casovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzPrveAktivnosti")
        val ponovljeniMoguciPredmeti = arrayListOf<String>()
        for(cas in casovi)
            ponovljeniMoguciPredmeti.add(cas.getNaziv())
        val moguciPredmeti = ArrayList(ponovljeniMoguciPredmeti.distinct())
        val izabraniPredmeti = ArrayList<String>()

        //adapteri za liste koje se prikazuju korisniku
        val lvMoguci = findViewById<ListView>(R.id.spisakPredmeta)
        val adapterMoguci = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moguciPredmeti)
        adapterMoguci.sort { s1, s2 ->  s1.compareTo(s2)}
        lvMoguci.adapter = adapterMoguci

        val lvIzabrani = findViewById<View>(R.id.izabraniPredmeti) as ListView
        val adapterIzabrani = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, izabraniPredmeti)
        adapterIzabrani.sort { s1, s2 ->  s1.compareTo(s2)}
        lvIzabrani.adapter = adapterIzabrani

        //dugme za laksi izbor svih predmeta
        val btnSveIliNista = findViewById<Button>(R.id.izaberiSvePredmete)
        btnSveIliNista.setOnClickListener { _ ->
            if(btnSveIliNista.text == "Изабери све") {
                izabraniPredmeti.addAll(moguciPredmeti)
                lvIzabrani.adapter = adapterIzabrani
                moguciPredmeti.clear()
                lvMoguci.adapter = adapterMoguci
                btnSveIliNista.text = "Поништи све"
            }
            else{
                moguciPredmeti.addAll(izabraniPredmeti)
                lvMoguci.adapter = adapterMoguci
                izabraniPredmeti.clear()
                lvIzabrani.adapter = adapterIzabrani
                btnSveIliNista.text = "Изабери све"
            }
        }

        //izbor nekog predmeta
        lvMoguci.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            izabraniPredmeti.add(moguciPredmeti[position])
            lvIzabrani.adapter = adapterIzabrani
            Toast.makeText(this@SecondActivity, "Izabrali ste predmet: " + moguciPredmeti[position], Toast.LENGTH_SHORT).show()
            moguciPredmeti.remove(moguciPredmeti[position])
            lvMoguci.adapter = adapterMoguci

            //provera da nisu svi predmeti selektovani jedan po jedan
            if(moguciPredmeti.isEmpty())
                btnSveIliNista.text = "Поништи све"
        }

        //izbacivanje nekog predmeta
        lvIzabrani.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            moguciPredmeti.add(izabraniPredmeti[position])
            lvMoguci.adapter = adapterMoguci
            Toast.makeText(this@SecondActivity, "Izbacili ste predmet: " + izabraniPredmeti[position], Toast.LENGTH_SHORT).show()
            izabraniPredmeti.remove(izabraniPredmeti[position])
            lvIzabrani.adapter = adapterIzabrani

            //provera da nisu svi predmeti izbaceni jedan po jedan
            if(izabraniPredmeti.isEmpty())
                btnSveIliNista.text = "Изабери све"
        }

        //prelazak na sledecu stranu
        val dugme = findViewById<Button>(R.id.dugmeSecond)
        dugme.setOnClickListener({
            var filtriraniCasovi = casovi.toList()
            filtriraniCasovi = filtriraniCasovi.filter { cas -> izabraniPredmeti.contains(cas.getNaziv()) }

            val intent = Intent(this@SecondActivity, ThirdActivity::class.java)
            intent.putParcelableArrayListExtra("filtriraniCasoviIzDrugeAktivnosti" ,ArrayList(filtriraniCasovi))
            startActivity(intent)
            //finish()
        })
    }
}