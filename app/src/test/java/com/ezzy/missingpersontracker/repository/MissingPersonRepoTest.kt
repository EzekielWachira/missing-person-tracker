package com.ezzy.missingpersontracker.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ezzy.core.data.datasource.MissingPersonDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import java.net.URI
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MissingPersonRepoTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    /**
     * A unit test that tests that saving a missing person
     * returns an id of the person
     * */
    @Test
    suspend fun `add missing person returns id`() {
        val documentId = "hb2djihijh7i88"
        val testMissingPerson = MissingPerson(
            "Test",
            "test",
            "test",
            "black",
            "Teenager",
            "13",
            "male",
            1.5f,
            56.3f,
            "Test descriptuion",
            "1234567",
            false,
            System.currentTimeMillis()
        )
        val testAddress = Address("Test Country", "Test city", "test state", "test street")
        val sampleContacts = listOf(
            Contact("Test Name", "012345678", null),
            Contact("Test Name 2", null, "testmail@gmail.com")
        )
        val testImages = listOf(
            URI.create("https://testmage/image1.png"),
            URI.create("https://testmage/image2.png"),
            URI.create("https://testmage/image3.png")
        )
        val testFileNames = listOf("image1.png", "image2.png", "image3.png")

        val missingPersonRepo = FakeMissingPersonRepo()
        val id = missingPersonRepo.addAMissingPerson(
            testMissingPerson,
            testAddress,
            sampleContacts,
            testImages,
            testFileNames
        )

        assertEquals(id, documentId)

    }



}

class FakeMissingPersonRepo : MissingPersonDataSource {
    override suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>,
        fileNames: List<String>
    ): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMissingPerson(name: String): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMissingPersonByFirstName(name: String): Flow<Resource<List<MissingPerson>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMissingPeople(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFoundPeople(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMissingPersonId(missingPerson: MissingPerson): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMissingPersonImages(missingPerson: MissingPerson): Flow<Resource<List<Image>>> {
        TODO("Not yet implemented")
    }

    override suspend fun reportFoundPerson(
        missingPerson: MissingPerson,
        address: Address
    ): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }
}

object LiveDataTestUtil {

    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS) // wait 2 seconds for the live data to be emitted

        @Suppress("UNCHECKED_CAST")
        return data[0] as T
    }
}