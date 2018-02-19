/**
 * Copyright Google Inc. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.udacity.friendlychat.Message.view

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.udacity.friendlychat.BR
import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage
import com.google.firebase.udacity.friendlychat.Message.viewmodel.MessageViewModel
import com.google.firebase.udacity.friendlychat.MessageContract
import com.google.firebase.udacity.friendlychat.R
import com.google.firebase.udacity.friendlychat.databinding.ActivityMainBinding
import com.google.firebase.udacity.friendlychat.root.App
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), MessageContract.View {

    lateinit var mMessageAdapter: MessageAdapter


    lateinit var messageViewModel: MessageViewModel

    private var mChildEventListener: ChildEventListener? = null
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null
    var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.component.inject(this)

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setVariable(BR.activity, this)

        val providers = Arrays.asList<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
        )

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Toast.makeText(this, "You're now signed in. Welcome to FriendlyChat.", Toast.LENGTH_SHORT).show()
                onSignedInInicialize(user.displayName)
            } else {
                onSignedOutCleanup()
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setProviders(providers)
                        .build(), RC_SIGN_IN)
            }
        }

        messageViewModel.fecthConfig()
        initialize()
    }

    override fun onStart() {
        super.onStart()

        (App.component).inject(this)
    }

    private fun initialize(){
        val friendlyMessages = ArrayList<FriendlyMessage>()
        mMessageAdapter = MessageAdapter(this, R.layout.item_message, friendlyMessages)
        messageListView!!.adapter = mMessageAdapter

        progressBar!!.visibility = ProgressBar.INVISIBLE

        messageEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                buttonState(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        messageEditText!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT))
    }

    private fun onSignedInInicialize(username: String?) {
        messageViewModel.mUsername = username!!
        attachDatabaseReadListener()
    }

    private fun attachDatabaseReadListener() {
            mChildEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    mMessageAdapter.add(dataSnapshot.getValue(FriendlyMessage::class.java))
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            messageViewModel.addChildListener(mChildEventListener as ChildEventListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                AuthUI.getInstance().signOut(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        messageViewModel.mFirebaseAuth.addAuthStateListener(mAuthStateListener!!)
    }

    override fun onPause() {
        super.onPause()
        if (mAuthStateListener != null)
            messageViewModel.mFirebaseAuth.removeAuthStateListener(mAuthStateListener!!)
        mMessageAdapter.clear()
        detatchDatabaseReadListener()
    }

    private fun onSignedOutCleanup() {
        messageViewModel.mUsername = ANONYMOUS
        mMessageAdapter.clear()
        if(mChildEventListener != null)
        detatchDatabaseReadListener()
    }

    private fun detatchDatabaseReadListener() {
        messageViewModel.detachDatabaseReadListener()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
                messageViewModel.imageUrl = data.data
                messageViewModel.sendMessage(message)
        }
    }

    override fun openPhotoPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(Intent.createChooser(intent, "Complete action with"), RC_PHOTO_PICKER)
    }

    override fun buttonState(charSequence: CharSequence) {
        when(charSequence.toString().isNotEmpty()) {
            false -> sendButton!!.isEnabled = false
            true -> sendButton!!.isEnabled = true
        }
    }

    override fun updateMessages(friendlyMessage: FriendlyMessage) {
        mMessageAdapter.add(friendlyMessage)
    }

    override fun sendMessage() {
        messageViewModel.sendMessage(message)
    }

    override fun applyRetrievedConfiguration() {
        val newLenght = messageViewModel.getRetrievedConfiguration()
        messageEditText!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(newLenght.toInt()))
        Log.i("newLenght", "new lenght is = " + newLenght)
    }

    companion object {
        val ANONYMOUS = "anonymous"
        val DEFAULT_MSG_LENGTH_LIMIT = 1000
        val RC_SIGN_IN = 1
        val RC_PHOTO_PICKER = 2
    }
}
