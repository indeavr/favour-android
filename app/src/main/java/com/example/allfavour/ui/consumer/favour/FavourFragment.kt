package com.example.allfavour.ui.consumer.favour

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels

import com.example.allfavour.R
import com.example.allfavour.data.FavourRepository
import com.example.allfavour.ui.auth.AddFavourViewModelFactory
import com.example.allfavour.ui.auth.SearchViewModelFactory
import com.example.allfavour.ui.consumer.search.SearchViewModel
import com.example.allfavour.ui.provider.addFavour.AddFavourViewModel
import kotlinx.android.synthetic.main.consumer_favour_fragment.*

class FavourFragment : Fragment() {

    companion object {
        fun newInstance() =
            FavourFragment()
    }

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }

    private val viewModel: SearchViewModel by navGraphViewModels(R.id.consumer_search_navigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.consumer_favour_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentId = arguments?.getString("fragmentId")
        viewModel.setCurrentFavour(fragmentId!!)

        val favour = viewModel.currentFavour

        favour_title.text = favour!!.title
        favour_money.text = favour.money.toString()
        favour_desc.text = favour.description

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }


}
