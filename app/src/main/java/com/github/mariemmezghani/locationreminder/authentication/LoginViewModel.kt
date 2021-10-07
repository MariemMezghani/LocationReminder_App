package com.github.mariemmezghani.locationreminder.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class LoginViewModel : ViewModel() {

    //create authState variable based on the FirebaseUserLiveData object
    val authenticationState = FirebaseUserLiveData().map { user ->
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