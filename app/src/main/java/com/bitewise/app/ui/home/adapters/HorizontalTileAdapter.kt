package com.bitewise.app.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.R

data class HorizontalFoodTile (
    val id: String,
    val name: String,
    val image: Int
    // add button maybe
)

class HorizontalTileAdapter (
    private val items: MutableList<HorizontalFoodTile>
): RecyclerView.Adapter<HorizontalTileAdapter.TileViewHolder>() {

    class TileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.img_tile)
        val text: TextView = view.findViewById(R.id.txt_tile_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tile_horizontal, parent, false)

        return TileViewHolder(view)
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        val item: HorizontalFoodTile = items[position]
        holder.text.text = item.name
        holder.image.setImageResource(item.image)
    }

    override fun getItemCount(): Int = items.size
    fun addItem(item: HorizontalFoodTile) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun setItems(newItems: List<HorizontalFoodTile>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}