package com.example.allfavour.ui.provider.myOfferings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.allfavour.DecoratedActivity

import com.example.allfavour.R
import com.example.allfavour.data.model.ActiveOfferingModel
import com.example.allfavour.data.model.OfferingModel
import com.example.allfavour.ui.auth.AuthViewModelFactory
import com.example.allfavour.ui.auth.AuthenticationViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchFragment

class MyOfferingsFragment : Fragment() {

    companion object {
        fun newInstance() = MyOfferingsFragment()
    }

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }

    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var listData: ArrayList<ActiveOfferingModel>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val factory = MyOfferingsViewModelFactory()
    private val viewModel: MyOfferingsViewModel by navGraphViewModels(R.id.provider_my_offerings_navigation) { factory }

    private val authViewModel: AuthenticationViewModel by lazy {
        ViewModelProviders.of(
            this.requireActivity(),
            AuthViewModelFactory()
        ).get(AuthenticationViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.myOfferingsList.observe(this, Observer<List<ActiveOfferingModel>> { myOfferings ->
            swipeRefreshLayout.isRefreshing = false

            listData.clear()
            listData.addAll(myOfferings)

            adapter.notifyDataSetChanged()
        })
        viewModel.getMyOfferings(authViewModel.userId)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.provider_my_offerings_fragment, container, false)

        setupRecycler(view)

        return view
    }

    fun setupRecycler(view: View) {
        val activity = requireActivity()
        (activity as DecoratedActivity).toggleBottomNavVisibility(true)

        swipeRefreshLayout =
            view.findViewById(R.id.my_offerings_swipe_container)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true

            viewModel.getMyOfferings(authViewModel.userId)
        }

        swipeRefreshLayout.setColorSchemeResources(
            R.color.primaryColor,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true

            viewModel.getMyOfferings(authViewModel.userId)
        }


        listData = viewModel.myOfferingsList.value ?: arrayListOf()

        val favourRecycleList = view.findViewById<RecyclerView>(R.id.my_offerings_recycle_list)
        layoutManager = LinearLayoutManager(activity)
        adapter = MyOfferingsAdapter(
            this.listData,
            navController
        )

        favourRecycleList.adapter = adapter
        favourRecycleList.layoutManager = layoutManager
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    class MyOfferingsAdapter(
        private val myDataset: ArrayList<ActiveOfferingModel>,
        private val navController: NavController
    ) :
        RecyclerView.Adapter<MyOfferingsAdapter.ViewHolder>() {

        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_offering_item, parent, false)

            return ViewHolder(textView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            if (myDataset.isNullOrEmpty()) {

            } else {
                val activeOffering = myDataset[position]

                holder.view.findViewById<TextView>(R.id.my_offering_item_title).text =
                    activeOffering.offering.title
                holder.view.findViewById<TextView>(R.id.my_offering_item_applications_count).text =
                    activeOffering.applications?.size.toString() ?: "0"
//                holder.view.findViewById<TextView>(R.id.offering_item_address).text =
//                    offering.location.address
//                holder.view.findViewById<TextView>(R.id.offering_item_dates).text =
//                    offering.description
//                holder.view.findViewById<TextView>(R.id.offering_item_money).text =
//                    offering.money.toString()
//
//                holder.view.setOnClickListener {
//                    if (offering.id != null) {
//                        navController.navigate(
//                            OfferingsSearchFragmentDirections.offeringDest(offering.id!!)
//                        )
//                    }
//                }
            }
        }

        override fun getItemCount() = myDataset.size
    }
}
