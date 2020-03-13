//package com.example.allfavour.ui.oldauth
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProviders
//import com.example.allfavour.data.LoginRepository
//
///**
// * ViewModel consumer factory to instantiate LoginViewModel.
// * Required given LoginViewModel has a non-empty constructor
// */
//class RegisterViewModelFactory1 : ViewModelProviders.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
//            return RegisterViewModel(
//                loginRepository = LoginRepository()
//            ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
