package com.example.weatherforecast.ui.bookmark

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherforecast.database.AppDatabase
import com.example.weatherforecast.database.entity.Bookmark
import com.example.weatherforecast.network.Resource
import com.example.weatherforecast.network.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class BookMarkViewModel(application: Application) : AndroidViewModel(application) {

    val bookMarkLiveData: LiveData<Resource<List<Bookmark>>>
    get() = bookMarkData

    private val bookMarkData = MutableLiveData<Resource<List<Bookmark>>>()

    val deleteLiveData: LiveData<Resource<Bookmark>>
    get() = deleteData

    private val deleteData = MutableLiveData<Resource<Bookmark>>()

    /**
     * launching new coroutine to fetch bookmark list
     */
    fun getBookMarked() {
        bookMarkData.value = Resource(status = Status.Loading,data = null, message = null)
        viewModelScope.launch (Dispatchers.IO){
            try {
                bookMarkData.postValue(Resource(status = Status.Success,data = getBookMarkedLocations(), message = null))
            }catch (exception:Exception){
                bookMarkData.postValue(Resource(status = Status.Error,data = null, message = "Error Occurred"))
            }
        }
    }

    /**
     * launching new coroutine to delete
     * @param bookMark
     */
    fun deleteBookMark(bookMark:Bookmark){
        deleteData.value = Resource(status = Status.Loading,data = null, message = null)

        viewModelScope.launch (Dispatchers.IO){
            try {
                deleteBookMarkLocation(bookMark)
                deleteData.postValue(Resource(status = Status.Success,data = bookMark, message = null))
            }catch (exception:Exception){
                deleteData.postValue(Resource(status = Status.Error,data = null, message = "Error Occurred"))
            }
        }
    }

    private suspend fun getBookMarkedLocations() = AppDatabase(context = getApplication()).getBookmarksDao().getBookmarks()

    private suspend fun deleteBookMarkLocation(bookMark: Bookmark) = AppDatabase(context = getApplication()).getBookmarksDao().delete(bookMark)

}
