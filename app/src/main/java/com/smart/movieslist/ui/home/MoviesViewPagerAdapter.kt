package com.smart.movieslist.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smart.movieslist.data.model.GenreModel
import com.smart.movieslist.ui.moviesFragment.MoviesFragment
import com.smart.movieslist.utils.Constants

class MoviesViewPagerAdapter(fragment: Fragment, private val genreList: List<GenreModel>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = genreList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = MoviesFragment()
        val genre = genreList[position]
        fragment.arguments = Bundle().apply {
            if(genre.id!=0)
                putInt(Constants.GENRE_ID, genre.id)
        }
        return fragment
    }
}