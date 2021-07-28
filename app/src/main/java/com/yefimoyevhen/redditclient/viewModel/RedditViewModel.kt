package com.yefimoyevhen.redditclient.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yefimoyevhen.redditclient.model.Entry
import com.yefimoyevhen.redditclient.repository.RedditRepository
import com.yefimoyevhen.redditclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedditViewModel
@Inject
constructor(
    private val repository: RedditRepository,
) : ViewModel() {

    var resourceLiveData = MutableLiveData<Resource<List<Entry>>>()

    init {
        fetchData()
        resourceLiveData = repository.resourceLiveData
    }

    fun fetchData(isRefresh: Boolean = false) {
        viewModelScope.launch {
            repository.fetchData(isRefresh)
        }
    }
}