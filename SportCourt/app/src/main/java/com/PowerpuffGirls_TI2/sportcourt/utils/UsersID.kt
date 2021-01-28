package com.PowerpuffGirls_TI2.sportcourt.utils

import com.google.firebase.auth.FirebaseAuth

class UsersID {
    companion object{
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
    }
}