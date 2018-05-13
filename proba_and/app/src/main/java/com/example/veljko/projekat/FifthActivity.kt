package com.example.veljko.projekat

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView

class FifthActivity : AppCompatActivity() {

    //TODO
    //Ne radi za veci broj podataka
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val casovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzCetvrteAktivnosti")

        //za sada je odvojeno dodavanje po danima
        val trDrugiRed = findViewById<TableRow>(R.id.drugiRed)
        val trTreciRed = findViewById<TableRow>(R.id.treciRed)
        val trCetvrtiRed = findViewById<TableRow>(R.id.cetvrtiRed)
        val trPetiRed = findViewById<TableRow>(R.id.petiRed)
        val trSestiRed = findViewById<TableRow>(R.id.sestiRed)

        popuniRed("ponedeljak", casovi, trDrugiRed)
        popuniRed("utorak", casovi, trTreciRed)
        popuniRed("sreda", casovi, trCetvrtiRed)
        popuniRed("cetvrtak", casovi, trPetiRed)
        popuniRed("petak", casovi, trSestiRed)

        val dugme = findViewById<Button>(R.id.dugmeFourth)
        dugme.setOnClickListener({
            dugme.setBackgroundColor(Color.RED)
            val intent = Intent(this@FifthActivity, ProbnaActivity::class.java)
            //intent.putParcelableArrayListExtra("filtriraniCasoviIzCetvrteAktivnosti" , ArrayList(izabraniCasovi))
            startActivity(intent)
        })
    }

    private fun popuniRed(imeDana: String, casovi: ArrayList<Predmet>, red: TableRow){
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
            zaSpan.span = cas.vreme_kraja - cas.vreme_pocetka
            tv.layoutParams = zaSpan
            /*val boje = arrayListOf<Int>()
            boje.add(Color.GREEN)
            boje.add(Color.RED)
            boje.add(Color.BLUE)*/
            tv.setBackgroundColor(Color.BLUE)

            tv.text = cas.toString()
            red.addView(tv)

            sati += cas.vreme_kraja - cas.vreme_pocetka
        }
    }

}