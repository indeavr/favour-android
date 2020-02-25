package com.example.allfavour.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allfavour.R
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.chat_activity.*
import androidx.core.content.ContextCompat
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import android.widget.ProgressBar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions.Builder
import com.firebase.ui.database.SnapshotParser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView


class ChatActivity : AppCompatActivity() {

    class MessageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        internal var messageImageView: ImageView = itemView.findViewById(R.id.messageImageView)
        internal var messengerTextView: TextView = itemView.findViewById(R.id.messengerTextView)
        internal var messengerImageView: CircleImageView =
            itemView.findViewById(R.id.messengerImageView)

    }

    private val TAG = "MainActivity"
    val MESSAGES_CHILD = "messages"
    private val REQUEST_INVITE = 1
    private val REQUEST_IMAGE = 2
    private val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    val DEFAULT_MSG_LENGTH_LIMIT = 10
    val ANONYMOUS = "anonymous"
    private val MESSAGE_SENT_EVENT = "message_sent"
    private var mUsername: String? = null
    private val mPhotoUrl: String? = null
    private var mSharedPreferences: SharedPreferences? = null
    private val MESSAGE_URL = "http://friendlychat.firebase.google.com/message/"

    private lateinit var mSendButton: Button
    private lateinit var mMessageRecyclerView: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mMessageEditText: EditText
    private lateinit var mAddMessageImageView: ImageView

    /** Firebase */
    private lateinit var mFirebaseDatabaseReference: DatabaseReference
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.chat_activity)

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // Set default username is anonymous.
        mUsername = ANONYMOUS

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = findViewById(R.id.progressBar) as ProgressBar
        mMessageRecyclerView = findViewById(R.id.messageRecyclerView) as RecyclerView
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.setStackFromEnd(true)
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager)


        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference

        val parser: SnapshotParser<FriendlyMessage> = SnapshotParser { dataSnapshot ->
            val friendlyMessage =
                dataSnapshot.getValue<FriendlyMessage>(FriendlyMessage::class.java)
            if (friendlyMessage != null) {
                friendlyMessage.id = dataSnapshot.key
            }
            friendlyMessage!!
        }

        val messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD)
        val options = Builder<FriendlyMessage>()
            .setQuery(messagesRef, parser)
            .build()

        val activity = this

        mFirebaseAdapter =
            object : FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(options) {
                override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MessageViewHolder {
                    val inflater = LayoutInflater.from(viewGroup.context)
                    return MessageViewHolder(
                        inflater.inflate(
                            R.layout.item_message,
                            viewGroup,
                            false
                        )
                    )
                }

                override fun onBindViewHolder(
                    viewHolder: MessageViewHolder,
                    position: Int,
                    friendlyMessage: FriendlyMessage
                ) {
                    mProgressBar.visibility = ProgressBar.INVISIBLE
                    if (friendlyMessage.text != null) {
                        viewHolder.messageTextView.setText(friendlyMessage.text)
                        viewHolder.messageTextView.visibility = TextView.VISIBLE
                        viewHolder.messageImageView.visibility = ImageView.GONE
                    } else if (friendlyMessage.imageUrl != null) {
                        val imageUrl = friendlyMessage.imageUrl
                        if (imageUrl!!.startsWith("gs://")) {
                            val storageReference = FirebaseStorage.getInstance()
                                .getReferenceFromUrl(imageUrl)

                            storageReference.downloadUrl.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val downloadUrl = task.result!!.toString()
                                    Glide.with(viewHolder.messageImageView.context)
                                        .load(downloadUrl)
                                        .into(viewHolder.messageImageView)
                                } else {
                                    Log.w(
                                        TAG, "Getting download url was not successful.",
                                        task.exception
                                    )
                                }
                            }
                        } else {
                            Glide.with(viewHolder.messageImageView.context)
                                .load(friendlyMessage.imageUrl)
                                .into(viewHolder.messageImageView)
                        }
                        viewHolder.messageImageView.visibility = ImageView.VISIBLE
                        viewHolder.messageTextView.visibility = TextView.GONE
                    }


                    viewHolder.messengerTextView.text = friendlyMessage.name
                    if (friendlyMessage.photoUrl == null) {
                        viewHolder.messengerImageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                activity,
                                R.drawable.ic_account_circle_black_36dp
                            )
                        )
                    } else {
                        Glide.with(activity)
                            .load(friendlyMessage.photoUrl)
                            .into(viewHolder.messengerImageView)
                    }

                }
            }

        mFirebaseAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val friendlyMessageCount = mFirebaseAdapter.getItemCount()
                val lastVisiblePosition =
                    mLinearLayoutManager.findLastCompletelyVisibleItemPosition()
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 || positionStart >= friendlyMessageCount - 1 && lastVisiblePosition == positionStart - 1) {
                    mMessageRecyclerView.scrollToPosition(positionStart)
                }
            }
        })

        mMessageRecyclerView.adapter = mFirebaseAdapter

        mMessageEditText = messageEditText as EditText
        mMessageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mSendButton.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        mSendButton = sendButton
        mSendButton.setOnClickListener {
            // Send messages on click.
            val friendlyMessage = FriendlyMessage(
                mMessageEditText.text.toString(),
                mUsername.toString(),
                mPhotoUrl,
                null /* no image */
            )
            mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                .push().setValue(friendlyMessage)
            mMessageEditText.setText("")
        }

        mAddMessageImageView = addMessageImageView as ImageView
        mAddMessageImageView.setOnClickListener {
            // Select image for image message on click.
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }

    override fun onPause() {
        mFirebaseAdapter.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        mFirebaseAdapter.startListening()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    val uri = data!!.data
                    Log.d(TAG, "Uri: " + uri!!.toString())

                    val tempMessage = FriendlyMessage(
                        null, mUsername, mPhotoUrl,
                        LOADING_IMAGE_URL
                    )
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).push()
                        .setValue(
                            tempMessage
                        ) { databaseError, databaseReference ->
                            if (databaseError == null) {
                                val key = databaseReference.key
                                val storageReference = FirebaseStorage.getInstance()
                                    .getReference(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .child(key!!)
                                    .child(uri.lastPathSegment!!)

                                putImageInStorage(storageReference, uri, key)
                            } else {
                                Log.w(
                                    TAG, "Unable to write message to database.",
                                    databaseError.toException()
                                )
                            }
                        }
                }
            }
        }
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri?, key: String?) {
        val activity = this

        storageReference.putFile(uri!!).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                task.result!!.metadata!!.reference!!.downloadUrl
                    .addOnCompleteListener(
                        activity
                    ) { task ->
                        if (task.isSuccessful) {
                            val friendlyMessage = FriendlyMessage(
                                null, mUsername, mPhotoUrl,
                                task.result!!.toString()
                            )
                            mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                                .child(key!!)
                                .setValue(friendlyMessage)
                        }
                    }
            } else {
                Log.w(
                    TAG, "Image upload task was not successful.",
                    task.exception
                )
            }
        }
    }
}
