package com.example.firebasechat

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.firebasechat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        databaseRef = Firebase.database.getReference("messages")
        setUpAvatar()
        dataChangeListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out) {
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
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

    private fun setUpAvatar() {
        val actionBar = supportActionBar
        Thread {
            val bitMapImage = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val avatarImage = BitmapDrawable(resources, bitMapImage)
            runOnUiThread {
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.setHomeAsUpIndicator(avatarImage)
                actionBar?.title = auth.currentUser?.displayName
            }
        }.start()
    }
}