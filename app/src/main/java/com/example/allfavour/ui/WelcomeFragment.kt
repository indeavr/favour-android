package com.example.allfavour.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.allfavour.R
import kotlinx.android.synthetic.main.welcome_fragment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WelcomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        login_button.setOnClickListener {
//            val action = WelcomeFragmentDirections.authDest()
//            findNavController().navigate(action)
//        }
//
//        consumer_button.setOnClickListener {
//            val action = WelcomeFragmentDirections.consumerDest()
//            findNavController().navigate(action)
//        }
//
//        provider_button.setOnClickListener {
//            val action = WelcomeFragmentDirections.providerDest()
//            findNavController().navigate(action)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_fragment, container, false)

    }

    companion object {
        fun newInstance() = WelcomeFragment()
    }
}
