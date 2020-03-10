package com.example.allfavour.ui.consumer.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.allfavour.DecoratedActivity
import com.example.allfavour.R
import com.example.allfavour.ui.consumer.addFavour.AddFavourFragment
import kotlinx.android.synthetic.main.consumer_search_fragment.*


class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.consumer_search_fragment, container, false)

        val activity = requireActivity() as DecoratedActivity
        activity.toggleBottomNavVisibility(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_favour_button.setOnClickListener {
//            navController.navigate(SearchFragmentDirections.actionConsumerAddFavour())

            val newFragment = AddFavourFragment.newInstance()
//
            newFragment.show(fragmentManager!!, null)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
