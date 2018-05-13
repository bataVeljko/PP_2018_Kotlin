package com.example.veljko.projekat

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
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_fifth2.*

private var casovi = arrayListOf<Predmet>()

class ProbnaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth2)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        casovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzCetvrteAktivnosti")

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
            var trenutniTab = View(context)
            val lvTrenutnaLista: ListView
            val filtriranaListaCasova: List<Predmet>
            val arrayListFiltriranihCasova = arrayListOf<String>()
            when(arguments.getInt(ARG_SECTION_NUMBER)){
                1 -> {
                    trenutniTab = inflater.inflate(R.layout.ponedeljak, container, false)
                    lvTrenutnaLista = trenutniTab.findViewById(R.id.spisak)
                    filtriranaListaCasova = casovi.filter { cas -> cas.dan == "ponedeljak" }
                    for(cas in filtriranaListaCasova)
                        arrayListFiltriranihCasova.add(cas.toString())
                    val adapterSpisak = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListFiltriranihCasova)
                    //TODO
                    //sortirati predmete po vremenu
                    lvTrenutnaLista.adapter = adapterSpisak
                }
                2 -> {
                    trenutniTab = inflater.inflate(R.layout.utorak, container, false)
                    lvTrenutnaLista = trenutniTab.findViewById(R.id.spisak)
                    filtriranaListaCasova = casovi.filter { cas -> cas.dan == "utorak" }
                    filtriranaListaCasova.sortedBy { cas ->  cas.vreme_pocetka}
                    for(cas in filtriranaListaCasova)
                        arrayListFiltriranihCasova.add(cas.toString())
                    val adapterSpisak = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListFiltriranihCasova)
                    lvTrenutnaLista.adapter = adapterSpisak
                }
                3 -> {
                    trenutniTab = inflater.inflate(R.layout.sreda, container, false)
                    lvTrenutnaLista = trenutniTab.findViewById(R.id.spisak)
                    filtriranaListaCasova = casovi.filter { cas -> cas.dan == "sreda" }
                    filtriranaListaCasova.sortedBy { cas ->  cas.vreme_pocetka}
                    for(cas in filtriranaListaCasova)
                        arrayListFiltriranihCasova.add(cas.toString())
                    val adapterSpisak = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListFiltriranihCasova)
                    lvTrenutnaLista.adapter = adapterSpisak
                }
                4 -> {
                    trenutniTab = inflater.inflate(R.layout.cetvrtak, container, false)
                    lvTrenutnaLista = trenutniTab.findViewById(R.id.spisak)
                    filtriranaListaCasova = casovi.filter { cas -> cas.dan == "cetvrtak" }
                    filtriranaListaCasova.sortedBy { cas ->  cas.vreme_pocetka}
                    for(cas in filtriranaListaCasova)
                        arrayListFiltriranihCasova.add(cas.toString())
                    val adapterSpisak = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListFiltriranihCasova)
                    lvTrenutnaLista.adapter = adapterSpisak
                }
                5 -> {
                    trenutniTab = inflater.inflate(R.layout.petak, container, false)
                    lvTrenutnaLista = trenutniTab.findViewById(R.id.spisak)
                    filtriranaListaCasova = casovi.filter { cas -> cas.dan == "petak" }
                    filtriranaListaCasova.sortedBy { cas ->  cas.vreme_pocetka}
                    for(cas in filtriranaListaCasova)
                        arrayListFiltriranihCasova.add(cas.toString())
                    val adapterSpisak = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayListFiltriranihCasova)
                    lvTrenutnaLista.adapter = adapterSpisak
                }
                else -> { }
            }
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
