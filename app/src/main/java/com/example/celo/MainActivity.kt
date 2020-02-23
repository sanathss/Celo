package com.example.celo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.celo.database.DatabaseHandler
import com.example.celo.model.User
import com.example.celo.network.UserSingleton
import com.example.celo.recycler.RecyclerItemClickListener
import com.example.celo.recycler.RecyclerViewAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    //Layout variables
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: RecyclerViewAdapter
    //Database variables
    lateinit var dbHandler: DatabaseHandler
    //Lists to store users
    var userList: ArrayList<User> = ArrayList()
    //Network variables
    val url = "https://randomuser.me/api/?results=25"
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //Initial Setup methods
        initDB()
        initRecyclerView()
        //First call to user retrieval
        retrieveOnline()
    }

    fun initDB() {
        dbHandler = DatabaseHandler(this)
    }

    fun initRecyclerView(){
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //Scroll Listener used here to implement pagination
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {   //Ensure user swipe is downward for pagination
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = layoutManager.itemCount
                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItem) >= total) {
                            getPage() //Point reached once the user has scrolled to the bottom of the list in downward motion
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        //Setup of recyclerview to correctly hold and display user information using adapter and RecyclerViewAdapter class
        recyclerAdapter = RecyclerViewAdapter(this, userList)
        recyclerView.adapter = recyclerAdapter
        recyclerView.addOnItemTouchListener(    //Listener for clicks on rows inside recyclerview
            RecyclerItemClickListener(applicationContext,
                RecyclerItemClickListener.OnItemClickListener { view, position ->
                    val intent = Intent(applicationContext, UserDetail::class.java) //Setup and starting of new activity
                    val user = userList.get(position)
                    intent.putExtra("Bundle", user)
                    startActivity(intent)
                })
        )
        //Setup of listener used for filter/search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerAdapter!!.getFilter().filter(newText)
                return true
            }
        })
    }

    fun getPage(){  //Method used for loading more data to support Pagination
        isLoading = true
        progressBar.visibility = View.VISIBLE
        if (::recyclerAdapter.isInitialized) {
            retrieveOnline()
        } else {
            isLoading = false
            progressBar.visibility = View.GONE
        }
    }

    fun retrieveOnline(){   //Method for hitting API. If method fails, offline method is called.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->                                             //If API returns data correctly continue
                val userListJSON: JSONArray = response.get("results") as JSONArray      //Retrieve information in JSON
                val userListConverted: ArrayList<User> = JsonArray2List(userListJSON)   //Arraylist conversion
                userList.addAll(userListConverted)
                dbHandler.addUsers(userListConverted)
                recyclerAdapter.notifyDataSetChanged()      //Include new information in list
                isLoading = false
                progressBar.visibility = View.GONE
            },
            Response.ErrorListener { error ->var error =error.toString()
                retrieveOffline()                           //Offline database retrieval
            }
        )
        // Access the RequestQueue through your singleton class.
        UserSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun retrieveOffline(){
        userList.addAll(dbHandler.getUsers())   //Access search methods from DatabaseHandler class
        recyclerAdapter.notifyDataSetChanged()
        isLoading = false
        progressBar.visibility = View.GONE
    }

    fun JsonArray2List(userListJSON: JSONArray): ArrayList<User>{
        //Setup temp lists used for data manipulation
        val tmpUserList = ArrayList<User>()
        var tmpUserJSON: JSONObject? = null

        for (i in 0..userListJSON.length()-1) {
            val tmpUser = User()
            tmpUserJSON = userListJSON.get(i) as JSONObject

            //Retrieve information based on returned JSON
            //Implement other attributes below
            try {
                val names: JSONObject = tmpUserJSON.get("name") as JSONObject   //JSON Name Object
                tmpUser.title = names.getString("title")
                tmpUser.firstName = names.getString("first")
                tmpUser.lastName = names.getString("last")

                tmpUser.gender = tmpUserJSON.getString("gender")
                //Extract Date of Birth
                val dob: JSONObject = tmpUserJSON.get("dob") as JSONObject      //JSON dob Object
                tmpUser.dateOfBirth = dob.getString("date")

                tmpUser.email = tmpUserJSON.getString("email")
                tmpUser.phone = tmpUserJSON.getString("phone")
                val picture: JSONObject = tmpUserJSON.get("picture") as JSONObject  //JSON Picture Object
                tmpUser.thumbnail = picture.getString("thumbnail")
                tmpUser.largeImage = picture.getString("large")
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        tmpUserList.add(tmpUser)
        }
        return tmpUserList
    }

}