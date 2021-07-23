package com.example.weatherforecast.ui.bookmark

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.database.AppDatabase
import com.example.weatherforecast.database.entity.Bookmark
import com.example.weatherforecast.database.hideProgress
import com.example.weatherforecast.database.showProgress
import com.example.weatherforecast.databinding.BookMarkFragmentBinding
import com.example.weatherforecast.network.Status
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

/**
 * Created by Harish on 16-07-2021
 */
class BookMarkFragment : Fragment() {
    lateinit var binding: BookMarkFragmentBinding
    private lateinit var listener: BookMarkListener
    private lateinit var bookMarkAdapter: BookMarkAdapter
    private lateinit var viewModel: BookMarkViewModel

    companion object {
        fun newInstance() = BookMarkFragment()

        const val BOOKMARK_FRAGMENT = "bookMarkFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BookMarkFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookMarkAdapter = BookMarkAdapter(ArrayList(),
            onItemClick = { bookMark ->
                listener.onCitySelected(bookMark = bookMark)
            },

            onItemDeleteClick = { bookMark ->
                activity?.let {
                    showDialog(context = it,bookMark = bookMark)
                }
            })

        binding.rvBookmarks.adapter = bookMarkAdapter
        binding.rvBookmarks.layoutManager = LinearLayoutManager(activity)

        binding.fab.setOnClickListener {
            if (::listener.isInitialized) {
                listener.onFabClick()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BookMarkViewModel::class.java)

        activity?.let {

            viewModel.bookMarkLiveData.observe(it) { resource ->
                when (resource.status) {

                    Status.Loading -> {
                            binding.progressHorizontal.showProgress()
                    }

                    Status.Success -> {
                        resource.data?.let {bookMarkList->
                            if (bookMarkList.isNotEmpty()){
                                binding.tvEmptyMsg.isVisible = false
                                binding.rvBookmarks.isVisible = true
                                setBookMarkData(resource.data)
                            }else{
                                binding.tvEmptyMsg.isVisible = true
                            }
                        }
                        binding.progressHorizontal.hideProgress()
                    }

                    Status.Error -> {
                        binding.tvEmptyMsg.isVisible = false
                        binding.progressHorizontal.hideProgress()
                        Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
                    }

                }
            }

            viewModel.deleteLiveData.observe(it) { resource ->
                when (resource.status) {

                    Status.Loading -> {

                    }

                    Status.Success -> {
                        resource.data?.let {bookMark->
                            bookMarkAdapter.removeItem(bookMark)
                        }
                    }

                    Status.Error -> {
                        Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
                    }

                }
            }
            viewModel.getBookMarked()
        }
    }

    override fun onResume() {
        activity?.title = getString(R.string.app_name)
        super.onResume()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser){
            activity?.title = getString(R.string.app_name)
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun setBookMarkData(bookMarkList: List<Bookmark>) {
        if (::bookMarkAdapter.isInitialized) {
            bookMarkAdapter.addData(bookMarkList)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as BookMarkListener
        } catch (exception: Exception) {

        }
    }

    fun addBookMarkedLocation(bookMark: Bookmark){
        bookMarkAdapter.addData(listOf(bookMark))
    }

    private fun showDialog(context: Context, bookMark: Bookmark) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Do you want to delete bookmarked location ${bookMark.city}?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            viewModel.deleteBookMark(bookMark = bookMark)
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    interface BookMarkListener {
        fun onFabClick()
        fun onCitySelected(bookMark: Bookmark)
    }

}