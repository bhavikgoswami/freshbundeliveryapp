package com.greypixstudio.broovisdeliveryapp.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.greypixstudio.broovisdeliveryapp.ui.fragment.DeliveredOrderListFragment
import com.greypixstudio.broovisdeliveryapp.ui.fragment.OnGoingOrdersListFragment

class ViewPagerAdapter (manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager,lifecycle) {
    private val numPages = 2


    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 ->{
                OnGoingOrdersListFragment()
            }
            1 ->{
                DeliveredOrderListFragment()
            }

            else ->  OnGoingOrdersListFragment()
        }
    }

    override fun getItemCount(): Int {
        return  numPages
    }


}

