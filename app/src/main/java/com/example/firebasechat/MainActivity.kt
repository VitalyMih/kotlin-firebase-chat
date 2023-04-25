package com.example.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.firebasechat.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
   private  lateinit var databaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseRef = Firebase.database.getReference("messages")
        dataChangeListener()
    }

    private fun dataChangeListener() = with(binding) {
        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString()
                messagesList.append("\n")
                messagesList.append(value)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun onSendMessageClick(view: View) = with(binding) {
        databaseRef.setValue(messageText.text.toString())
    }
}