package com.appdtt.dtt
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import android.content.DialogInterface
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.appdtt.dtt.DbAdapter.MyListAdapter
import com.appdtt.dtt.dbHandler.DatabaseHandler
import com.appdtt.dtt.dbModel.EmpModelClass


class DbSqLite : AppCompatActivity() {
    var listView :ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db_sq_lite)

        listView = findViewById(R.id.listView)
    }
    //method for saving records in database
    fun saveRecord(view: View){
        val u_id = findViewById<EditText>(R.id.u_id)
        val u_name = findViewById<EditText>(R.id.u_name)
        val u_email = findViewById<EditText>(R.id.u_email)
        val id = u_id.text.toString()
        val name = u_name.text.toString()
        val email = u_email.text.toString()
        val databaseHandler = DatabaseHandler(this)
        if(id.isNotBlank() && name.isNotBlank() && email.isNotBlank()){
            val status = databaseHandler.addEmployee(EmpModelClass(Integer.parseInt(id),name, email))
            if(status > -1){
                Toast.makeText(applicationContext,"record save",Toast.LENGTH_LONG).show()
                u_id.text.clear()
                u_name.text.clear()
                u_email.text.clear()
            }

        }else{
            Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
        }

    }
    //method for read records from database in ListView
    fun viewRecord(view: View){
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee()
        val empArrayId = Array<String>(emp.size){"0"}
        val empArrayName = Array<String>(emp.size){"null"}
        val empArrayEmail = Array<String>(emp.size){"null"}
        var index = 0
        for(e in emp){
            empArrayId[index] = e.userId.toString()
            empArrayName[index] = e.userName
            empArrayEmail[index] = e.userEmail
            index++
        }
        //creating custom ArrayAdapter
        val myListAdapter = MyListAdapter(this,empArrayId,empArrayName,empArrayEmail)
        listView!!.adapter = myListAdapter
    }
    //method for updating records based on user id
    fun updateRecord(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val edtId = dialogView.findViewById(R.id.updateId) as EditText
        val edtName = dialogView.findViewById(R.id.updateName) as EditText
        val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText

        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setMessage("Enter data below")
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateId = edtId.text.toString()
            val updateName = edtName.text.toString()
            val updateEmail = edtEmail.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler = DatabaseHandler(this)
            if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!=""){
                //calling the updateEmployee method of DatabaseHandler class to update record
                val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(updateId),updateName, updateEmail))
                if(status > -1){
                    Toast.makeText(applicationContext,"record update",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            //pass
        }
        val b = dialogBuilder.create()
        b.show()
    }
    //method for deleting records based on id
    @SuppressLint("MissingInflatedId")
    fun deleteRecord(view: View){
        //creating AlertDialog for taking user id
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val dltId = dialogView.findViewById(R.id.deleteId) as EditText
        dialogBuilder.setTitle("Delete Record")
        dialogBuilder.setMessage("Enter id below")
        dialogBuilder.setPositiveButton("Delete") { _, _ ->

            val deleteId = dltId.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler = DatabaseHandler(this)
            if (deleteId.isNotBlank()) {
                //calling the deleteEmployee method of DatabaseHandler class to delete record
                val status = databaseHandler.deleteEmployee(
                    EmpModelClass(
                        Integer.parseInt(deleteId),
                        "",
                        ""
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "record deleted", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "id or name or email cannot be blank", Toast.LENGTH_LONG).show()
            }

        }
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
}