package com.example.veljko.projekat

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    //TODO: izbaciti vecinu stvari iz funkcije onCreate
    //vazi za sve aktivitije!!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //lista svih casova koji se nalaze u fajlu 'casovi.json'
        val sviCasovi = makeObjects()

        val rgNivoStudija = findViewById<RadioGroup>(R.id.rbLevel)

        val rbOsnovneStudije = findViewById<RadioButton>(R.id.osnovne)
        val rbMasterStudije = findViewById<RadioButton>(R.id.master)

        val rgGrupaModul = findViewById<RadioGroup>(R.id.rbModul)

        val rbModulInformatika = findViewById<RadioButton>(R.id.informatika)
        val rbModulMatematika = findViewById<RadioButton>(R.id.matematika)
        val rbModulAstronomija = findViewById<RadioButton>(R.id.astronomija)

        val tvSmerText = findViewById<TextView>(R.id.Smer)

        val rgGrupaMatematika = findViewById<RadioGroup>(R.id.rgSmerM)
        val rgGrupaAstronomija = findViewById<RadioGroup>(R.id.rgSmerA)

        val godine = findViewById<LinearLayout>(R.id.vertikalna_podela_godine)

        rgNivoStudija.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == rbOsnovneStudije.id)
                godine.visibility = VISIBLE
            else
                godine.visibility = INVISIBLE
        }

        //provera koje radio dugme je oznaceno, i u zavisnosti od toga,
        //prikazuje se labela 'smer' i moguci smerovi za izabrani modul
        rgGrupaModul.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                rbModulMatematika.id -> {
                    tvSmerText.visibility = VISIBLE
                    rgGrupaMatematika.visibility = VISIBLE
                    rgGrupaAstronomija.visibility = INVISIBLE
                }
                rbModulAstronomija.id -> {
                    tvSmerText.visibility = VISIBLE
                    rgGrupaAstronomija.visibility = VISIBLE
                    rgGrupaMatematika.visibility = INVISIBLE
                }
                else -> {
                    tvSmerText.visibility = INVISIBLE
                    rgGrupaMatematika.visibility = INVISIBLE
                    rgGrupaAstronomija.visibility = INVISIBLE
                }
            }
        }

        val rbSmerML = findViewById<RadioButton>(R.id.ML)
        val rbSmerMM = findViewById<RadioButton>(R.id.MM)
        val rbSmerMR = findViewById<RadioButton>(R.id.MR)
        val rbSmerMP = findViewById<RadioButton>(R.id.MP)
        val rbSmerMS = findViewById<RadioButton>(R.id.MS)
        val rbSmerMA = findViewById<RadioButton>(R.id.MA)
        //ne postoji u rasporedu posebni casovi za astronomiju
        //val smerAI = findViewById<RadioButton>(R.id.AI)
        //val smerAF = findViewById<RadioButton>(R.id.AF)

        val cbPrvaGodina = findViewById<CheckBox>(R.id.god1)
        val cbDrugaGodina = findViewById<CheckBox>(R.id.god2)
        val cbTrecaGodina = findViewById<CheckBox>(R.id.god3)
        val cbCetvrtaGodina = findViewById<CheckBox>(R.id.god4)

        val dugme = findViewById<Button>(R.id.dugmeMain)

        dugme.setOnClickListener({
            val imenaPredmeta = arrayListOf<String>()

            var startNextActivityQuestion = true  //ako neko dugme ili checkbox nije odabrano, nece se prebaciti na sledecu aktivnost
            var filtriraniCasovi = sviCasovi.toList()    //lokalna promenljiva koja sadrzi predmete, koje kasnije saljemo drugoj aktivnosti

            val izabranNivo = rgNivoStudija.checkedRadioButtonId
            val izabraniModul = rgGrupaModul.checkedRadioButtonId

            when (izabranNivo) {
                rbOsnovneStudije.id -> {
                    filtriraniCasovi = filtriraniCasovi.filter{cas :Predmet -> cas.godina != 5 }

                    //pomocna promenljiva koja sluzi da bi se filtirale godine, problem zbog koga je uvedena je u tome sto je moguce izabrati vise od jedne godine
                    val pomocniFiltriraniCasoviZaGodine = mutableListOf<Predmet>()

                    //provera checkbox-ova za godine
                    var isIzabranaGodina = false
                    if ( cbPrvaGodina.isChecked ){
                        pomocniFiltriraniCasoviZaGodine.addAll(filtriraniCasovi.filter { cas -> cas.godina == 1 })
                        isIzabranaGodina = true
                    }
                    if ( cbDrugaGodina.isChecked ){
                        pomocniFiltriraniCasoviZaGodine.addAll(filtriraniCasovi.filter { cas -> cas.godina == 2 })
                        isIzabranaGodina = true
                    }
                    if ( cbTrecaGodina.isChecked ){
                        pomocniFiltriraniCasoviZaGodine.addAll(filtriraniCasovi.filter { cas -> cas.godina == 3 })
                        isIzabranaGodina = true
                    }
                    if ( cbCetvrtaGodina.isChecked ){
                        pomocniFiltriraniCasoviZaGodine.addAll(filtriraniCasovi.filter { cas -> cas.godina == 4 })
                        isIzabranaGodina = true
                    }
                    if(!isIzabranaGodina){
                        poruka("Niste izabrali godinu!")
                        startNextActivityQuestion = false
                    }
                    filtriraniCasovi = pomocniFiltriraniCasoviZaGodine
                }
                rbMasterStudije.id -> filtriraniCasovi = filtriraniCasovi.filter{ cas -> cas.godina == 5}
                else -> {
                    poruka("Niste izabrali nivo studija!")
                    startNextActivityQuestion = false
                }
            }

            if(startNextActivityQuestion) {
                when (izabraniModul) {
                    rbModulMatematika.id -> {
                        filtriraniCasovi = filtriraniCasovi.filter { cas -> !cas.grupa.toList().contains('i') }
                        val izabraniSmer = rgSmerM.checkedRadioButtonId
                        if (!cbPrvaGodina.isChecked) {
                            when (izabraniSmer) {
                                rbSmerMM.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('m') }
                                rbSmerML.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('l') }
                                rbSmerMR.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('r') }
                                rbSmerMP.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('p') }
                                rbSmerMS.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('s') }
                                rbSmerMA.id -> {
                                    poruka("Trenutno nedostupan smer - 'Astronomija',\nizaberite drugi")
                                    startNextActivityQuestion = false
                                }
                                else -> {
                                    poruka("Niste izabrali smer!")
                                    startNextActivityQuestion = false
                                }
                            }
                        }
                        else {
                            if(izabraniSmer == rbSmerMA.id){
                                poruka("Trenutno nedostupan smer - 'Astronomija',\nizaberite drugi")
                                startNextActivityQuestion = false
                            }
                        }
                    }
                    rbModulInformatika.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('i') }
                    rbModulAstronomija.id -> {
                        poruka("Trenutno nedostupan modul - 'Astronomija',\nizaberite drugi")
                        startNextActivityQuestion = false
                    }
                    else -> {
                        poruka("Niste izabrali modul!")
                        startNextActivityQuestion = false
                    }
                }
            }

            for(cas in filtriraniCasovi)
                imenaPredmeta.add(cas.getNaziv())

            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            intent.putParcelableArrayListExtra("filtriraniCasoviIzPrveAktivnosti" ,ArrayList(filtriraniCasovi))
            if(startNextActivityQuestion) {
                startActivity(intent)
                //finish()
            }
        })
    }

    private fun loadJson(): String {
        val reader: BufferedReader?
        val s = StringBuffer()
        try {
            reader = BufferedReader(InputStreamReader(assets.open("casovi.json")))
            var line = reader.readLine()
            while(line != null) {
                s.append(line)
                line = reader.readLine()
            }
        } catch (e: NullPointerException){
            Log.d("Null", "Nula!")
        } catch (e: Exception){
            e.printStackTrace()
        }
        return s.toString()
    }

    private fun makeObjects(): MutableList<Predmet>{
        val jsonObjekatCasovi = JSONObject(loadJson())
        val nizCasova = jsonObjekatCasovi.getJSONArray("casovi")
        var i = 0
        val duzina = nizCasova.length()
        val sviCasovi = mutableListOf<Predmet>()
        while(i < duzina){
            val jedanCas = nizCasova.getJSONObject(i)
            val godina = jedanCas.getString("godina").toInt()
            val naziv = jedanCas.getString("ime")
            val grupa = jedanCas.getString("grupa")
            val dan = jedanCas.getString("dan")
            //dobije se vreme pocetka predavanja -> prva dva broja se uzmu -> nastaje niz charova -> od toga pravimo string -> od stringa zatim int
            val vremePocetka = jedanCas.getString("pocetak").toInt()
            val vremeKraja = jedanCas.getString("kraj").toInt()
            val predavac = jedanCas.getString("predavac")
            val ucionica = jedanCas.getString("mesto")
            val tip = jedanCas.getString("tip")
            sviCasovi.add(Predmet(godina, naziv, grupa, dan, vremePocetka, vremeKraja, predavac, ucionica, tip))
            i += 1
        }
        return sviCasovi
    }

    private fun poruka(msg:String){
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
    }

}

//Radi jednostavnosti, vreme pocetka i kraja je predstavljeno samo jednim brojem, 8-21
class Predmet (
        val godina: Int,
        private val naziv: String,
        val grupa: String,
        val dan: String,
        val vreme_pocetka: Int,
        val vreme_kraja: Int,
        private val predavac: String,
        private val ucionica: String,
        val tip: String) : Parcelable{

    fun getNaziv(): String{
        return naziv
    }

    fun getUcionica(): String{
        return ucionica
    }

    override fun toString(): String {
        return "$naziv\n$predavac\n$ucionica\n$vreme_pocetka:00 - $vreme_kraja:00\n"
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.godina)
        dest.writeString(this.naziv)
        dest.writeString(this.grupa)
        dest.writeString(this.dan)
        dest.writeInt(this.vreme_pocetka)
        dest.writeInt(this.vreme_kraja)
        dest.writeString(this.predavac)
        dest.writeString(this.ucionica)
        dest.writeString(this.tip)
    }

    companion object CREATOR : Parcelable.Creator<Predmet> {
        override fun createFromParcel(parcel: Parcel): Predmet {
            return Predmet(parcel)
        }

        override fun newArray(size: Int): Array<Predmet?> {
            return arrayOfNulls(size)
        }
    }
}