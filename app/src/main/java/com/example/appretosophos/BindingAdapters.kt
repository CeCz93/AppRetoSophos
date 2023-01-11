package com.example.appretosophos

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appretosophos.data.AddedDocument
import com.example.appretosophos.model.DocumentsApiStatus
import com.example.appretosophos.viewdocuments.ViewDocumentsAdapter


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<AddedDocument>?) {
    val adapter = recyclerView.adapter as ViewDocumentsAdapter
    adapter.submitList(data)
}

@BindingAdapter("documentsApiStatus")
fun bindStatus(statusImageView: ImageView, status: DocumentsApiStatus) {
    when (status) {
        // Visible while loading with loading animation
        DocumentsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        // Visible when error with error image
        DocumentsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        // Invisible when done
        DocumentsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
