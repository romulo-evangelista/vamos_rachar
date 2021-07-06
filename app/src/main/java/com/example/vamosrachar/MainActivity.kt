package com.example.vamosrachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat
import java.util.*


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    lateinit var resultText: TextView
    lateinit var valueEditText: EditText
    lateinit var personEditText: EditText
    lateinit var share: FloatingActionButton
    lateinit var ttsBtn: FloatingActionButton
    lateinit var tts: TextToSpeech

    var value: Double = 0.0
    var person: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // result
        resultText = findViewById<TextView>(R.id.result)

        // value
        valueEditText = findViewById<EditText>(R.id.valueEditText)
        valueEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                try {
                    value = s.toString().toDouble()
                    if(person !== 0) {
                        var res: Double = (value/person)
                        var df: DecimalFormat = DecimalFormat("#.00")
                        resultText.text = "R$ " + df.format(res)
                    }
                } catch(e: Exception) {
                    resultText.text = ("R$ 0,00")
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        // per person
        personEditText = findViewById<EditText>(R.id.editTextTextPersonName2)
        personEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                try {
                    person = s.toString().toInt()
                    var res: Double = (value/person)
                    var df: DecimalFormat = DecimalFormat("#.00")
                    resultText.text = "R$ " + df.format(res)
                } catch(e: Exception) {
                    resultText.text = ("R$ 0,00")
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        // share
        share = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        share.setOnClickListener { view ->
            var res: String = resultText.text.toString()
            var intent: Intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plan")
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "O valor da conta por pessoa é: $res")
            startActivity(Intent.createChooser(intent, "Escolha um app para compartilhar!"))
        }

        // TTS
        ttsBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        ttsBtn!!.isEnabled = false;
        tts = TextToSpeech(this, this)

        ttsBtn!!.setOnClickListener { speakOut() }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale("pt_BR"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","A linguagem especificada não é suportada!")
            } else {
                ttsBtn!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Falha na inicialização!")
        }

    }

    private fun speakOut() {
        var res: String = resultText.text.toString()
        val text: String = "O valor da conta por pessoa é: $res"
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}

