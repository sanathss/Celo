package com.example.celo.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.celo.R
import com.example.celo.model.User
import com.squareup.picasso.Picasso

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>, Filterable {

    var act: Context? = null
    var users: ArrayList<User>? = null
    var usersFull: ArrayList<User>? = null
    var mfilter: NewFilter

    constructor(recyclerViewActivity: Context, userList: ArrayList<User>) {
        act = recyclerViewActivity
        users = userList
        usersFull = users
    }

    override fun getFilter(): Filter {
        return mfilter
    }

    init {
        mfilter = NewFilter(this@RecyclerViewAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(act).inflate(R.layout.recycler_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Populate viewholder with information from userlist
        val user: User? = users?.get(position)

        val nameComplete: String = user?.title + " " + user?.firstName + " " + user?.lastName
        val gender: String? = user?.gender
        val dateOfBirth: String? = user?.dateOfBirth

        holder.namesView?.setText(nameComplete)
        holder.genderView?.setText(gender)
        holder.dateOfBirthView?.setText(dateOfBirth)

        val thumbURL: String? = user?.thumbnail
        Picasso.get().load(thumbURL).into(holder.thumbnailView)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {        //View showing rows of recyclerview
        var namesView: TextView? = itemView.findViewById(R.id.fullname) as TextView?
        var genderView: TextView? = itemView.findViewById(R.id.gender) as TextView?
        var dateOfBirthView: TextView? = itemView.findViewById(R.id.dateOfBirth) as TextView?
        var thumbnailView: ImageView? = itemView.findViewById(R.id.thumbnail) as ImageView?
    }

    inner class NewFilter(var mAdapter: RecyclerViewAdapter) : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            users = ArrayList<User>()
            val results = FilterResults()
            if (charSequence.length == 0) {
                users!!.addAll(usersFull!!)
            } else {
                //val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                for (i in 0..usersFull!!.size) {
                    val firstName = usersFull!!.get(i).firstName
                    if (firstName!!.startsWith(charSequence, true)) {
                        users!!.add(usersFull!!.get(i))
                    }
                }
            }
            results.values = users
            results.count = users!!.size
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            this.mAdapter.notifyDataSetChanged()
        }
    }

}