package com.shanonim.senty.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shanonim.senty.R

/**
 * Fragment for MyPage
 */
class MyPageFragment : Fragment() {
    companion object {
        fun getInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }
}
