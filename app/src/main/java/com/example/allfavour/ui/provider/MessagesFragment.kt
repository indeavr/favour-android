package com.example.allfavour.ui.provider


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allfavour.DecoratedActivity

import com.example.allfavour.R
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : Fragment() {
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val navController: NavController by lazy { findNavController(this) }
    private val firebaseDB: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

    private val chatData = ArrayList<ChatItem>()

    private val myUserId: String by lazy { AuthenticationProvider.getUserId(requireActivity())!! }
    private val myUsername: String by lazy { AuthenticationProvider.getUserFullname(requireActivity()) }
    val USERS_CHILD = "users"
    val CHAT_CHILD = "chats"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseDB.child(USERS_CHILD)
            .child(myUserId)
            .child("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnap: DataSnapshot) {
                    val activeChats = dataSnap.children
                    val chatIds = ArrayList<String>()
                    activeChats.forEach {
                        chatIds.add(it.key!!)
                    }
                    chatData.clear()

                    adapter.notifyDataSetChanged()
                    getFullChatData(chatIds)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.provider_messages_fragment, container, false)

        view.findViewById<Button>(R.id.new_chat)
            .setOnClickListener(::showPeopleList)

        val activity = this.requireActivity()
        (activity as DecoratedActivity).toggleBottomNavVisibility(true)


        layoutManager = LinearLayoutManager(activity)

        adapter = PeopleListAdapter(chatData, myUserId, navController)

        val recyclerView = view.findViewById<RecyclerView>(R.id.chat_people_recycle_list)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        recyclerView.hasFixedSize()

        return view
    }

    fun getFullChatData(chatIds: ArrayList<String>) {
        chatIds.forEach { chatId ->
            firebaseDB.child(CHAT_CHILD)
                .child(chatId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val chatItem = snapshot.getValue(ChatItem::class.java)

                        if (chatItem != null) {
                            chatItem.id = snapshot.key
                            val index = chatData.size
                            chatData.add(index, chatItem)

                            adapter.notifyItemInserted(index)
                        }

                        firebaseDB.child(CHAT_CHILD)
                            .child(chatId)
                            .removeEventListener(this)
                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
        }
    }

    private fun showPeopleList(it: View) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children

                val fullNames = ArrayList<String>()
                val userToId = mutableMapOf<String, String>()

                users.forEach {
                    val id = it.key
                    if (id != myUserId) {

                        // TODO: what if there is no fullName for some reason ? -> crash
                        val fullName = it.child("fullName").getValue(String::class.java)
                        fullNames.add(fullName!!)
                        userToId[fullName] = id!!
                    }
                }

                val array = arrayOfNulls<String>(fullNames.size)
                fullNames.toArray<String>(array)

                MaterialAlertDialogBuilder(context)
                    .setTitle("Choose a Person")
                    .setItems(array) { dialog: DialogInterface, which: Int ->
                        val user = fullNames[which]
                        startNewChat(userToId[user]!!, user)
                    }
                    .setPositiveButton("Ok", null)
                    .show()

                firebaseDB.child(USERS_CHILD).removeEventListener(this)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }

        firebaseDB.child(USERS_CHILD)
            .addListenerForSingleValueEvent(listener)
    }

    fun startNewChat(userId: String, username: String) {
        val chatId = firebaseDB.child(CHAT_CHILD)
            .push()
            .key

        val userIdToUsername = mutableMapOf(userId to username, myUserId to myUsername)

        val chatItem = ChatItem(userIdToUsername)
        val chatItemValues = chatItem.toMap()


        val childUpdates = HashMap<String, Any>()
        childUpdates["/chats/$chatId"] = chatItemValues
        childUpdates["/users/$userId/chats/$chatId"] = true
        childUpdates["/users/$myUserId/chats/$chatId"] = true

        firebaseDB.updateChildren(childUpdates)
    }

    class PeopleListAdapter(
        private val myDataset: ArrayList<ChatItem>,
        private val myUserId: String,
        private val navController: NavController
    ) :
        RecyclerView.Adapter<PeopleListAdapter.ViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            // create a new view
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_person_item, parent, false)

            // set the view's size, margins, paddings and layout parameters

            return ViewHolder(textView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view wi
            // th that element
            if (myDataset.isNullOrEmpty()) {

            } else {
                val notMyId = myDataset[position].users.keys.first { it != myUserId }

                holder.view.findViewById<TextView>(R.id.personName).text =
                    myDataset[position].users[notMyId]

                holder.view.setOnClickListener {
                    val chatId = myDataset[position].id
                    val action =
                        MessagesFragmentDirections.actionProviderMessagesDestToChatFragment(chatId!!)
                    navController.navigate(action)
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }

    @IgnoreExtraProperties
    data class ChatItem(
        var users: MutableMap<String, String> = HashMap(),
        var lastMessage: String? = "",
        var lastMessageTime: String? = ""
    ) {

        var id: String? = null

        @Exclude
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "users" to users,
                "lastMessage" to lastMessage,
                "lastMessageTime" to lastMessageTime
            )
        }
    }
}
