package com.example.allfavour.ui.consumer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.allfavour.R
import kotlinx.android.synthetic.main.consumer_messages_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : Fragment() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    val navController: NavController by lazy { findNavController(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.consumer_messages_fragment, container, false)

        val activity = this.requireActivity()
        viewManager = LinearLayoutManager(activity)
        val arr = Array(2) { "Pesho"; "Gosho" }
        viewAdapter = PeopleListAdapter(arr, navController)

        val recyclerView = view.findViewById<RecyclerView>(R.id.chat_people_recycle_list)

        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter

        recyclerView.hasFixedSize()

        return view
    }


    class PeopleListAdapter(
        private val myDataset: Array<String>,
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
            holder.view.findViewById<TextView>(R.id.personName).text = myDataset[position]

            holder.view.setOnClickListener {
                val personName = it.findViewById<TextView>(R.id.personName).text.toString()
//                val args = bundleOf("personName" to personName)

                val action =
                    MessagesFragmentDirections.actionConsumerMessagesDestToChatFragment(personName)
                navController.navigate(action)
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }
}
