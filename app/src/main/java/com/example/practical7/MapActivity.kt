package com.example.practical7

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.practical7.Person

class MapActivity : AppCompatActivity() {

    private lateinit var titleText: TextView
    private lateinit var latText: TextView
    private lateinit var longText: TextView
    private lateinit var openMapsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        titleText = findViewById(R.id.titleText)
        latText = findViewById(R.id.latText)
        longText = findViewById(R.id.longText)
        openMapsBtn = findViewById(R.id.openMapsBtn)

        val obj = intent.getSerializableExtra("Object") as? Person
        if (obj != null) {
            titleText.text = obj.name
            latText.text = "Lat: ${obj.latitude}"
            longText.text = "Long: ${obj.longitude}"

            openMapsBtn.setOnClickListener {
                // open Google Maps via geo URI
                val geo = Uri.parse("geo:${obj.latitude},${obj.longitude}?q=${obj.latitude},${obj.longitude}(${Uri.encode(obj.name)})")
                val mapIntent = Intent(Intent.ACTION_VIEW, geo)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    // fallback to browser
                    val gmm = Uri.parse("https://www.google.com/maps/search/?api=1&query=${obj.latitude},${obj.longitude}")
                    startActivity(Intent(Intent.ACTION_VIEW, gmm))
                }
            }
        } else {
            titleText.text = "No object passed"
        }
    }
}
