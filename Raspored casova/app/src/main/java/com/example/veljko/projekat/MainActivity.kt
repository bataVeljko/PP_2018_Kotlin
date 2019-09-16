@file:Suppress("NAME_SHADOWING")

package com.example.veljko.projekat

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*

class MainActivity : AppCompatActivity() {

    companion object {
        var sviCasovi = mutableListOf<Predmet>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //httpZahtevi()

        val pbProgressBar = findViewById<ProgressBar>(R.id.progressBar)
        val dugme = findViewById<Button>(R.id.dugmeMain)
        dugme.isEnabled = false
        //zahtev ka stranicama 'http://poincare.matf.bg.ac.rs/~kmiljan/raspored/sve/...' radi dohvatanja sadrzaja
        val skidanjeCasova = SkiniCasove(pbProgressBar, dugme)
        skidanjeCasova.execute()

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
            if (checkedId == rbOsnovneStudije.id)
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


        dugme.setOnClickListener {
            val imenaPredmeta = arrayListOf<String>()

            var startNextActivityQuestion = true  //ako neko dugme ili checkbox nije odabrano, nece se prebaciti na sledecu aktivnost
            var filtriraniCasovi = sviCasovi.toList()    //lokalna promenljiva koja sadrzi predmete, koje kasnije saljemo drugoj aktivnosti

            val izabranNivo = rgNivoStudija.checkedRadioButtonId
            val izabraniModul = rgGrupaModul.checkedRadioButtonId

            when (izabranNivo) {
                rbOsnovneStudije.id -> {
                    filtriraniCasovi = filtriraniCasovi.filter { cas: Predmet -> cas.godina != 5 }

                    //pomocna promenljiva koja sluzi da bi se filtirale godine, problem zbog koga je uvedena je u tome sto je moguce izabrati vise od jedne godine
                    val pomocniFiltriraniCasoviZaGodine = mutableListOf<Predmet>()

                    //provera checkbox-ova za godine
                    var isIzabranaGodina = false
                    if (cbPrvaGodina.isChecked) {
                        pomocniFiltriraniCasoviZaGodine.addAll(filtriraniCasovi.filter { cas -> cas.godina == 1 })
                        isIzabranaGodina = true
                    }
                    if (cbDrugaGodina.isChecked) {
                        pomocniFiltriraniCasoviZaGodine.addAll(filtriraniCasovi.filter { cas -> cas.godina == 2 })
                        isIzabranaGodina = true
                    }
                    if (cbTrecaGodina.isChecked) {
                        pomocniFiltriraniCasoviZaGodine.addAll(filtriraniCasovi.filter { cas -> cas.godina == 3 })
                        isIzabranaGodina = true
                    }
                    if (cbCetvrtaGodina.isChecked) {
                        pomocniFiltriraniCasoviZaGodine.addAll(filtriraniCasovi.filter { cas -> cas.godina == 4 })
                        isIzabranaGodina = true
                    }
                    if (!isIzabranaGodina) {
                        poruka("Niste izabrali godinu!")
                        startNextActivityQuestion = false
                    }
                    filtriraniCasovi = pomocniFiltriraniCasoviZaGodine
                }
                rbMasterStudije.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.godina == 5 }
                else -> {
                    poruka("Niste izabrali nivo studija!")
                    startNextActivityQuestion = false
                }
            }

            if (startNextActivityQuestion) {
                when (izabraniModul) {
                    rbModulMatematika.id -> {
                        filtriraniCasovi = filtriraniCasovi.filter { cas -> !cas.grupa.toList().contains('И') }
                        val pomocniFiltriraniCasoviZaPrvuGodinu = filtriraniCasovi.filter { cas -> cas.godina == 1 }

                        val izabraniSmer = rgGrupaMatematika.checkedRadioButtonId
                        if (!rbMasterStudije.isChecked) {
                            when (izabraniSmer) {
                                rbSmerMM.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('М') }
                                rbSmerML.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('Л') }
                                rbSmerMR.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('Р') }
                                rbSmerMP.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('Н') }
                                rbSmerMS.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('В') }
                                rbSmerMA.id -> {
                                    poruka("Trenutno nedostupan smer - 'Astronomija'\nIzaberite drugi")
                                    startNextActivityQuestion = false
                                }
                                else -> {
                                    poruka("Niste izabrali smer!")
                                    startNextActivityQuestion = false
                                }
                            }
                        }
                        else {
                            when (izabraniSmer) {
                                rbSmerMM.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.trim() == "5ММ" }
                                rbSmerML.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('Л') }
                                rbSmerMR.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('Р') }
                                rbSmerMP.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('Н') }
                                rbSmerMS.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('В') }
                                rbSmerMA.id -> {
                                    poruka("Trenutno nedostupan smer - 'Astronomija'\nIzaberite drugi")
                                    startNextActivityQuestion = false
                                }
                                else -> {
                                    poruka("Niste izabrali smer!")
                                    startNextActivityQuestion = false
                                }
                            }
                        }

                        if(cbPrvaGodina.isChecked){
                            filtriraniCasovi += pomocniFiltriraniCasoviZaPrvuGodinu
                        }
                    }

                    rbModulInformatika.id -> filtriraniCasovi = filtriraniCasovi.filter { cas -> cas.grupa.toList().contains('И') }

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

            for (cas in filtriraniCasovi)
                imenaPredmeta.add(cas.getNaziv())

            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            intent.putParcelableArrayListExtra("filtriraniCasoviIzPrveAktivnosti", ArrayList(filtriraniCasovi))
            if (startNextActivityQuestion) {
                startActivity(intent)
                //finish()
            }
        }
    }

    private fun poruka(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
    }

}

