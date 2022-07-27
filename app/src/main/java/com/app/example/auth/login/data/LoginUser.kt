package com.app.example.auth.login.data

import android.text.TextUtils
import android.util.Patterns
import com.app.example.base.data.IValidate

class LoginUser(
    val email: String,
    val password: String
) : IValidate {

    override fun isValid(): Int {
        if (TextUtils.isEmpty(email)) {
            return 0
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return 1
        } else if (TextUtils.isEmpty(password)) {
            return 2
        } else if (password.length < 6) {
            return 3
        } else {
            return -1
        }
    }
}