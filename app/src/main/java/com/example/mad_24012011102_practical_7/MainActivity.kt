package com.example.mad_24012011102_practical_7

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_24012011102_practical_7.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val API_URL = "https://api.json-generator.com/templates/5rDXHcbgpo93/data"
        private const val API_TOKEN = "d7wrtfqywyhu7y2bcbsz3cgjpbfisuhnmbibvgvf"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: PersonAdapter
    private val persons = mutableListOf<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        persons.addAll(dbHelper.allPersons)
        adapter = PersonAdapter(persons) { position -> deletePerson(position) }
        binding.recyclerViewPersons.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPersons.adapter = adapter

        binding.fabRefresh.setOnClickListener { fetchPersonsFromApi() }

        if (persons.isEmpty()) {
            fetchPersonsFromApi()
        }
    }

    private fun deletePerson(position: Int) {
        val person = persons[position]
        dbHelper.deletePerson(person)
        persons.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    private fun fetchPersonsFromApi() {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = HttpRequest().makeServiceCall(API_URL, API_TOKEN)
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    if (json != null) {
                        val fetched = parsePersonsFromJson(json)
                        fetched.forEach { dbHelper.insertPerson(it) }
                        persons.clear()
                        persons.addAll(dbHelper.allPersons)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@MainActivity, "Could not fetch contacts", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "fetchPersonsFromApi: ${e.message}")
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parsePersonsFromJson(json: String): List<Person> {
        val result = mutableListOf<Person>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val profile = obj.getJSONObject("profile")
            val location = profile.getJSONObject("location")
            result.add(
                Person(
                    id = obj.getString("id"),
                    name = profile.getString("name"),
                    emailId = obj.getString("email"),
                    phoneNo = obj.getString("phone"),
                    address = profile.getString("address"),
                    latitude = location.getDouble("lat"),
                    longitude = location.getDouble("long")
                )
            )
        }
        return result
    }
}
