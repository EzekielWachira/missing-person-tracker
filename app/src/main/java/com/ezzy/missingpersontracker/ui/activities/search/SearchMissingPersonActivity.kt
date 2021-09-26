package com.ezzy.missingpersontracker.ui.activities.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonAdapter
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.ActivitySearchMissingPersonBinding
import com.ezzy.missingpersontracker.ui.activities.person_details.PersonDetailsActivity
import com.ezzy.missingpersontracker.ui.activities.run_face_identification.FaceIdentificationActivity
import com.ezzy.missingpersontracker.ui.fragments.home.HomeViewHolder
import com.ezzy.missingpersontracker.util.gone
import com.ezzy.missingpersontracker.util.invisible
import com.ezzy.missingpersontracker.util.showToast
import com.ezzy.missingpersontracker.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class SearchMissingPersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchMissingPersonBinding

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var mAdapter: CommonAdapter<Pair<Pair<MissingPerson, List<Image>>, User>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMissingPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_chevron_left)
        }

        binding.search.requestFocus()

        setUpRecyclerView()
        setUpUI()
        subscribeToUI()
    }

    private fun setUpUI() {
        with(binding) {
            runFace.setOnClickListener {
                startActivity(
                    Intent(
                        this@SearchMissingPersonActivity,
                        FaceIdentificationActivity::class.java
                    )
                )
            }

            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (search.text.isNotEmpty()) {
                        searchViewModel.searchMissingPerson(
                            search.text.toString()
                        )
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (search.text.isNotEmpty()) {
                        Timber.d("SEARCH QUERY: ${s.toString()}")
                        searchViewModel.searchMissingPerson(
                            s.toString()
                        )
                    }
                }
            })
//                searchViewModel.searchMissingPerson(keyword.toString())
        }

        mAdapter.setOnClickListener {
            startActivity(Intent(this, PersonDetailsActivity::class.java).apply {
                putExtra("missingPerson", it?.first?.first)
                putExtra("images", it?.first?.second?.toTypedArray())
            })
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = CommonAdapter {
            HomeViewHolder(it, this)
        }

        binding.searchRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@SearchMissingPersonActivity)
            adapter = mAdapter
            addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
        }
    }

    private fun subscribeToUI() {
        lifecycleScope.launch {
            searchViewModel.missingPeople.collect { state ->
                with(binding) {
                    when (state) {
                        is Resource.Loading -> {
                            layoutNoData.gone()
                            spinKit.visible()
                        }
                        is Resource.Success -> {
                            spinKit.gone()
                            Timber.d("SEARCH RESULTS: ${state.data}")
                            mAdapter.differ.submitList(state.data)
                            searchRecyclerview.visible()
                        }
                        is Resource.Failure -> showToast(state.errorMessage!!)
                        is Resource.Empty -> {
                            layoutNoData.visible()
                            searchRecyclerview.gone()
                        }
                    }
                }
            }
        }
    }

}