package com.example.weatherforecast.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.database.entity.Bookmark
import com.example.weatherforecast.databinding.ItemBookmarkBinding

/**
 * Created by Harish on 16-07-2021
 */
class BookMarkAdapter(
    private val bookmarks: ArrayList<Bookmark>,
    val onItemClick: (Bookmark) -> Unit,
    val onItemDeleteClick: (Bookmark) -> Unit
) :
    RecyclerView.Adapter<BookMarkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: ItemBookmarkBinding =
            ItemBookmarkBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookmark = bookmarks[position]
        holder.bind(bookmark,onItemClick, onItemDeleteClick)
    }

    override fun getItemCount() = bookmarks.size

    fun addData(bookMarkList: List<Bookmark>){
        bookmarks.addAll(bookMarkList)
        notifyDataSetChanged()
    }

    class ViewHolder(private val itemBookmarkBinding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(itemBookmarkBinding.root) {

        fun bind(
            bookMark: Bookmark,
            onItemClick: (Bookmark) -> Unit,
            onItemDeleteClick: (Bookmark) -> Unit
        ) {
            with(itemBookmarkBinding) {

                tvCity.text = bookMark.city

                tvCity.setOnClickListener {
                    onItemClick(bookMark)
                }

                ivDelete.setOnClickListener {
                    onItemDeleteClick(bookMark)
                }

            }
        }
    }

 fun removeItem(bookMark: Bookmark){
     bookmarks.remove(bookMark)
     notifyDataSetChanged()
 }

}