/* drugi nacin za dohvatanje fajlova sa sajta, ali nemogucnost koricnenja ProgressBar-a
    private fun httpZahtevi() {
        //http zahtev


        /*for(indeks in indeksiStranica)
            jedanZahtev(url+indeks)*/
    }

    fun jedanZahtev(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val string = response.body()?.string()
        val table = Jsoup.parse(string).select("table")[0]
        for(row in table.select("tr")){
            for(col in row.select("td"))
            //parsiranje tabele sa casovima
            //jedini u td koji ima atribut height sa vrednoscu 90% je onaj koji sadrzi tabelu sa rasporedom casova za zadatu grupu
                if(col.hasAttr("height") && (col.attr("height").toString() == "90%")){
                    parsirajTabelu(col)
                }
        }

    val thread = Thread(Runnable {
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response) {
                try {
                    val string = response.body()?.string()
                    val table = Jsoup.parse(string).select("table")[0]
                    for(row in table.select("tr")){
                        for(col in row.select("td"))
                            //parsiranje tabele sa casovima
                            //jedini u td koji ima atribut height sa vrednoscu 90% je onaj koji sadrzi tabelu sa rasporedom casova za zadatu grupu
                            if(col.hasAttr("height") && (col.attr("height").toString() == "90%")){
                                parsirajTabelu(col)
                            }
                    }
                } catch (ioe: IOException) {
                    ioe.printStackTrace()
                    }
                }
            })
        })
        thread.run()
    }

    private fun parsirajTabelu(kolona: Element) {
        //prvih 7 karaktera je "GRUPA: "
        val imeGrupe = kolona.select("h1").text().substring(7)
        val godina = (imeGrupe[0]-'0')

        val tabela = kolona.selectFirst("table")
        var i = 0
        //trenutno resenje
        for(red in tabela.select("tr")){
            if(i < 2) {
                i += 1
                //prva dva reda tabele sadrze indekse casova i vremena odrzavanja
                continue
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
                    sati += 1
                } else
                    when {
                        kolona.hasAttr("colspan") -> {
                        val trajanje = kolona.attr("colspan").toInt()
                        val cas = kolona.html().split("<br>")
                        casNaziv = cas[0]
                        casPredavac = cas[1]
                        casUcionica = cas[2]
                        casTip = if(casNaziv.contains("(вежбе)")) "v" else "p"
                        val pred = Predmet(godina, casNaziv, imeGrupe, danUNedelji, sati, sati+trajanje, casPredavac, casUcionica, casTip)
                        sviCasovi.add(pred)
                        sati += trajanje
                    }
                    kolona.select("table") != null -> {
                        //ako kolona sadrzi tabelu u sebi -> postoji vise casova u toj koloni, pa moramo da dohvatamo jedan po jedan
                        val unutrasnjaTabela = kolona.select("table")
                        for(unutrasnjiRed in unutrasnjaTabela.select("tr")){
                            for(unutrasnjaKolona in unutrasnjiRed.select("td")){
                                //moze da sadrzi podatke o casu ili string '&nbsp;', ako je prazno polje
                                val cas = unutrasnjaKolona.selectFirst("small").html().split("<br>")
                                if(cas.size == 4){
                                    casNaziv = cas[0]
                                    casGrupa = cas[1]
                                    casPredavac = cas[2]
                                    casUcionica = cas[3]
                                    casTip = if(casNaziv.contains("(вежбе)")) "p" else "v"

                                    //na nekim mestima su spojene dve grupe odjednom
                                    if(casGrupa.contains(',')){
                                        val listaGrupa = casGrupa.split(", ")
                                        for(gr in listaGrupa) {
                                            val pred = Predmet(godina, casNaziv, "$imeGrupe-$gr", danUNedelji, sati, sati + 1, casPredavac, casUcionica, casTip)
                                            if (proveraPostojanja(pred))
                                                sviCasovi.add(pred)
                                        }
                                    } else {
                                        val pred = Predmet(godina, casNaziv, "$imeGrupe-$casGrupa", danUNedelji, sati, sati + 1, casPredavac, casUcionica, casTip)
                                        if (proveraPostojanja(pred))
                                            sviCasovi.add(pred)
                                    }
                                }
                            }
                        }
                    }
                }
                //predstavlja prazno polje, ili polje koje sadrzi tabelu
                if(!kolona.hasAttr("height") && !kolona.hasAttr("colspan"))
                    sati += 1
            }
        }
    }

    private fun proveraPostojanja(pred: Predmet): Boolean {
        var pomocna = sviCasovi.toList()
        pomocna = pomocna.filter { tmp -> tmp.getNaziv() == pred.getNaziv() }
        pomocna = pomocna.filter { tmp -> tmp.grupa == pred.grupa }
        if(pomocna.any()){
            pomocna[0].setVremeKraja(1)
            return false
        }
        return true
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

    private fun makeObjects(){
        val jsonObjekatCasovi = JSONObject(loadJson())
        val nizCasova = jsonObjekatCasovi.getJSONArray("casovi")
        var i = 0
        val duzina = nizCasova.length()
        //val sviCasovi = mutableListOf<Predmet>()
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
    }
*/