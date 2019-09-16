package com.example.veljko.projekat

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import android.view.View.INVISIBLE
import android.widget.Button
import android.widget.ProgressBar
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SkiniCasove(val pbProgressBar: ProgressBar, val dugme: Button): AsyncTask<Void, Void, Void>() {

    /* trenutno nepotrebna fja
    override fun onPreExecute() {
        super.onPreExecute()
    }*/

    @SuppressLint("SimpleDateFormat")
    override fun doInBackground(vararg params: Void?): Void? {
        val url = "http://poincare.matf.bg.ac.rs/~kmiljan/raspored/sve/"
        val indeksiStranica = arrayListOf<String>()

        // Zahtev za dohvatanje grupa
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        try {
            val response = client.newCall(request).execute()

            //val headers = response.headers()
            //Log.d("Tag", "${headers.getDate("")}")
            /*for (i in 0 until headers.size()){
                Log.d("Tag", "$i: ${headers.name(i)}: ${headers.value(i)}")
            }*/

            val string = response.body()?.string()
            val table = Jsoup.parse(string).select("table")[0]

            // Hardcoded last-modified date, because it doesn't exist in http header
            val ps = Jsoup.parse(string).select("p")
            val lastModified = ps.last().toString().split("<br>").first().toString().split(" ").last()
            val lms = lastModified.split(".")

            val parser =  SimpleDateFormat("dd.MM.yyyy")
            val lastModifiedDateFromWebPage = parser.parse(lastModified)
            val lastModifiedFileText = File(LoginActivity.FILE_NAME).readText(Charset.defaultCharset())
            Log.d("Tag", lastModifiedFileText)
            val lastModifiedDateFromFile = parser.parse(lastModifiedFileText)
            // compareTo: greater -> 1; equal -> 0; smaller -> -1
            if(lastModifiedDateFromFile.compareTo(lastModifiedDateFromWebPage) != -1){
                Log.d("Tag", "Ne treba nista skidati")
                // TODO: Ucitati podatke iz tabele
                return null
            }
            // TODO: Izbrisati posojecu tabelu sa svim predmetima i napraviti novu prema novom rasporedu
            Log.d("Tag", "Treba skidati")


            // Log.d("Tag", "${lastModifiedDate.compareTo(parser.parse("08.03.2019"))}")

            for (a in table.getElementsByAttributeValue("name", "forms").select("option")){
                val currentGroup = a.attr(("value"))
                if (currentGroup != "-1")
                    indeksiStranica.add(currentGroup)
            }
        } catch (e: IOException){
            e.printStackTrace()
        }

        // NOTE: deprecated
        /*for(i in 1..41){
            var indeks = "form_0"
            if(i < 10)
                indeks += "0"
            indeks += "$i.html"
            indeksiStranica.add(indeks)
        }*/

        for(indeks in indeksiStranica)
            jedanZahtev(url+indeks)

        return null
    }

    private fun jedanZahtev(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        try {
            val response = client.newCall(request).execute()
            val string = response.body()?.string()
            val table = Jsoup.parse(string).select("table")[0]
            for (row in table.select("tr")) {
                for (col in row.select("td"))
                //parsiranje tabele sa casovima
                //jedini u td koji ima atribut height sa vrednoscu 90% je onaj koji sadrzi tabelu sa rasporedom casova za zadatu grupu
                    if (col.hasAttr("height") && (col.attr("height").toString() == "90%")) {
                        parsirajTabelu(col)
                    }
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun parsirajTabelu(kolona: Element) {
        //prvih 7 karaktera je "GRUPA: "
        val imeGrupe = kolona.select("h1").text().substring(7)
        val godina = (imeGrupe[0]-'0')

        val tabela = kolona.selectFirst("table")
        var i = 0

        tabela.select("tr").forEach { red ->
            if(i < 2) {
                i++
                //prva dva reda tabele sadrze indekse casova i vremena odrzavanja
                return@forEach
            }

            //prva kolona u redu je ime dana, istovremeno se oznacava vreme u satima
            var sati = 7
            var danUNedelji = ""
            red.select("td").forEach { kolona ->
                var casNaziv: String
                var casPredavac: String
                var casUcionica: String
                var casGrupa: String
                var casTip: String
                //parsiranje jednog casa
                if(sati < 8) {
                    danUNedelji = kolona.text()
                    sati++
                } else
                    when {
                    //vise spojenih casova
                        kolona.hasAttr("colspan") -> {
                            val trajanje = kolona.attr("colspan").toInt()
                            val cas = kolona.html().split("<br>")
                            casNaziv = cas[0]
                            casPredavac = cas[1]
                            casUcionica = cas[2]
                            if(casUcionica == "ЈАГ3&amp;4")
                                casUcionica = "ЈАГ3&4"
                            casTip = if(casNaziv.contains("(вежбе)")) "v" else "p"
                            val pred = Predmet(godina, casNaziv, imeGrupe, danUNedelji, sati, sati + trajanje, casPredavac, casUcionica, casTip)
                            MainActivity.sviCasovi.add(pred)
                            sati += trajanje
                        }
                    //kolona koja sadrzi tabelu u sebi, uvek je u pitanju jedan cas sa 4 reda
                        kolona.select("table") != null -> {
                            //ako kolona sadrzi tabelu u sebi -> postoji vise casova u toj koloni, pa moramo da dohvatamo jedan po jedan
                            val unutrasnjaTabela = kolona.select("table")
                            unutrasnjaTabela.select("tr").forEach { unutrasnjiRed ->
                                unutrasnjiRed.select("td").forEach { unutrasnjaKolona ->
                                    //moze da sadrzi podatke o casu ili string '&nbsp;', ako je prazno polje
                                    val cas = if (unutrasnjaKolona.selectFirst("small") != null){
                                        unutrasnjaKolona.selectFirst("small").html().split("<br>")
                                    } else{
                                        unutrasnjaKolona.html().split("<br>")
                                    }
                                    when {
                                        cas.size == 4 -> {
                                            casNaziv = cas[0]
                                            casGrupa = cas[1]
                                            casPredavac = cas[2]
                                            casUcionica = cas[3]
                                            if(casUcionica == "ЈАГ3&amp;4")
                                                casUcionica = "ЈАГ3&4"
                                            casTip = if(casNaziv.contains("(вежбе)")) "p" else "v"
                                        }
                                        cas.size == 3 -> {
                                            casNaziv = cas[0]
                                            casPredavac = cas[1]
                                            casUcionica = cas[2]
                                            casGrupa = ""
                                            if(casUcionica == "ЈАГ3&amp;4")
                                                casUcionica = "ЈАГ3&4"
                                            casTip = if(casNaziv.contains("(вежбе)")) "p" else "v"
                                        }
                                        else -> {
                                            casGrupa = ""
                                            casNaziv = ""
                                            casPredavac = ""
                                            casUcionica = ""
                                            casTip = ""
                                        }
                                    }
                                    //na nekim mestima su spojene dve grupe odjednom
                                    if(casGrupa.contains(',')){
                                        val listaGrupa = casGrupa.split(", ")
                                        for(gr in listaGrupa) {
                                            val pred = Predmet(godina, casNaziv, "$imeGrupe-$gr", danUNedelji, sati, sati + 1, casPredavac, casUcionica, casTip)
                                            if (proveraPostojanja(pred))
                                                MainActivity.sviCasovi.add(pred)
                                        }
                                    }
                                    else {
                                        val pred = Predmet(godina, casNaziv, "$imeGrupe-$casGrupa", danUNedelji, sati, sati + 1, casPredavac, casUcionica, casTip)
                                        if (proveraPostojanja(pred))
                                            MainActivity.sviCasovi.add(pred)
                                    }
                                }
                            }
                        }
                    }
                //predstavlja prazno polje, ili polje koje sadrzi tabelu
                if(!kolona.hasAttr("height") && !kolona.hasAttr("colspan"))
                    sati++
            }
        }
    }

    private fun proveraPostojanja(pred: Predmet): Boolean {
        var pomocna = MainActivity.sviCasovi.toList()
        pomocna = pomocna.filter { tmp -> tmp.getNaziv() == pred.getNaziv() }
        pomocna = pomocna.filter { tmp -> tmp.grupa == pred.grupa }
        if(pomocna.any()){
            pomocna[0].setVremeKraja(1)
            return false
        }
        return true
    }

    override fun onPostExecute(result: Void?) {
        pbProgressBar.visibility = INVISIBLE
        dugme.isEnabled = true
        super.onPostExecute(result)
    }
}