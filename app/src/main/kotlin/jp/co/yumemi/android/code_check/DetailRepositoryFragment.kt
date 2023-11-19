/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentDetailRepositoryBinding

class DetailRepositoryFragment : Fragment(R.layout.fragment_detail_repository) {

    private val args: DetailRepositoryFragmentArgs by navArgs()
    private var _binding: FragmentDetailRepositoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailRepositoryBinding.bind(view)
        with(binding) {
            val item = args.item

            ownerIconView.load(item.ownerIconUrl) {
                listener(onError = { _, _ ->
                    Log.e("DetailRepositoryFragment", "画像のロードに失敗しました。")
                })
            }
            nameView.text = item.name
            languageView.text = item.language
            starsView.text = getString(R.string.stars_count, item.stargazersCount)
            watchersView.text = getString(R.string.watchers_count, item.watchersCount)
            forksView.text = getString(R.string.forks_count, item.forksCount)
            openIssuesView.text = getString(R.string.open_issues_count, item.openIssuesCount)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
