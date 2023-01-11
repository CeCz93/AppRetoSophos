package com.example.appretosophos.viewdocuments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appretosophos.data.AddedDocument
import com.example.appretosophos.databinding.DocumentItemBinding

class ViewDocumentsAdapter(private val navController: NavController) : ListAdapter<AddedDocument,
        ViewDocumentsAdapter.AddedDocumentViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<AddedDocument>() {

        override fun areItemsTheSame(oldItem: AddedDocument, newItem: AddedDocument): Boolean {
            return oldItem.documentId == newItem.documentId
        }

        override fun areContentsTheSame(oldItem: AddedDocument, newItem: AddedDocument): Boolean {
            return oldItem.documentId == newItem.documentId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            AddedDocumentViewHolder {
        return AddedDocumentViewHolder(
            DocumentItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: AddedDocumentViewHolder, position: Int) {
        val addedDocument = getItem(position)
        holder.bind(addedDocument)
        holder.binding.buttonViewFile.setOnClickListener {
            val registry = addedDocument.documentId
            val action = ViewDocumentsFragmentDirections.actionViewDocumentsFragmentToDisplayDocumentFragment(documentID = registry)
            navController.navigate(action)
        }
        holder.binding.root.context
    }

    class AddedDocumentViewHolder(var binding: DocumentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(AddedDocument: AddedDocument) {
            binding.document = AddedDocument
            binding.executePendingBindings()

        }
    }
}
