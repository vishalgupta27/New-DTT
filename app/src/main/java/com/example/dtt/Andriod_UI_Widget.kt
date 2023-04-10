package com.example.dtt

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.albums.utils.Utility

class Andriod_UI_Widget : AppCompatActivity() {

    private val items = listOf("Male", "Female", "TransGender")


    @SuppressLint("MissingInflatedId", "WrongViewCast", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_andriod_ui_widget)

        //1. For Auto Completed TextView code Start
        // Data for auto-completion
        val data = listOf("Java", "Kotlin", "XML", "Android")
        // Set up the AutoCompleteTextView
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, data)
        autoCompleteTextView.setAdapter(adapter)

        // Optional: Set other properties for AutoCompleteTextView
        autoCompleteTextView.threshold = 1 // set the minimum number of characters to 1
        // set the dropdown width to match_parent
        autoCompleteTextView.dropDownWidth = ViewGroup.LayoutParams.MATCH_PARENT
        //1. For Auto Completed TextView code End


        //2.For Spinner Code Start
        // Set up the spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapters = ArrayAdapter(this,R.layout.spinner_item_layout,R.id.spinner_item_text, data)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner.adapter = adapters

        // Set spinner width to match_parent
        spinner.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        // Set up item selected listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = data[position]
                Toast.makeText(
                    this@Andriod_UI_Widget,
                    "Selected Item: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        } //2.For Spinner Code End


        //3. Set up the button click listener to show the AlertDialog
        val button = findViewById<Button>(R.id.butoon)
        button.setOnClickListener {
            Utility.showAlertDialog(this)
        }  //3. Set up the button click listener to show the AlertDialog End Code


        //4. Set up the toggle button
        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Do something when toggle button is checked
                Toast.makeText(this, "Toggle button is ON", Toast.LENGTH_SHORT).show()
            } else {
                // Do something when toggle button is unchecked
                Toast.makeText(this, "Toggle button is OFF", Toast.LENGTH_SHORT).show()
            }
        }//End Code


        // Set up the checkbox
        val pizza = findViewById<CheckBox>(R.id.pizza)
        val burger = findViewById<CheckBox>(R.id.burger)
        val order = findViewById<Button>(R.id.order)
        order.setOnClickListener { view ->
            var totalAmount = 0
            val result = StringBuilder()
            result.append("Selected Items:")
            if (pizza.isChecked) {
                result.append(" Pizza 100Rs")
                totalAmount += 100
            }
            if (burger.isChecked) {
                result.append(" , Burger 120Rs")
                totalAmount += 120
            }
            result.append("  Total: $totalAmount Rs")
            // Displaying the message on the toast
            Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_LONG).show()
        } //end code


        //For RadioButton Code Setup
        val radioGroup = findViewById<RadioGroup>(R.id.RadioGroup)
        val male = findViewById<RadioButton>(R.id.Male)
        val female = findViewById<RadioButton>(R.id.Female)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (male.isChecked){
                Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show()
            }
            else if (female.isChecked){
                Toast.makeText(this,"Female",Toast.LENGTH_SHORT).show()
            }
        }

        //For Dynamic Radio Button

        // Create a RadioGroup
        val radioGroups = RadioGroup(this)
        radioGroup.orientation = RadioGroup.VERTICAL

        // Create dynamic radio buttons
        val radioGroup2 = findViewById<RadioGroup>(R.id.radioGroup2)
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Dynamically create radio buttons
        for (item in items) {
            val radioButton = RadioButton(this)
            radioButton.text = item
            radioButton.id = View.generateViewId() // Set a unique ID for each radio button
            radioGroup2.addView(radioButton)

            // Optionally, set other properties for the radio button, such as layout parameters
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            radioButton.layoutParams = layoutParams
        }

        // Set a default selection
        radioGroup.check(radioGroup2.getChildAt(0).id) // Check the first radio button by default

        // Set a listener for submit button
        submitButton.setOnClickListener {
            val checkedId = radioGroup2.checkedRadioButtonId
            if (checkedId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                val selectedItem = selectedRadioButton.text.toString()
                // Do something with the selected item
                // For example, show a toast with the selected item
                Toast.makeText(this, "Selected item: $selectedItem", Toast.LENGTH_SHORT).show()
            } else {
                // No radio button selected
                Toast.makeText(this, "Please select an item", Toast.LENGTH_SHORT).show()
            }
        } // end



    }


    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Alert Dialog")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setMessage("This is an example of AlertDialog in Kotlin.")
        alertDialogBuilder.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialog, which ->
                // Do something when OK button is clicked
                Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            })
        alertDialogBuilder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which ->
                // Do something when Cancel button is clicked
                dialog.dismiss()
            })
        alertDialogBuilder.show()
    }
}