package com.benlinux.realestatemanager.data.userRepository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.benlinux.realestatemanager.ui.models.User
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

class UserRepository {

    companion object {
        // FIRESTORE DATA
        private const val COLLECTION_NAME = "users"
        private const val USERNAME_FIELD = "name"
        private const val EMAIL_FIELD = "email"
        private const val AVATAR_FIELD = "avatar"
        private const val REALTOR_FIELD = "realtor"
        private const val FAVORITES_FIELD = "favorites"
        private const val PROPERTIES_FIELD = "realtorProperties"

        fun getCurrentUser(): FirebaseUser? {
            return FirebaseAuth.getInstance().currentUser
        }

        private fun getCurrentUserUID(): String? {
            val user = getCurrentUser()
            return user?.uid
        }

        fun signOut(context: Context?): Task<Void?> {
            return AuthUI.getInstance().signOut(context!!)
        }

        fun deleteUser(context: Context?): Task<Void?> {
            return AuthUI.getInstance().delete(context!!)
        }


        // Get the Collection Reference in Firestore Database
        private fun getUsersCollection(): CollectionReference {
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
        }

        // Create User in Firestore
        fun createUser(userData: User): Task<QuerySnapshot>? {
            val user = getCurrentUser()
            return if (user != null) {
                val urlPicture = if (user.photoUrl != null) user.photoUrl.toString() else ""
                val uid = user.uid

                val userToCreate = User(
                    uid,
                    userData.email,
                    userData.firstName,
                    userData.lastName,
                    urlPicture,
                    userData.favorites,
                    userData.isRealtor,
                    userData.realtorProperties)

                // Check if user exists in database
                getAllUsersData().addOnSuccessListener { querySnapshot: QuerySnapshot ->
                    // Get all users
                    val users: List<User> =
                        querySnapshot.toObjects(User::class.java)
                    var userExists = false
                    // Check if user id exists in database
                    for (i in users.indices) {
                        if (users[i].id == uid) {
                            // If exists, set boolean to true
                            userExists = true
                            break
                        }
                    }
                    // If user exists, don't do anything
                    if (userExists) {
                        Log.d("USER CREATION INFO", "user already exists")
                        // If user doesn't exist, create it in FireStore
                    } else {
                        getUsersCollection().document(uid).set(userToCreate)
                    }
                }
            } else {
                null
            }
        }


        // Get User Data from Firestore
        fun getUserData(): Task<DocumentSnapshot?>? {
            val uid = getCurrentUserUID()
            return if (uid != null) {
                getUsersCollection().document(uid).get()
            } else {
                null
            }
        }


        // Get all users from Firestore
        fun getAllUsersData(): Task<QuerySnapshot> {
            return getUsersCollection().get()
        }


        // Update Username in FireStore
        fun updateUsername(username: String?) {
            val uid = getCurrentUserUID()
            if (uid != null) {
                getUsersCollection().document(uid).update(USERNAME_FIELD, username)
            }
        }


        // Update User Avatar in FireStore
        fun updateUserAvatar(avatarUrl: String?) {
            val uid = getCurrentUserUID()
            if (uid != null) {
                getUsersCollection().document(uid).update(AVATAR_FIELD, avatarUrl)
            }
        }

        // Update User isNotified in FireStore
        fun updateIsRealtor(isRealtor: Boolean?) {
            val uid = getCurrentUserUID()
            if (uid != null) {
                getUsersCollection().document(uid).update(REALTOR_FIELD, isRealtor)
            }
        }

        // Add restaurant to favorites in FireStore
        fun addPropertyToFavorites(propertyId: String?) {
            val uid = getCurrentUserUID()
            if (uid != null) {
                getUsersCollection().document(uid)
                    .update(FAVORITES_FIELD, FieldValue.arrayUnion(propertyId))
            }
        }

        // Remove restaurant from favorites in FireStore
        fun removePropertyFromFavorites(propertyId: String?) {
            val uid = getCurrentUserUID()
            if (uid != null) {
                getUsersCollection().document(uid)
                    .update(FAVORITES_FIELD, FieldValue.arrayRemove(propertyId))
            }
        }




        // Upload image from device to firebase storage
        fun uploadImage(imageUri: Uri?, pictures: String): UploadTask {
            val uuid = UUID.randomUUID().toString() // GENERATE UNIQUE STRING
            val mImageRef = FirebaseStorage.getInstance().getReference("$pictures/$uuid")
            return mImageRef.putFile(imageUri!!)
        }

        // Delete the User from Firestore
        // if result ok, delete from firebase & logout
        fun deleteUserFromFirestore(context: Context?): Task<Void?> {
            val uid = getCurrentUserUID()!!
            return getUsersCollection().document(uid).delete()
        }

    }


}