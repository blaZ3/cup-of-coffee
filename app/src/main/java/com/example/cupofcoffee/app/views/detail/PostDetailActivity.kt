package com.example.cupofcoffee.app.views.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailActivity : BaseActivity<PostDetailViewState>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var data = intent.extras
        if (savedInstanceState != null) {
            data = savedInstanceState
        }
        val post: Post? = data?.getParcelable(EXTRA_POST)
        view = PostDetailView(this, post, viewState).also {
            setContentView(it)
        }
    }

    companion object {
        private const val EXTRA_POST = "extra_post"
        fun getIntent(context: Context, post: Post): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra(EXTRA_POST, post)
            }
        }
    }
}
