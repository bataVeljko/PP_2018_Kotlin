package com.example.veljko.projekat

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.sql.SQLException

class Raspored(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "Raspored.db"
        const val TABLE_NAME = "Raspored_table"
        const val ID = "id"
        const val GODINA = "GODINA"
        const val NAZIV = "NAZIV"
        const val GRUPA = "GRUPA"
        const val DAN = "DAN"
        const val VREME_POCETKA = "VREME_POCETKA"
        const val VREME_KRAJA = "VREME_KRAJA"
        const val PREDAVAC = "PREDAVAC"
        const val UCIONICA = "UCIONICA"
        const val TIP = "TIP"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "create table " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                GODINA + " INTEGER, " +
                                                                NAZIV + " varchar(255), " +
                                                                GRUPA + " varchar(15), " +
                                                                DAN + " varchar(15), " +
                                                                VREME_POCETKA + " INTEGER, " +
                                                                VREME_KRAJA + " INTEGER, " +
                                                                PREDAVAC + " varchar(127), " +
                                                                UCIONICA + " varchar(15), " +
                                                                TIP + " varchar(3))"
        //provera da li je razlicito od null
        try {
            db?.execSQL(query)
        } catch (e: SQLException){
            Log.d("Upit", "Ne valja")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(predmet: Predmet){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("GODINA", predmet.godina)
        cv.put("NAZIV", predmet.getNaziv())
        cv.put("GRUPA", predmet.grupa)
        cv.put("DAN", predmet.dan)
        cv.put("VREME_POCETKA", predmet.vreme_pocetka)
        cv.put("VREME_KRAJA", predmet.getVremeKraja())
        cv.put("PREDAVAC", predmet.predavac)
        cv.put("UCIONICA", predmet.getUcionica())
        cv.put("TIP", predmet.tip)
        val res = db.insert(TABLE_NAME, null, cv)
        if(res == (-1).toLong())
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
    }

    fun readData(): MutableList<Predmet>{
        val lista = mutableListOf<Predmet>()

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)
        while(result.moveToNext()){
            val predmet = Predmet(
                    result.getString(1).toInt(),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5).toInt(),
                    result.getString(6).toInt(),
                    result.getString(7),
                    result.getString(8),
                    result.getString(9))
            predmet.id = result.getString(0).toInt()
            lista.add(predmet)
        }
        result.close()
        db.close()

        return lista
    }

}
