package com.shalenmathew.quotesapp.presentation.viewmodel

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavLikedQuote
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.GetFavQuote
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetIfSameOrEmptyUseCase
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.util.FavQuoteEvent
import com.shalenmathew.quotesapp.presentation.screens.home_screen.util.QuoteEvent
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever


@RunWith(MockitoJUnitRunner::class)
class FavQuoteViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

   private lateinit var favQuoteUseCase: FavQuoteUseCase

   @Mock
   private lateinit var getFavQuote: GetFavQuote

   @Mock
   private lateinit var favLikedQuote: FavLikedQuote

    @Mock private lateinit var updateWidgetUsecase: UpdateWidgetIfSameOrEmptyUseCase

   private lateinit var vm: FavQuoteViewModel


   @Before
   fun setUp(){
       favQuoteUseCase = FavQuoteUseCase(getFavQuote = getFavQuote, favLikedQuote = favLikedQuote)
   }


    @Test
    fun getFavQuote_without_Query_should_return_all_liked_quotes()= runTest{

        val testQuote = Quote(id=1, quote = "test",author="tester",liked = true, updatedAt = 1000L)

        whenever(favQuoteUseCase.getFavQuote.getAllLikedQuotes("")).thenReturn(flowOf(listOf(testQuote)))

        vm = FavQuoteViewModel(favQuoteUseCase = favQuoteUseCase, updateWidgetIfSameOrEmptyUseCase = updateWidgetUsecase)


        val state = vm.favQuoteState.value

        assertEquals(1,state.dataList.size)
        assertEquals("test",state.dataList[0].quote)

    }

    @Test
    fun onEvent_Like_Should_Update_Specific_Quote_To_Unliked()= runTest{

        val likedQuote = Quote(id=1, quote = "test",author="tester",liked = true, updatedAt = 1000L)
        val unlikedQuote = likedQuote.copy(liked = false)

        // for inside getQuote()
        whenever(favQuoteUseCase.getFavQuote.getAllLikedQuotes("")).thenReturn(flowOf(listOf(likedQuote)))

//        for inside Event.Liked
        whenever(favQuoteUseCase.favLikedQuote.saveLikedQuote(likedQuote)).thenReturn(unlikedQuote)
        whenever(updateWidgetUsecase.invoke(any())).thenReturn(Result.success(Unit))

        vm = FavQuoteViewModel(favQuoteUseCase = favQuoteUseCase, updateWidgetIfSameOrEmptyUseCase = updateWidgetUsecase)

        vm.onEvent(FavQuoteEvent.Like(likedQuote))

        val state = vm.favQuoteState.value

        assertEquals(1,state.dataList.size)
        assertEquals(false,state.dataList[0].liked)

    }




}