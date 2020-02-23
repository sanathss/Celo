package com.example.celo.recycler

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class RecyclerViewAdapterTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
        fragment = MyFragment()

        activityController = Robolectric.buildActivity(FragmentActivity::class.java)

        activityController.create().start().resume()

        activityController.get()
            .supportFragmentManager
            .beginTransaction()
            .add(fragment, null)
            .commit()
    }

    @Test
    @Throws(Exception::class)
    fun testClickEntry() {
        val recycler = fragment.view!!.findViewById(R.id.sideNavigationRecycler) as RecyclerView
        // workaround robolectric recyclerView issue
        recycler.measure(0,0)
        recycler.layout(0,0,100,1000)

        // lets just imply a certain item at position 0 for simplicity
        recycler.findViewHolderForAdapterPosition(0).itemView.performClick()

        // presenter is injected in my case, how this verification happens depends on how you manage your dependencies.
        verify(fragment.presenter).onEntryClicked(MyNavigationEntry.XYZ)
    }
}