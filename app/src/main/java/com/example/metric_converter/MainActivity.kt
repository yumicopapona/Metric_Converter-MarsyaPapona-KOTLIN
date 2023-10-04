package com.example.metric_converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import kotlin.math.abs
import android.widget.Toast
import com.example.metric_converter.R

class MainActivity : AppCompatActivity() {

    //Deklarasi id xml ke var kotlin
    private lateinit var spMetrics: Spinner
    private lateinit var spOriginal: Spinner
    private lateinit var spConvert: Spinner
    private lateinit var inputValue: EditText
    private lateinit var resultText: TextView

    //Fungsi hitung satuan + deteksi satuan yg dipilih di spinner 2 dan spinner 3
    fun calculateResult(input: Double, index: Int, index2: Int, index3: Int) {
        var index4: Int = 0
        var result: Double = input
        if(index == 1 || index == 2 || index == 3 || index == 4) {
            index4 = index2 - index3
            println(index4)
            if(index4 > 0) {
                for(x in 1..index4)
                    result *= 10
                resultText.setText("$result")
            } else if(index4 < 0) {
                index4 = abs(index4)
                for(x in 1..index4)
                    result /= 10
                resultText.setText("$result")

            } else if(index4 == 0) {
                resultText.setText("$result")
            }
        } else if(index == 5) { //Hitung Suhu
            if((index2 == 0 && index3 == 0) || (index2 == 1 && index3 == 1) || (index2 == 2 && index3 == 2))
                println(input)
            else if(index2 == 0 && index3 == 1)
                result = (input * 9 / 5) + 32
            else if(index2 == 0 && index3 == 2)
                result = input + 273.15
            else if(index2 == 1 && index3 == 0)
                result = (input - 32) * 5 / 9
            else if(index2 == 1 && index3 == 2)
                result = ((input - 32) * 5 / 9) + 273.15
            else if(index2 == 2 && index3 == 0)
                result = input - 273.15
            else if(index2 == 2 && index3 == 1)
                result = ((input - 273.15) * 9 / 5) + 32
            resultText.setText("$result")
            // Celcius -> Fahrenheit = (0 °C × 9/5) + 32
            // Fahrenheit -> Celcius =  (0 °F − 32) × 5/9
            // Celcius -> Kelvin = 0 °C + 273,15
            // Kelvin -> Celcius = 0 K − 273,15
            // Kelvin -> Fahrenheit = (0 K − 273,15) × 9/5 + 32
            // Fahrenheit -> Kelvin = (0 °F − 32) × 5/9 + 273,15
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Mencari teks input + teks hasil di xml
        inputValue = findViewById(R.id.getInputValue)
        resultText = findViewById(R.id.resultText)

        //Ubah list spinner 2 3 mengikuti sesuai dengan metrik yg dipilih spinner 1
        val lengths = listOf("Milimeter", "Centimeter", "Desimeter", "Meter", "Dekameter", "Hektometer", "Kilometer")
        val mass = listOf("Miligram", "Centigram", "Desigram", "Gram", "Dekagram", "Hektogram", "Kilogram")
        val time = listOf("Milisekon", "Centisekon", "Desisekon", "Sekon", "Dekasekon", "Hektosekon", "Kilosekon")
        val electric_current = listOf("Miliampere", "Centiampere", "Desiampere", "Ampere", "Dekaampere", "Hektoampere", "Kiloampere")
        val temperature = listOf("Celcius", "Fahrenheit", "Kelvin")

        val adapter_lengths = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, lengths)
        val adapter_mass = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, mass)
        val adapter_time = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, time)
        val adapter_electric_current = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, electric_current)
        val adapter_temperature = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, temperature)

        var indexMetric: Int = 0
        var indexMetric2: Int = 0
        var indexMetric3: Int = 0

        var getInputNum: Double = 0.0

        //Mencari id spinner ke 2 satuan di xml + disable sementara
        spOriginal = findViewById(R.id.spOriginal)
        spOriginal.isEnabled = false

        //Mencari id spinner ke 3 satuan di xml + disable sementara
        spConvert = findViewById(R.id.spConvert)
        spConvert.isEnabled = false

        //Mencari id spinner ke 1 metrik di xml + melakukan deteksi posisi
        spMetrics = findViewById(R.id.spMetrics)
        spMetrics.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != 0) {
                    spOriginal.isEnabled = true
                    spConvert.isEnabled = true
                }
                if(position == 1) {
                    spOriginal.adapter = adapter_lengths
                    spConvert.adapter = adapter_lengths
                    indexMetric = position
                } else if(position == 2) {
                    spOriginal.adapter = adapter_mass
                    spConvert.adapter = adapter_mass
                    indexMetric = position
                } else if(position == 3) {
                    spOriginal.adapter = adapter_time
                    spConvert.adapter = adapter_time
                    indexMetric = position
                } else if(position == 4) {
                    spOriginal.adapter = adapter_electric_current
                    spConvert.adapter = adapter_electric_current
                    indexMetric = position
                } else if (position == 5) {
                    spOriginal.adapter = adapter_temperature
                    spConvert.adapter = adapter_temperature
                    indexMetric = position
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        spOriginal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                indexMetric2 = position
                if(getInputNum != 0.0 && indexMetric3 != 0)
                    calculateResult(getInputNum, indexMetric, indexMetric2, indexMetric3)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spConvert.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                indexMetric3 = position
                if(getInputNum != 0.0 && indexMetric2 != 0)
                    calculateResult(getInputNum, indexMetric, indexMetric2, indexMetric3)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        //Membuat event listener di teks input onchange => dari charseq => string => double => fungsi menghitung
        inputValue.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var getString: String = ""+s
                var getNum: Double = getString.toDouble()
                getInputNum = getNum
                calculateResult(getNum, indexMetric, indexMetric2, indexMetric3)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })



    }
}