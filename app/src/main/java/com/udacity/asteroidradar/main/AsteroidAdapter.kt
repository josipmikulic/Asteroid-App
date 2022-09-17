package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.databinding.ItemListAsteroidBinding

class AsteroidAdapter(private val onAsteroidClickListener: (asteroid: Asteroid) -> Unit) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidVh>(AsteroidDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidVh {
        return AsteroidVh(
            ItemListAsteroidBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onAsteroidClickListener
        )
    }

    override fun onBindViewHolder(holder: AsteroidVh, position: Int) {
        val item = getItem(position) as Asteroid
        holder.bind(item)
    }

    class AsteroidVh(
        private val binding: ItemListAsteroidBinding,
        private val onAsteroidClickListener: (asteroid: Asteroid) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Asteroid) {
            binding.root.setOnClickListener {
                onAsteroidClickListener(item)
            }
            binding.asteroid = item
            binding.executePendingBindings()
        }
    }

}

class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {

    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }

}
