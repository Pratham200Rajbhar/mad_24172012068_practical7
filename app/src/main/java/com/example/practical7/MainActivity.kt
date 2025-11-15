package com.example.practical7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: PersonAdapter
    private val tag = "MainActivity"

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabRefresh: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        fabRefresh = findViewById(R.id.fabRefresh)
        dbHelper = DatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PersonAdapter(mutableListOf())
        recyclerView.adapter = adapter

        loadFromDb()

        fabRefresh.setOnClickListener {
            fetchAndSaveData()
        }
    }

    private fun loadFromDb() {
        val list = dbHelper.allPersons
        adapter.updateItems(list)
    }

    private fun fetchAndSaveData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = "https://api.json-generator.com/templates/x1QbX-JkAS57/data"
                val token = "mm53tej3fypzbc4exljs9adhqmo4oj6eqkqg5656"

                val result = HttpRequest.makeServiceCall(url, token)

                if (!result.isNullOrEmpty()) {
                    parseAndSave(result)
                    withContext(Dispatchers.Main) {
                        loadFromDb()
                        Toast.makeText(this@MainActivity, "Data refreshed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "No data from server", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("MainActivity", "Exception fetching data: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun parseAndSave(jsonStr: String) {
        try {
            val arr = JSONArray(jsonStr)
            val db = dbHelper
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val person = Person()
                person.id = obj.getString("id")
                person.emailId = obj.optString("email", "")
                person.phoneNo = obj.optString("phone", "")
                if (obj.has("profile")) {
                    val prof = obj.getJSONObject("profile")
                    person.name = prof.optString("name", "")
                    person.address = prof.optString("address", "")
                }
                db.insertPerson(person)
            }
        } catch (e: Exception) {
            Log.e(tag, "parseAndSave error: ${e.message}")
        }
    }
}
