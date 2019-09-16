package com.example.veljko.projekat

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Matrix2f
import android.widget.*
import java.util.ArrayList
import kotlin.math.max
import kotlin.math.min
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.view.ViewCompat
import android.view.*
import android.view.GestureDetector.OnGestureListener
import android.view.animation.ScaleAnimation

private const val RESULT = 12345

class FourthActivity : AppCompatActivity() {

    private var dobijeniCasovi = arrayListOf<Predmet>()
    private val izabraniCasovi = arrayListOf<Predmet>()
    private var grupe = arrayListOf<String>()
    private var odabranaGrupa = String()
    private var grupaIzTmpa : String? = String()

    /*private var AXIS_Y_MIN: Float = -1.0f
    private var AXIS_X_MIN: Float = -1.0f
    private var AXIS_X_MAX: Float = 1.0f
    private var AXIS_Y_MAX: Float = 1.0f
    var viewSV : ScrollView? = null
    var viewHSV : HorizontalScrollView? = null
    var viewTL : TableLayout? = null
    private lateinit var SGD : ScaleGestureDetector
    lateinit var GD : GestureDetector
    var scale = 1.0f
    private val mCurrentViewport = RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX)
    private val mContentRect: Rect? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)
        //radi bolje preglednosti
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        /*viewSV = findViewById(R.id.viewSV)
        viewHSV = findViewById(R.id.viewHSV)
        viewTL = findViewById(R.id.tabela)*/

        dobijeniCasovi = intent.getParcelableArrayListExtra<Predmet>("filtriraniCasoviIzTreceAktivnosti")
        grupe = intent.getStringArrayListExtra("grupe")
        odabranaGrupa = intent.getStringExtra("imeGrupe")

        val bGrupa = findViewById<Button>(R.id.drugaGrupa)
        bGrupa.setOnClickListener {
            val intent = Intent(this@FourthActivity, TmpActivity::class.java)
            intent.putExtra("grupe" ,grupe)
            startActivityForResult(intent, RESULT)
        }

        val dugme = findViewById<Button>(R.id.dugmeFourth)
        dugme.setOnClickListener {
            val intent = Intent(this@FourthActivity, FifthActivity::class.java)
            intent.putParcelableArrayListExtra("filtriraniCasoviIzCetvrteAktivnosti" , ArrayList(izabraniCasovi))
            startActivity(intent)
        }

        /*
        GD = GestureDetector(this, object : OnGestureListener{
            override fun onShowPress(e: MotionEvent?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                mContentRect?.apply {
                    // Pixel offset is the offset in screen pixels, while viewport offset is the
                    // offset within the current viewport.
                    val viewportOffsetX = velocityX * mCurrentViewport.width() / width()
                    val viewportOffsetY = -velocityY * mCurrentViewport.height() / height()


                    // Updates the viewport, refreshes the display.
                    mCurrentViewport.set(
                            mCurrentViewport.left + viewportOffsetX,
                            mCurrentViewport.bottom + viewportOffsetY,
                            0f,
                            0f
                    )
                }

                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                mContentRect?.apply {
                    // Pixel offset is the offset in screen pixels, while viewport offset is the
                    // offset within the current viewport.
                    val viewportOffsetX = distanceX * mCurrentViewport.width() / width()
                    val viewportOffsetY = -distanceY * mCurrentViewport.height() / height()


                    // Updates the viewport, refreshes the display.
                    mCurrentViewport.set(
                            mCurrentViewport.left + viewportOffsetX,
                            mCurrentViewport.bottom + viewportOffsetY,
                            0f,
                            0f
                    )
                }

                return true
            }
        })

        SGD = ScaleGestureDetector(this, object: ScaleGestureDetector.SimpleOnScaleGestureListener(){
            private val viewportFocus = PointF()
            private var lastSpanX: Float = 0f
            private var lastSpanY: Float = 0f

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                lastSpanX = SGD.currentSpanX
                lastSpanY = SGD.currentSpanY
                return true
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                val spanX: Float = SGD.currentSpanX
                val spanY: Float = SGD.currentSpanY

                val newWidth: Float = lastSpanX / spanX * mCurrentViewport.width()
                val newHeight: Float = lastSpanY / spanY * mCurrentViewport.height()

                val focusX: Float = SGD.focusX
                val focusY: Float = SGD.focusY

                mContentRect?.apply {
                    mCurrentViewport.set(
                            viewportFocus.x - newWidth * (focusX - left) / width(),
                            viewportFocus.y - newHeight * (bottom - focusY) / height(),
                            0f,
                            0f
                    )
                }
                mCurrentViewport.right = mCurrentViewport.left + newWidth
                mCurrentViewport.bottom = mCurrentViewport.top + newHeight

                // Invalidates the View to update the display.
                ViewCompat.postInvalidateOnAnimation(viewSV!!)

                lastSpanX = spanX
                lastSpanY = spanY
                return true
            }
        })*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT) {
             if(resultCode == RESULT_OK) {
                grupaIzTmpa = data?.getStringExtra("imeGrupe")
             }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        //proverava se koja je grupa odabrana, i da li je grupa iz prethodne ili pomocne aktivnosti
        val odabranaGrupa: String? = if(grupaIzTmpa == "")
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

    /*override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        super.dispatchTouchEvent(ev)
        SGD.onTouchEvent(ev)
        GD.onTouchEvent(ev)
        return GD.onTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return SGD.onTouchEvent(event)
                || GD.onTouchEvent(event)
                || super.onTouchEvent(event)
    }*/

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
            tv.setOnClickListener {
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
            }
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
