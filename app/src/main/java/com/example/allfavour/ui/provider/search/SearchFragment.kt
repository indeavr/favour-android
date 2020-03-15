package com.example.allfavour.ui.provider.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.allfavour.R
import com.example.allfavour.data.model.Favour
import com.example.allfavour.ui.provider.search.SearchViewModelFactory
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModelFactory


class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var listData: ArrayList<Favour>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }

    private val factory = SearchViewModelFactory()
    private val viewModel: SearchViewModel by navGraphViewModels(R.id.provider_search_navigation) { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.getFavours()

        viewModel.favoursList.observe(this, Observer<ArrayList<Favour>> { favours ->
            swipeRefreshLayout.isRefreshing = false

            listData.clear()
            listData.addAll(favours)

            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.provider_search_fragment, container, false)

        swipeRefreshLayout =
            view.findViewById(R.id.provider_search_swipe_container)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true

            viewModel.getFavours()
        }

        swipeRefreshLayout.setColorSchemeResources(
            R.color.primaryColor,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true

            viewModel.getFavours()
        }


        val activity = this.requireActivity()

        listData = viewModel.favoursList.value ?: arrayListOf()

        val favourRecycleList = view.findViewById<RecyclerView>(R.id.favours_recycle_list)
        layoutManager = LinearLayoutManager(activity)
        adapter = FavoursAdapter(this.listData, navController)

        favourRecycleList.adapter = adapter
        favourRecycleList.layoutManager = layoutManager

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
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.favour_item, parent, false)

            return ViewHolder(textView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            if (myDataset.isNullOrEmpty()) {

            } else {
                val favour = myDataset[position]

                holder.view.findViewById<TextView>(R.id.title).text = favour.title
                holder.view.findViewById<TextView>(R.id.money).text = favour.money.toString()

                holder.view.setOnClickListener {
                    if (favour.id != null) {
                        navController.navigate(
                            SearchFragmentDirections.actionProviderSearchDestToFavourFragment(
                                favour.id!!
                            )
                        )
                    }
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }
}
