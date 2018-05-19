package com.example.veljko.projekat

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class TmpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmp)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val lvLista = findViewById<ListView>(R.id.listaGrupa)
        val grupe = intent.getStringArrayListExtra("grupe")
        val adapterGrupe = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grupe)
        if(grupe.size > 1)
            adapterGrupe.sort { s1, s2 ->  s1.compareTo(s2)}
        lvLista.adapter = adapterGrupe

        lvLista.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val izabranaGrupa = grupe[position]
            Toast.makeText(this@TmpActivity, "Izabrali ste grupu: " + grupe[position], Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.putExtra("imeGrupe", izabranaGrupa)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Nema nazad", Toast.LENGTH_SHORT).show()
    }

}
