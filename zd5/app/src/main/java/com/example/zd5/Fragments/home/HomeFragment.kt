package com.example.zd5.Fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.TextClassification
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.privacysandbox.tools.core.model.Method
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.zd5.DBUniversity.DBHelperUniversites
import com.example.zd5.DBUniversity.University
import com.example.zd5.DBUniversity.UniversityAdapter
import com.example.zd5.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {
    lateinit var recyclerView: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val root: View = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = root.findViewById(R.id.list_entries)

        var url = "http://universities.hipolabs.com/search?country=Belarus"
        val queue = Volley.newRequestQueue(requireContext())
        val stringR = StringRequest(Request.Method.GET,
            url,
            { response ->
                val obj = JSONObject(response)
                val title = obj.getString("name")
                val web = obj.getString("web_pages")
                var country = obj.getString("country")
                val university = University(title,web,country)
                val db = DBHelperUniversites (requireContext())
                db.addUniversity(university)
            },
            {
                Log.d("Log", "Volley error: $it")
            }
        )
        queue.add(stringR)
        setAPIInfromation()
        return root
    }
    fun fetchData() {
        var url = "http://universities.hipolabs.com/search?country=Belarus"

    }
    fun getAPIInformation()
    {
    }

    fun setAPIInfromation()
    {
        var helper = DBHelperUniversites(requireContext())
        var array = helper.readAllUniversities()
        recyclerView.removeAllViews()
        array.forEach {
            var uni = TextView(requireContext())
            uni.textSize = 35F
            uni.text = "${it.title.toString()}\n${it.web.toString()}\n${it.country.toString()}"
            recyclerView.addView(uni)
        }
    }
}