package com.example.veljko.projekat

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList

private const val RESULT = 12345

class FourthActivity : AppCompatActivity() {

    private var dobijeniCasovi = arrayListOf<Predmet>()
    private val izabraniCasovi = arrayListOf<Predmet>()
    private var grupe = arrayListOf<String>()
    private var odabranaGrupa = String()
    private var grupaIzTmpa = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)
        //radi bolje preglednosti
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        dobijeniCasovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzTreceAktivnosti")
        grupe = intent.getStringArrayListExtra("grupe")
        odabranaGrupa = intent.getStringExtra("imeGrupe")

        val bGrupa = findViewById<Button>(R.id.drugaGrupa)
        bGrupa.setOnClickListener({
            val intent = Intent(this@FourthActivity, TmpActivity::class.java)
            intent.putExtra("grupe" ,grupe)
            startActivityForResult(intent, RESULT)
        })

        val dugme = findViewById<Button>(R.id.dugmeFourth)
        dugme.setOnClickListener({
            val intent = Intent(this@FourthActivity, FifthActivity::class.java)
            intent.putParcelableArrayListExtra("filtriraniCasoviIzCetvrteAktivnosti" , ArrayList(izabraniCasovi))
            startActivity(intent)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT) {
             if(resultCode == RESULT_OK) {
                grupaIzTmpa = data.getStringExtra("imeGrupe")
             }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        //proverava se koja je grupa odabrana, i da li je grupa iz prethodne ili pomocne aktivnosti
        val odabranaGrupa: String = if(grupaIzTmpa == "")
            intent.getStringExtra("imeGrupe")
        else
            grupaIzTmpa
        var casovi = dobijeniCasovi.toList()
        casovi = casovi.filter { cas -> cas.grupa == odabranaGrupa }

        val tvPraznoPolje = findViewById<TextView>(R.id.prazno)
        tvPraznoPolje.text = odabranaGrupa
        tvPraznoPolje.textSize = 50F
        tvPraznoPolje.gravity = Gravity.CENTER
        tvPraznoPolje.setBackgroundColor(Color.BLACK)
        tvPraznoPolje.setTextColor(Color.GREEN)

        //redovi u tabeli u koje ce biti ubaceni casovi
        val trPonedeljak: TableRow = findViewById(R.id.ponedeljakDan)
        val trUtorak: TableRow = findViewById(R.id.utorakDan)
        val trSreda: TableRow = findViewById(R.id.sredaDan)
        val trCetvrtak: TableRow = findViewById(R.id.cetvrtakDan)
        val trPetak: TableRow = findViewById(R.id.petakDan)

        popuniRed("Понедељак", casovi, trPonedeljak)
        popuniRed("Уторак", casovi, trUtorak)
        popuniRed("Среда", casovi, trSreda)
        popuniRed("Четвртак", casovi, trCetvrtak)
        popuniRed("Петак", casovi, trPetak)
    }

    private fun popuniRed(imeDana: String, casovi: List<Predmet>, red: TableRow){
        red.removeViews(1, red.childCount-1)

        val lokalniCasovi = casovi.filter { cas -> cas.dan == imeDana }
        lokalniCasovi.sortedBy { cas -> cas.vreme_pocetka }
        var sati = 8
        for(cas in lokalniCasovi){
            while(sati != cas.vreme_pocetka){
                val tv = TextView(this)
                red.addView(tv)
                sati += 1
            }
            val tv = TextView(this)
            tv.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT)
            tv.gravity = Gravity.CENTER

            val zaSpan = TableRow.LayoutParams(tv.layoutParams as TableRow.LayoutParams)
            zaSpan.span = cas.getVremeKraja() - cas.vreme_pocetka
            tv.layoutParams = zaSpan

            if(izabraniCasovi.contains(cas))
                tv.setBackgroundColor(Color.GREEN)

            tv.isClickable = true
            tv.setOnClickListener({
                //prvo se proverava da li je cas vec izabran
                //zatim da li je izabran u neoj drugoj grupi
                //i zatim da li postoji preklapanje sa nekim izabranim casom iz drugih grupa
                //na osnovu rezultata vrsi se odgovarajuca akcija(bojenje, poruka, sitna podesavanja)
                when {
                    izabraniCasovi.contains(cas) -> {
                        tv.setBackgroundColor(Color.TRANSPARENT)
                        izabraniCasovi.remove(cas)
                    }
                    proveriPostojanjePredmeta(cas) -> {
                        tv.setBackgroundColor(Color.GRAY)
                        tv.isClickable = false
                    }
                    proveriPreklapanje(cas) -> {
                        tv.setBackgroundColor(Color.GREEN)
                        izabraniCasovi.add(cas)
                    }
                    else -> {
                        tv.setBackgroundColor(Color.RED)
                        tv.isClickable = false
                    }
                }
            })
            tv.text = cas.toString()
            red.addView(tv)

            sati += cas.getVremeKraja() - cas.vreme_pocetka
        }
    }

    private fun proveriPostojanjePredmeta(cas: Predmet): Boolean {
        for(casGlobalna in izabraniCasovi.filter { casLokalna -> casLokalna.getNaziv() == cas.getNaziv() }){
            if(cas.tip == casGlobalna.tip) return postojeciPoruka()
        }
        return false
    }

    private fun proveriPreklapanje(cas: Predmet): Boolean {
        for(casGlobalna in izabraniCasovi.filter { casLokalna -> casLokalna.dan == cas.dan }){
            when {
                (cas.vreme_pocetka >= casGlobalna.vreme_pocetka) and (cas.vreme_pocetka < casGlobalna.getVremeKraja()) -> return preklapanjePoruka()
                (casGlobalna.vreme_pocetka >= cas.vreme_pocetka) and (casGlobalna.vreme_pocetka < cas.getVremeKraja()) -> return preklapanjePoruka()
                (cas.vreme_pocetka == casGlobalna.vreme_pocetka) and (cas.getVremeKraja() == casGlobalna.getVremeKraja()) -> return preklapanjePoruka()
                else -> {}
            }
        }
        return true
    }

    private fun postojeciPoruka(): Boolean{
        Toast.makeText(this@FourthActivity, "Ne mozete izabrati ovaj cas, vec ste ga izabrali u drugoj grupi", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun preklapanjePoruka(): Boolean {
        Toast.makeText(this@FourthActivity, "Ne mozete izabrati ovaj cas, postoji preklapanje sa vec izabranim", Toast.LENGTH_SHORT).show()
        return false
    }
}
