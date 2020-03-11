package com.example.allfavour.ui.consumer.search

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.allfavour.DecoratedActivity
import com.example.allfavour.R
import com.example.allfavour.data.model.Offering
import com.example.allfavour.ui.consumer.addFavour.AddFavourFragment
import com.example.allfavour.ui.provider.search.SearchViewModelFactory
import kotlinx.android.synthetic.main.consumer_search_fragment.*


class OfferingsSearchFragment : Fragment() {

    companion object {
        fun newInstance() = OfferingsSearchFragment()
    }

    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var listData: ArrayList<Offering>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }

//    private val viewModel: OfferingsSearchViewModel by lazy {
//        ViewModelProviders.of(
//            this,
//            OfferingsSearchViewModelFactory()
//        ).get(OfferingsSearchViewModel::class.java)
//    }

    private val factory = OfferingsSearchViewModelFactory()
    private val viewModel: OfferingsSearchViewModel by navGraphViewModels(R.id.consumer_search_navigation) { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.offeringsList.observe(this, Observer<ArrayList<Offering>> { offerings ->
            swipeRefreshLayout.isRefreshing = false

            listData.clear()
            listData.addAll(offerings)

            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.consumer_search_fragment, container, false)

        val activity = requireActivity()
        (activity as DecoratedActivity).toggleBottomNavVisibility(true)

        swipeRefreshLayout =
            view.findViewById(R.id.consumer_search_swipe_container)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true

            viewModel.getOfferings()
        }

        swipeRefreshLayout.setColorSchemeResources(
            R.color.primaryColor,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true

            viewModel.getOfferings()
        }


        listData = viewModel.offeringsList.value ?: arrayListOf()

        val favourRecycleList = view.findViewById<RecyclerView>(R.id.favours_recycle_list)
        layoutManager = LinearLayoutManager(activity)
        adapter = OfferingsAdapter(
            this.listData,
            navController
        )

        favourRecycleList.adapter = adapter
        favourRecycleList.layoutManager = layoutManager


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager = this.activity!!.supportFragmentManager
        add_favour_button.setOnClickListener {
            //            navController.navigate(SearchFragmentDirections.actionConsumerAddFavour())

            val newFragment = AddFavourFragment.newInstance()
//
            newFragment.show(fragmentManager, null)
        }

        offerings_arround_me_button.setOnClickListener {

            val newFragment = AddFavourFragment.newInstance()
//
            newFragment.show(fragmentManager, null)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    class OfferingsAdapter(
        private val myDataset: ArrayList<Offering>,
        private val navController: NavController
    ) :
        RecyclerView.Adapter<OfferingsAdapter.ViewHolder>() {

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
                .inflate(R.layout.offering_item, parent, false)

            return ViewHolder(textView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            if (myDataset.isNullOrEmpty()) {

            } else {
                val offering = myDataset[position]

                holder.view.findViewById<TextView>(R.id.offering_item_title).text = offering.title
                holder.view.findViewById<TextView>(R.id.offering_item_money).text =
                    offering.money.toString()

                holder.view.setOnClickListener {
                    if (offering.id != null) {
//                        navController.navigate(
//                            SearchFragmentDirections.actionProviderSearchDestToFavourFragment(
//                                offering.id!!
//                            )
//                        )
                    }
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }
}
