package com.example.zd5.Fragments.dashboard

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.zd5.DBCurrentUser.DBHelperCurrentUser
import com.example.zd5.DBSpecialty.DBHelperSpecialty
import com.example.zd5.DBStudent.DBContactStudent
import com.example.zd5.DBStudent.DBHelperStudent
import com.example.zd5.DBStudent.Student
import com.example.zd5.DBTeacher.DBHelperTeacher
import com.example.zd5.DBUser.DBHelperUser
import com.example.zd5.R
import kotlin.math.log

class DashboardFragment : Fragment() {
    lateinit var list: ListView
    val arrayItemStudent: ArrayList<String> = ArrayList()
    val arrayItemSpect: ArrayList<String> = ArrayList()
    lateinit var adapter: ArrayAdapter<String>
    lateinit var spinnerSpec: Spinner
    lateinit var login: String
    lateinit var role: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val root: View = inflater.inflate(R.layout.fragment_dashboard,container,false)
        spinnerSpec = root.findViewById(R.id.spinnerSort)
        list = root.findViewById(R.id.list_entries)
        definitionRole()
        if (role == "admin")
        {
            updateSpecSpinner()

            spinnerSpec.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    setInfoFromDB(selectedItem)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            list.onItemClickListener = object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>, view: View,
                    position: Int, id: Long,
                )
                {
                    val itemValue = list.getItemAtPosition(position) as String
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("You choosed: $itemValue")
                        .setCancelable(true)
                        .setPositiveButton("Delete")
                        { _, _ ->
                            var splitInfoStudent = itemValue.split("-")
                            var loginSt = splitInfoStudent[0].toString()
                            var fullname = splitInfoStudent[1].toString()
                            delStudent(fullname, loginSt, spinnerSpec.selectedItem.toString())
                        }
                        .setNegativeButton("Information")
                        {
                            _, _->
                            var splitInfoStudent = itemValue.split("-")
                            var loginSt = splitInfoStudent[0].toString()
                            var helperStudent = DBHelperStudent(requireContext())
                            var array = helperStudent.getStudentLogin("${loginSt}")
                            var stringMessage: String = ""
                            array.forEach {
                                stringMessage = "${it.fullname}\n${it.spec}\n${it.date}\n${it.email}\n${it.password}\n${it.point}"
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
        else if (role == "student")
        {
            spinnerSpec.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    setInfoSpecCurrentUserStudent(selectedItem)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            getStudentSpec()
        }
        else
        {
            spinnerSpec.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    setInfoSpecCurrentUserTeacher(selectedItem)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            getTeacherSpec()
        }
        return root
    }

    fun setInfoSpecCurrentUserStudent(spec: String)
    {
        arrayItemStudent.clear()
        var helperStudent = DBHelperStudent(requireContext())
        var array = helperStudent.getStudentSpec("$spec")
        array.forEach {
            var fullname = "${it.fullname}"
            arrayItemStudent.add(fullname.toString())
        }
        adapter = ArrayAdapter( requireContext(), android.R.layout.simple_list_item_1 ,arrayItemStudent)
        list.adapter = adapter
    }

    fun setInfoSpecCurrentUserTeacher(spec: String)
    {
        arrayItemStudent.clear()
        var helperStudent = DBHelperTeacher(requireContext())
        var array = helperStudent.getAllTeacherSpec("$spec")
        array.forEach {
            var fullname = "${it.fullname}"
            arrayItemStudent.add(fullname.toString())
        }
        adapter = ArrayAdapter( requireContext(), android.R.layout.simple_list_item_1 ,arrayItemStudent)
        list.adapter = adapter
    }
    fun getTeacherSpec()
    {
        var helperStudent = DBHelperTeacher(requireContext())
        var array = helperStudent.getAllTeacherLogin("${login}")
        array.forEach{
            var spec = "${it.spec}"
            arrayItemSpect.add(spec.toString())
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayItemSpect)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSpec.adapter = adapter

    }
    fun getStudentSpec()
    {
        var helperStudent = DBHelperStudent(requireContext())
        var array = helperStudent.getStudentLogin("${login}")
        array.forEach{
            var spec = "${it.spec}"
            arrayItemSpect.add(spec.toString())
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayItemSpect)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSpec.adapter = adapter

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

    fun delStudent(fullname: String, login: String, spec: String)
    {

        var helper = DBHelperStudent(requireContext())
        var result = helper.deleteStudent(login, spec)
        if (result)
        {
            var toast = Toast.makeText(requireContext(), R.string.deleted, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun updateSpecSpinner()
    {
        var helper = DBHelperSpecialty(requireContext())
        var array = helper.readAllSpec()

        array.forEach {
            var string = "${it.title.toString()}"
            arrayItemSpect.add(string.toString())
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayItemSpect)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSpec.adapter = adapter
    }

    fun setInfoFromDB(spec: String)
    {
        arrayItemStudent.clear()
        var helper = DBHelperStudent(requireContext())
        var itemArray: ArrayList<String>
        var array = helper.readAllStudents(spec)

        array.forEach {
            var string = "${it.login.toString()}-${it.fullname.toString()}"
            arrayItemStudent.add(string.toString())
        }

        adapter = ArrayAdapter( requireContext(), android.R.layout.simple_list_item_1 ,arrayItemStudent)
        list.adapter = adapter
    }
}