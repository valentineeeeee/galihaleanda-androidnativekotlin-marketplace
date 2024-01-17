package com.example.prjctmobile.session
import android.content.Context
import android.content.SharedPreferences
class SessionManager(context: Context) {
        private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        private val editor: SharedPreferences.Editor = sharedPreferences.edit()

        companion object {
            private const val PREF_NAME = "UserSession"
            private const val KEY_USER_ID = "userId"
            private const val KEY_USER_EMAIL = "email"
            private const val KEY_USER_USERNAME = "username"
            private const val KEY_USER_IMAGE_URL = "imageUrl"
        }

        fun saveUserSession(userId: String, userEmail: String, username: String, imageUrl: String) {
            editor.putString(KEY_USER_ID, userId)
            editor.putString(KEY_USER_EMAIL, userEmail)
            editor.putString(KEY_USER_USERNAME, username)
            editor.putString(KEY_USER_IMAGE_URL, imageUrl)
            editor.apply()
        }

        fun isLoggedIn(): Boolean {
            return sharedPreferences.contains(KEY_USER_ID)
        }

        fun getUserId(): String? {
            return sharedPreferences.getString(KEY_USER_ID, null)
        }

        fun getUserEmail(): String? {
            return sharedPreferences.getString(KEY_USER_EMAIL, null)
        }

        fun getUserName(): String? {
            return sharedPreferences.getString(KEY_USER_USERNAME, null)
        }
    fun getUserImageUrl(): String? {
        return sharedPreferences.getString(KEY_USER_IMAGE_URL, null)
    }

        fun clearSession() {
            editor.clear()
            editor.apply()
        }
}