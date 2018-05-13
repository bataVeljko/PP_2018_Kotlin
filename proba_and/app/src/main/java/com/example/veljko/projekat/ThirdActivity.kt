package com.example.veljko.projekat

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val casovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzDrugeAktivnosti")

        val ponovljenjeGrupe = arrayListOf<String>()
        for(cas in casovi)
            ponovljenjeGrupe.add(cas.grupa)
        val grupe = ponovljenjeGrupe.distinct()
        //pravim niz grupa, da bih mogao da posaljem podatke u sledecu aktivnost
        val nizGrupa = arrayListOf<String>()
        for(g in grupe)
            nizGrupa.add(g)

        //promenljive za odredjivanje lokacija
        //indeksi sluze da bi se moglo vrsiti i deselektovanje
        val lokacije = arrayListOf("Trg", "Jagiceva", "N")
        val indeksiLokacija = ArrayList<Int>(lokacije.size)
        val izabraneLokacije = arrayListOf<String>()

        val lvIzborGrupa = findViewById<ListView>(R.id.spisakGrupa)
        val grupeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grupe)
        lvIzborGrupa.adapter = grupeAdapter

        val lvIzborLokacija = findViewById<ListView>(R.id.spisakLokacija)
        val lokacijeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lokacije)
        lvIzborLokacija.adapter = lokacijeAdapter

        var izabranaGrupa = ""
        lvIzborGrupa.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            izabranaGrupa = grupe[position]
            Toast.makeText(this@ThirdActivity, "Izabrali ste grupu: " + grupe[position], Toast.LENGTH_SHORT).show()
        }

        lvIzborLokacija.onItemClickListener = OnItemClickListener { _, view, position, _ ->
            if (position in indeksiLokacija) {
                izabraneLokacije.remove(lokacije[position])
                view.setBackgroundColor(Color.TRANSPARENT)
                indeksiLokacija.remove(position)
            }
            else {
                izabraneLokacije.add(lokacije[position])
                view.setBackgroundColor(Color.GREEN)
                indeksiLokacija.add(position)
            }
        }

        val dugme = findViewById<Button>(R.id.dugmeThird)
        dugme.setOnClickListener({
            var filtriraniCasovi = casovi.toList()
            val pomocniFiltriraniCasoviZaLokacije = mutableListOf<Predmet>()

            if(izabraneLokacije.contains("Jagiceva"))
                pomocniFiltriraniCasoviZaLokacije.addAll(filtriraniCasovi.filter { cas -> cas.getUcionica().startsWith("jag") })
            if(izabraneLokacije.contains("N"))
                pomocniFiltriraniCasoviZaLokacije.addAll(filtriraniCasovi.filter { cas -> cas.getUcionica().startsWith("n") })
            if(izabraneLokacije.contains("Trg"))
                pomocniFiltriraniCasoviZaLokacije.addAll(filtriraniCasovi.filter { cas -> (!cas.getUcionica().startsWith("jag") && !cas.getUcionica().startsWith("n")) })
            //ako nije izabrana nijedna lokacija, sve ce biti uzete u obzir
            if(!izabraneLokacije.isEmpty())
                filtriraniCasovi = pomocniFiltriraniCasoviZaLokacije

            if(izabranaGrupa != ""){
                val intent = Intent(this@ThirdActivity, FourthActivity::class.java)
                intent.putParcelableArrayListExtra("filtriraniCasoviIzTreceAktivnosti" , ArrayList(filtriraniCasovi))
                intent.putExtra("imeGrupe", izabranaGrupa)
                intent.putExtra("grupe", nizGrupa)
                startActivity(intent)
                //finish()
            }
            else
                Toast.makeText(this@ThirdActivity, "Niste izabrali nijednu grupu", Toast.LENGTH_SHORT).show()
        })
    }
}