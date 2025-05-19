package com.example.facecontours

import androidx.compose.ui.geometry.Size
import com.example.facecontours.data.local.DataStoreInterface
import com.example.facecontours.presentation.facecontours.FaceContoursViewModel
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class FaceContoursViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: FaceContoursViewModel
    private lateinit var dataStore: DataStoreInterface

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        dataStore = mock()


        whenever(dataStore.isFirstLaunchFlow).thenReturn(flowOf(true))

        viewModel = FaceContoursViewModel(dataStore)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isFirstLaunchFlow returns true initially`() = testScope.runTest {
        assertTrue(viewModel.isFirstLaunch.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `isFirstLaunchFlow returns false after first launch`() = testScope.runTest {
        val isFirstLaunchFlow = MutableStateFlow(true)
        whenever(dataStore.isFirstLaunchFlow).thenReturn(isFirstLaunchFlow)
        whenever(dataStore.setFirstLaunchDone()).thenAnswer {
            isFirstLaunchFlow.value = false
        }

        viewModel = FaceContoursViewModel(dataStore)

        assertTrue(viewModel.isFirstLaunch.value)
        viewModel.setOnboardingShown()

        advanceUntilIdle()
        assertFalse(viewModel.isFirstLaunch.value)
    }

    @Test
    fun `updateFace should update face list`() {

        val faces1 = mock<Face>()
        val faces2 = mock<Face>()

        viewModel.updateFaces(listOf(faces1, faces2))

        val result = viewModel.faces.value
        assertEquals(2, result.size)
        assert(result.contains(faces1))
        assert(result.contains(faces2))
    }

    @Test
    fun `val isFaceDetected should be false when list of faces is empty`() {
        viewModel.updateFaces(emptyList())

        val result = viewModel.isFacesDetected
        assertFalse(result)
    }

    @Test
    fun `val isFaceDetected should be true when list of faces is not empty`() {
        val faces1 = mock<Face>()
        val faces2 = mock<Face>()

        viewModel.updateFaces(listOf(faces1, faces2))

        val result = viewModel.isFacesDetected
        assertTrue(result)
    }

    @Test
    fun `updateImageSize should update imageSize`() {

        viewModel.updateImageSize(100, 100)

        val result = viewModel.imageSize.value
        assertEquals(Size(100f, 100f), result)

    }


}
