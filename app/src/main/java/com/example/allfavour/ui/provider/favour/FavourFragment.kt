package com.example.allfavour.ui.provider.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels

import com.example.allfavour.R
import com.example.allfavour.ui.provider.search.SearchViewModel
import kotlinx.android.synthetic.main.provider_favour_fragment.*

class FavourFragment : Fragment() {

    companion object {
        fun newInstance() =
            FavourFragment()
    }

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }

    private val viewModel: SearchViewModel by navGraphViewModels(R.id.provider_search_navigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.provider_favour_fragment, container, false)

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
