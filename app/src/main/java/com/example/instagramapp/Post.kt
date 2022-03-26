package com.example.instagramapp

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Post")

// 1 - Description : String
// 2 - image : File
// 3 - user : User
class Post: ParseObject() {
    // creating getters and setters for 1,2,3 above
    // getters and setters for 1
    fun getDescription() : String? {
        return getString(KEY_DESCRIPTION)
    }
    fun setDescription(description: String) {
        put(KEY_DESCRIPTION , description)
    }
    // getters and setters for 2
    fun getImage() : ParseFile? {
        return getParseFile(KEY_IMAGE)
    }
    fun setImage(parsefile: ParseFile) {
        put(KEY_IMAGE , parsefile)
    }
    // getters and setters for 3
    fun getUser() : ParseUser? {
        return getParseUser(KEY_USER)
    }
    fun setUser(user : ParseUser) {
        put(KEY_USER , user)
    }


    companion object{
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
    }
}