package com.example.firebasechat

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val adapter = MessagesAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        databaseRef = Firebase.database.getReference("messages")
        setUpAvatar()
        initMessagesList()
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
                val messagesList = ArrayList<Message>()
                snapshot.children.forEach { message ->
                    val value = message.getValue(Message::class.java)
                    if (value !== null) { messagesList.add(value) }
                }
                adapter.submitList(messagesList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun onSendMessageClick(view: View) = with(binding) {
        val message = Message(auth.currentUser?.displayName, messageText.text.toString())
        databaseRef.child(databaseRef.push().key ?: "01").setValue(message)
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

    private fun initMessagesList() = with(binding) {
        messagesList.layoutManager = LinearLayoutManager(this@MainActivity)
        messagesList.adapter = adapter
    }
}