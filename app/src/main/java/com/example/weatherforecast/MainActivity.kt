package com.example.weatherforecast

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weatherforecast.database.entity.Bookmark
import com.example.weatherforecast.databinding.MainActivityBinding
import com.example.weatherforecast.ui.help.HelpFragment
import com.example.weatherforecast.ui.bookmark.BookMarkFragment
import com.example.weatherforecast.ui.bookmarkonmap.MapFragment
import com.example.weatherforecast.ui.city.CityFragment

class MainActivity : AppCompatActivity(), MapFragment.MapListener,
    BookMarkFragment.BookMarkListener {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        initializeView()
    }

    private fun initializeView() {
        addFragment(BookMarkFragment.newInstance(), BookMarkFragment.BOOKMARK_FRAGMENT)
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        if (fragment is BookMarkFragment) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag).commit()

        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag).addToBackStack(null).commit()

        }
    }

    override fun onFabClick() {
        addFragment(MapFragment.newInstance(), MapFragment.MAP_FRAGMENT)
    }

    override fun onLocationBookMarked(bookmark: Bookmark) {
        val fragment: BookMarkFragment =
            supportFragmentManager.findFragmentByTag(BookMarkFragment.BOOKMARK_FRAGMENT) as BookMarkFragment
        fragment.let {
            fragment.addBookMarkedLocation(bookmark)
        }

    }

    override fun onCitySelected(bookMark: Bookmark) {
        val cityFragment = CityFragment.newInstance()
        cityFragment.arguments = Bundle().apply {
            putParcelable(CityFragment.CITY, bookMark)
        }
        addFragment(cityFragment, CityFragment.CITY_FRAGMENT)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.help) {
            addFragment(HelpFragment.newInstance(), HelpFragment.HELP_FRAGMENT)
        }

        return super.onOptionsItemSelected(item)
    }

}