package com.mobdeve.tighee.emailapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.tighee.emailapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Our static storage for the emails
    companion object {
        private val data = ArrayList<Email>()
    }

    // RecyclerView components; We will need these late; hence, why we're saving them here
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter

    // If success, then add email. Otherwise, don't do anything.
    private val newEmailResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        // Check to see if the result returned is appropriate (i.e. OK)
        if (result.resultCode == RESULT_OK) {
            /* TODO:
             *      1. Extract new email information from intent coming from NewEmailActivity,
             *      2. Create a new Email,
             *      3. Add Email object to data,
             *      4. Notify adapter of change [explore notifyItemInserted()]
             *      NOTE: Make sure to place new email at top of list.
             * */
            val receiver : String = result.data?.getStringExtra(NewEmailActivity.NEW_RECEIVER_KEY).toString()
            val subject : String = result.data?.getStringExtra(NewEmailActivity.NEW_SUBJECT_KEY).toString()
            val body : String = result.data?.getStringExtra(NewEmailActivity.NEW_BODY_KEY).toString()

            val newEmail = Email(receiver, subject, body)

            MainActivity.data.add(0, newEmail)
            this.myAdapter.notifyItemInserted(0)
        }
    }

    // If result OK (in terms of deleting), then add email at position. Otherwise, don't do anything.
    // This launcher is passed into myAdapter so we can launch from an itemView click
    private val emailDetailsResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        // Check to see if the result returned is appropriate (i.e. OK)
        if (result.resultCode == RESULT_OK) {
            /* TODO:
             *      1. Get position sent back from EmailDetailsActivity
             *      2. Delete data point from ArrayList
             *      3. Notify adapter of change [explore notifyItemRemoved()]
             * */
            val position = result.data?.getIntExtra(EmailDetailsActivity.POSITION_KEY, 0)!!
            MainActivity.data.removeAt(position)
            this.myAdapter.notifyItemRemoved(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding for the MainActivity
        val viewBinding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Logic for creating a new email button
        viewBinding.newBtn.setOnClickListener(View.OnClickListener {
                /*  TODO:
                 *      1. Declare an intent heading to NewEmailActivity
                 *      2. Using the appropriate ActivityResultLauncher, launch using your intent
                 * */
            val intent = Intent(this@MainActivity, NewEmailActivity::class.java)
            this.newEmailResultLauncher.launch(intent)
        })

        // RecyclerView setup; Note how MyAdapter has emailDetailsResultLauncher
        this.recyclerView = viewBinding.recyclerView
        this.myAdapter = MyAdapter(data, emailDetailsResultLauncher)
        this.recyclerView.adapter = myAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}