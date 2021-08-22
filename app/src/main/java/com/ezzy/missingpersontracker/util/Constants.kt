package com.ezzy.missingpersontracker.util

object Constants {
    val TAB_TITLES = arrayListOf("Login", "Signup")
    const val PICK_PHOTO_REQUEST_CODE = 1
    const val TAKE_IMAGE_REQUEST_CODE = 0
    const val TAKE_PHOTO = "Take Photo"
    const val PICK_FROM_GALLERY = "Choose from gallery"
    const val CANCEL = "Cancel"
    const val CHOOSE_IMAGE = "Choose an image"
    const val REQUEST_PERMISSION_CODE = 10
    const val PREFERENCE_NAME = "user_preference"

    @Suppress("SpellCheckingInspection")
    object FIRESTORECOLLECTIONS {
        const val USER_COLLECTION = "users"
        const val MISSING_PERSON_COLLLECTION = "missing_person"
        const val MISSING_PERSON_IMAGES_COLLECTION = "missing_person_images"
        const val ADDRESS = "address"
        const val LOCATION = "location"
        const val IMAGES = "images"
        const val ADDITIONAL_CONTACTS = "additional_contacts"
    }

}