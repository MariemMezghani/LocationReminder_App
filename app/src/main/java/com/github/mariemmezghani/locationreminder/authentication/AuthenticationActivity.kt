package com.github.mariemmezghani.locationreminder.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.github.mariemmezghani.locationreminder.R
import com.github.mariemmezghani.locationreminder.databinding.ActivityAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        launchSignIn()

        setContentView(binding.root)

        //TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
//          TODO: If the user was authenticated, send him to RemindersActivity

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

    private fun launchSignIn() {
        //give users the option to sign in or register with email/google account
        val providers= arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // create and launch sign-in intent
        //we listen to the response with SIGH_IN_REQUEST_CODE
        startActivityForResult(
                AuthUI.getInstance().
                        createSignInIntentBuilder().
                        setAvailableProviders(providers).build(),
                AuthenticationActivity.SIGN_IN_REQUEST_CODE
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            // we start by getting the result of our resulting intent
            val respnse = IdpResponse.fromResultIntent(data)
            // then we check the resultCode to see what the result of the login was
            if (resultCode == Activity.RESULT_OK){
                Log.i(TAG,"Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}" )
            }else{
                // if the response is null, the user canceled the sign in flow by pressing the back button.
                // otherwise check the error code to handle it
                Log.i(TAG,"sign in unsuccessfull ${respnse?.error?.errorCode}")

            }
        }
    }
    companion object {
        const val TAG = "AuthenticationActivity"
        const val SIGN_IN_REQUEST_CODE = 1001
    }
}