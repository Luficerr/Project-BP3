package com.pab.funsplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Button
import com.pab.funsplash.R

class LoadingFooterAdapter(
    private val retry: () -> Unit
) : RecyclerView.Adapter<LoadingFooterAdapter.FooterViewHolder>() {

    enum class State {
        LOADING,
        ERROR,
        IDLE
    }

    private var state: State = State.IDLE

    fun setState(newState: State) {
        state = newState
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.footer_load_state, parent, false)
        return FooterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) {
        holder.bind(state)
    }

    override fun getItemCount(): Int {
        return if (state == State.IDLE) 0 else 1
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progress = itemView.findViewById<ProgressBar>(R.id.progressBar)
        private val errorText = itemView.findViewById<TextView>(R.id.tvError)
        private val retryBtn = itemView.findViewById<Button>(R.id.btnRetry)

        fun bind(state: State) {
            progress.visibility = View.GONE
            errorText.visibility = View.GONE
            retryBtn.visibility = View.GONE

            when (state) {
                State.LOADING -> {
                    progress.visibility = View.VISIBLE
                }
                State.ERROR -> {
                    errorText.visibility = View.VISIBLE
                    retryBtn.visibility = View.VISIBLE
                    retryBtn.setOnClickListener { retry() }
                }
                State.IDLE -> Unit
            }
        }
    }
}