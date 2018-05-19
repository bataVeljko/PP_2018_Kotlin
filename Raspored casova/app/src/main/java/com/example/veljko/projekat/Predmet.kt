package com.example.veljko.projekat

import android.os.Parcel
import android.os.Parcelable

//Radi jednostavnosti, vreme pocetka i kraja je predstavljeno samo jednim brojem, 8-21
class Predmet (
        var godina: Int,
        private val naziv: String,
        val grupa: String,
        val dan: String,
        val vreme_pocetka: Int,
        private var vreme_kraja: Int,
        val predavac: String,
        private val ucionica: String,
        val tip: String) : Parcelable {

    var id: Int = 0

    fun getNaziv(): String{
        return naziv
    }

    fun getUcionica(): String{
        return ucionica
    }

    fun getVremeKraja(): Int{
        return vreme_kraja
    }

    fun setVremeKraja(dodatak: Int){
        vreme_kraja += dodatak
    }

    override fun toString(): String {
        var pocetak = ""
        var kraj = ""
        if(vreme_pocetka < 10)
            pocetak += "0"
        if(vreme_kraja < 10)
            kraj += "0"
        pocetak += vreme_pocetka
        pocetak += ":00"
        kraj += vreme_kraja
        kraj += ":00"
        return "$naziv\n$predavac\n$ucionica\n$pocetak-$kraj\n"
    }

    //implementacija fja iz interfejsa Parcelable, radi mogucnosti prenosa podataka kroz aktivnosti
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