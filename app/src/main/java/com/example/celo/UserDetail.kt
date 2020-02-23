package com.example.celo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.celo.model.User
import com.squareup.picasso.Picasso

class UserDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        //Retrieve data from parcel user object
        val user = intent.getParcelableExtra("Bundle") as User
        populateViews(user)
    }

    fun populateViews(user: User){
        //Populate Large Imageview
        val largeImage: ImageView = findViewById(R.id.thumbnail) as ImageView
        Picasso.get().load(user.largeImage).into(largeImage)

        //Populate TextViews
        val namesView: TextView = findViewById(R.id.name) as TextView
        namesView.setText(user.firstName)
        val emailView: TextView = findViewById(R.id.email) as TextView
        emailView.setText(user.email)
        val phoneView: TextView = findViewById(R.id.phone) as TextView
        phoneView.setText(user.phone)
        val genderView: TextView = findViewById(R.id.gender) as TextView
        genderView.setText(user.gender)
        val dobView: TextView = findViewById(R.id.dob) as TextView
        dobView.setText(user.dateOfBirth)
    }
}
