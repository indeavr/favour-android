package com.example.allfavour.ui.consumer.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allfavour.MainNavigationDirections

import com.example.allfavour.R
import com.example.allfavour.data.model.Favour
import com.example.allfavour.ui.auth.RegisterViewModel
import com.example.allfavour.ui.auth.RegisterViewModelFactory
import com.example.allfavour.ui.auth.SearchViewModelFactory
import com.example.allfavour.utility.HandleNotifications
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.basic_info_form_fragment.*
import kotlinx.android.synthetic.main.consumer_search_fragment.*
import kotlinx.android.synthetic.main.main_nav_activity.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var listData: ArrayList<Favour>

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }

    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(
            this,
            SearchViewModelFactory()
        ).get(SearchViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFavours()

        viewModel.favoursList.observe(this, Observer<ArrayList<Favour>> { favours ->
            listData.clear()
            listData = favours

            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.consumer_search_fragment, container, false)
        val activity = this.requireActivity()

        listData = arrayListOf()

        val favourRecycleList = view.findViewById<RecyclerView>(R.id.favours_recycle_list)
        favourRecycleList.layoutManager = LinearLayoutManager(activity)
        favourRecycleList.adapter = FavoursAdapter(this.listData, navController)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        btn_notify.setOnClickListener {
//            HandleNotifications.showNotification(requireContext())
//        }
//
//        btn_logout.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            requireActivity().findNavController(R.id.main_nav_activity)
//                .navigate(MainNavigationDirections.authNavigationDest())
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }


    class FavoursAdapter(
        private val myDataset: ArrayList<Favour>,
        private val navController: NavController
    ) :
        RecyclerView.Adapter<FavoursAdapter.ViewHolder>() {

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
                .inflate(R.layout.favour_item, parent, false)

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
                val favour = myDataset[position]

                holder.view.findViewById<TextView>(R.id.title).text = favour.title
                holder.view.findViewById<TextView>(R.id.money).text = favour.money.toString()

                holder.view.setOnClickListener {
                    //                    val action =
//                        MessagesFragmentDirections.actionConsumerMessagesDestToChatFragment(chatId!!)
//                    navController.navigate(action)
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }
}
