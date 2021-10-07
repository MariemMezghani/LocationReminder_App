package com.github.mariemmezghani.locationreminder.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.firebase.ui.auth.AuthUI
import com.github.mariemmezghani.locationreminder.locationreminders.RemindersActivity

class LoginViewModel : ViewModel() {

    // encapsulation
    val authenticationState:LiveData<LoginViewModel.AuthenticationState>
        get()= _authenticationState

    //create authState variable based on the FirebaseUserLiveData object
    private var _authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }

    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }

}