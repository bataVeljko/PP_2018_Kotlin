package com.example.veljko.projekat

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_fifth.*

private var casovi = arrayListOf<Predmet>()
private var aktivnostIzKojeSuPristigliPodaci: String = ""

class FifthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //proverava da li su podaci poslati iz cetvrte aktivnosti ili ucitani iz postojece baze
        if(intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzCetvrteAktivnosti") != null) {
            casovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzCetvrteAktivnosti")
            aktivnostIzKojeSuPristigliPodaci = "Cetvrta"
        }
        else {
            //cetvrta aktivnost nije pokrenuta, korisnik se odmah prebacio na ovu aktivnost
            //izvlacimo podatke iz baze, sacuvane u telefonu, pomocu SQLite-a
            casovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzPocetneAktivnosti")
            aktivnostIzKojeSuPristigliPodaci = "Login"
        }

        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment { return PlaceholderFragment.newInstance(position + 1) }
        override fun getCount(): Int { return 5 }
    }

    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            //provera po tabovima
            return when(arguments?.getInt(ARG_SECTION_NUMBER)){
                1 -> kreirajTab("Понедељак", inflater)
                2 -> kreirajTab("Уторак", inflater)
                3 -> kreirajTab("Среда", inflater)
                4 -> kreirajTab("Четвртак", inflater)
                5 -> kreirajTab("Петак", inflater)
                else -> {null}
            }
        }

        @SuppressLint("SetTextI18n")
        private fun kreirajTab(dan: String, inflater: LayoutInflater): View? {
            val arrayListFiltriranihCasova = arrayListOf<String>()

            val trenutniTab = inflater.inflate(R.layout.jedan_tab, container, false)
            val lvTrenutnaLista = trenutniTab.findViewById<ListView>(R.id.spisak)

            val dugme = trenutniTab.findViewById<Button>(R.id.dugmeFifth)
            if(aktivnostIzKojeSuPristigliPodaci == "Login")
                dugme.text = "ДАЉЕ"
            dugme.setOnClickListener({
                val intent = Intent(context, SixthActivity::class.java)
                intent.putParcelableArrayListExtra("filtriraniCasoviIzPeteAktivnosti" ,ArrayList(casovi))
                intent.putExtra("imeAktivnosti", aktivnostIzKojeSuPristigliPodaci)
                startActivity(intent)
            })

            val filtriranaListaCasova = casovi.filter { cas -> cas.dan == dan }
            for(cas in filtriranaListaCasova)
                arrayListFiltriranihCasova.add(cas.toString())
            val adapterSpisak = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListFiltriranihCasova)
            /*ako ima vise od jednog izabranog casa, sortiraj
              sortira se po vremenu pocetka casa
              posto su podaci predstavljeni kao stringovi, dohvatamo prvo pojavljivanje ':' i uzimamo dva karaktera pre njih, npr 09, 15, 20
              isto radimo i za drugi cas u listi
              posto je dobijeni element List<Char> moramo prvo pretvoriti u string, pa tek zatim u int
              nakon cega sledi jednostavno oduzimanje */
            if(filtriranaListaCasova.size > 1)
                adapterSpisak.sort { o1, o2 ->  o1.subSequence(o1.indexOf(':')-2, o1.indexOf(':')).toString().toInt() - o2.subSequence(o2.indexOf(':')-2, o2.indexOf(':')).toString().toInt()}
            lvTrenutnaLista.adapter = adapterSpisak
            return trenutniTab
        }

        companion object {
            private const val ARG_SECTION_NUMBER = "section_number"
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
