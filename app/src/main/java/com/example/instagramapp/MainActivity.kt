package com.example.instagramapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File


// (Steps for the project )
//1- create a new login activity to identify a user
//2- we go to android manifest, we move our intent-filter inside our login activity so that it appears first when we run our app
//3- implement our login activity

class MainActivity : AppCompatActivity() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 1. Setting the description of the post
        // 2. A button to launch the camera to take a picture
        // 3. An imageView to show the picture the user has taken
        // 4. A button to save and send the post to our Parse server

        findViewById<Button>(R.id.submit_button).setOnClickListener{
            // send a post to a server
            // get the description that they have inputted

            val description = findViewById<EditText>(R.id.et_description).text.toString()
            val user = ParseUser.getCurrentUser()
            if(photoFile != null)
            {
                submitPost(description, user, photoFile!!)
            }
            else
            {
                // Print error log message
                Log.e(TAG, "Error to print out message")
                // show a toast to the user to let them know to take a picture
                Toast.makeText(this,"Failed to print message", Toast.LENGTH_SHORT).show()
            }


        }

        findViewById<Button>(R.id.picture_button).setOnClickListener{
            // launch camera to let user take a picture
            onLaunchCamera()
        }

        queryPosts()
        
    }

    // Sent a Post object to our Parse server
    fun submitPost(description: String, user: ParseUser, file: File )
    {
        // create the Post object
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground{ exception ->
            if(exception != null)
            {
               //something has went wrong
                Log.e(TAG, "Error while saving Post")
                exception.printStackTrace()
                // show a toast that something wrong has happened
                Toast.makeText(this, "Error saving post ", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Log.i(TAG, "Successfully saved post")
                // Resetting the edittext field to be empty
                // reset the imageview to empty
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.imageView)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    // Query for all Posts in our server
    fun queryPosts()
    {
        //specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        // Find all Post objects
        query.include(Post.KEY_USER)
       query.findInBackground(object : FindCallback<Post>{
           override fun done(posts: MutableList<Post>?, e: ParseException?) {
               if(e != null){
                   //something has went wrong
                   Log.e(TAG, "Error fetching posts")
               }
               else{
                   if(posts != null){
                       for(post in posts){
                           Log.i(TAG, "Post: " + post.getDescription() + " , username : " + post.getUser()?.username )
                       }
                   }
               }
           }

       })


    }
    companion object {
        const val TAG = "MainActivity"
    }
}