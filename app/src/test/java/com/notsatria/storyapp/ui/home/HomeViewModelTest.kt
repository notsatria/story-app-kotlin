package com.notsatria.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.notsatria.storyapp.DataDummy
import com.notsatria.storyapp.MainDispatcherRule
import com.notsatria.storyapp.data.preferences.UserPreference
import com.notsatria.storyapp.data.remote.response.StoryItem
import com.notsatria.storyapp.data.repository.StoryRepository
import com.notsatria.storyapp.getOrAwaitValue
import com.notsatria.storyapp.ui.adapter.StoryItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var userPreference: UserPreference

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<StoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(storyRepository, userPreference)
        val actualStory: PagingData<StoryItem> = homeViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryItemAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoryItem>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedQuote)
        val homeViewModel = HomeViewModel(storyRepository, userPreference)
        val actualQuote: PagingData<StoryItem> = homeViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryItemAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }

}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryItem>>>() {
    companion object {
        fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}