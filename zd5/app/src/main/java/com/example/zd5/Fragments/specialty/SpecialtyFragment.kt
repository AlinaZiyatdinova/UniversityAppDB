package com.example.zd5.Fragments.specialty

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import com.example.zd5.Activity.AddSpecialtyActivity
import com.example.zd5.Activity.AddStudentActivity
import com.example.zd5.Activity.MainActivity
import com.example.zd5.DBCurrentUser.DBHelperCurrentUser
import com.example.zd5.DBSpecialty.DBHelperSpecialty
import com.example.zd5.DBStudent.DBHelperStudent
import com.example.zd5.DBTeacher.DBHelperTeacher
import com.example.zd5.DBUniversity.DBHelperUniversites
import com.example.zd5.R

class SpecialtyFragment : Fragment() {
    lateinit var list: ListView
    val arrayItem: ArrayList<String> = ArrayList()
    lateinit var adapter: ArrayAdapter<String>
    var login: String = ""
    var role: String =""
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_specialty,container,false)
        list = root.findViewById(R.id.list_entries)
        setInfoFromDB()
        definitionRole()
        if(role == "admin") {
            list.onItemClickListener = object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>, view: View,
                    position: Int, id: Long,
                ) {
                    val itemValue = list.getItemAtPosition(position) as String
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("You choosed: $itemValue")
                        .setCancelable(true)
                        .setPositiveButton("Delete")
                        { _, _ ->
                            var helperStudent = DBHelperStudent(requireContext())
                            var result = helperStudent.getSpec("$itemValue")
                            if (!result) {

                                var helperTeacher = DBHelperTeacher(requireContext())
                                var result2 = helperTeacher.getSpec("$itemValue")
                                if (!result2) {
                                    delItemSpec(itemValue.trim().toString())
                                }
                                else
                                {
                                    var toast = Toast.makeText(requireContext(),
                                        R.string.nodelete,
                                        Toast.LENGTH_SHORT)
                                    toast.show()
                                }
                            }
                        }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
        else
        {
            list.onItemClickListener = object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>, view: View,
                    position: Int, id: Long,
                ) {
                    val itemValue = list.getItemAtPosition(position) as String
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("You choosed: $itemValue")
                        .setCancelable(true)
                        .setPositiveButton("Information")
                        { _, _ ->
                            var helper = DBHelperSpecialty(requireContext())
                            var array = helper.readAllSpecTitle("$itemValue")
                            var stringMessage: String = ""
                            array.forEach {
                                stringMessage = "Name: ${it.title}\nType: ${it.type}\nLoad: ${it.load}"
                            }
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Information")
                            builder.setMessage("${stringMessage}")
                            builder.setPositiveButton("OK") { dialog, which ->
                            }
                            val dialog = builder.create()
                            dialog.show()
                        }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
        return root
    }

    fun definitionRole()
    {
        var helperCurrentUser = DBHelperCurrentUser(requireContext())
        var array = helperCurrentUser.getCurrentUser()
        array.forEach {
            login = "${it.login.toString()}"
            role = "${it.role.toString()}"
        }
    }
    fun delItemSpec(title: String)
    {
        var helper = DBHelperSpecialty(requireContext())
        var result = helper.deleteSpec(title)
        if (result)
        {
            var toast = Toast.makeText(requireContext(), R.string.deleted, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun setInfoFromDB()
    {
        var helper = DBHelperSpecialty(requireContext())
        var itemArray: ArrayList<String>
        var array = helper.readAllSpec()

        array.forEach {
            var string = "${it.title.toString()}"
            arrayItem.add(string.toString())
        }

        adapter = ArrayAdapter( requireContext(), android.R.layout.simple_list_item_1 ,arrayItem)
        list.adapter = adapter
    }

}