package com.example.csun_markers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SpotListAdapter : ListAdapter<Spot, SpotViewHolder>(SpotComparator()), Filterable {

    // ***** Initialize Interface for Clicking *****
    private lateinit var myListener: ItemListener

    interface ItemListener {

        fun onSpotClick(spot: Spot, position: Int)

        fun onDeleteClick(spot: Spot, position: Int)

        fun onGoClick(spot: Spot)
    }

    fun setListener(listener: ItemListener) {
        myListener = listener
    }
    // ***** Initialize Interface for Clicking *****

    private lateinit var originalList : List<Spot> // List of Spots, without a search query attached.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotViewHolder {
        return SpotViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SpotViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem.title, currentItem.description, currentItem.imgView)

        holder.itemView.setOnClickListener {
            println(" _____ Clicking spot at position $position _____")
            myListener.onSpotClick(currentItem, position)
        }

        holder.deleteButton.setOnClickListener {
            println(" _____ Deleting spot at position $position _____")
            myListener.onDeleteClick(currentItem, position)
        }

        holder.goButton.setOnClickListener {
            println(" _____ Going to spot at position $position _____")
            myListener.onGoClick(currentItem)
        }
    }

    // ----- Return a list of Spots -----
    override fun submitList(list: List<Spot>?) {
        originalList = list ?: listOf()
        super.submitList(list)
    }

    // ----- Return the results of filtering using p0, the search query. -----
    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {

                val filteredList =
                    if (p0.isNullOrEmpty()) originalList // Empty Query? Filtered = Original!
                else {
                    val filterQuery = p0.toString().lowercase().trim()

                    originalList.filter {
                        it.title.lowercase().contains(filterQuery) // Filter according to a Spot's title.
                    }
                }

                // Return results as the values from the newly created Filtered List.
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            // ----- Submit the filter results as a List of Spots -----
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                submitList(p1?.values as List<Spot>)
            }

        }

    }

}

class SpotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val spotTitle: TextView = itemView.findViewById(R.id.tv_spot_title_item)
    private val spotDesc: TextView = itemView.findViewById(R.id.tv_spot_desc_item)
    private val spotImage: ImageView = itemView.findViewById(R.id.tv_spot_image)
    val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete_item)
    val goButton: ImageButton = itemView.findViewById(R.id.button_go_item)

    // ----- Bind layout elements to the given title, desc, and image -----
    fun bind(titleText: String?, descText: String?, image: Int?) {
        spotTitle.text = titleText
        spotDesc.text = descText
        if (image != null) {
            spotImage.setImageResource(image)
        }
    }

    // Ensure the creation of each item uses the spot item view layout!
    companion object {
        fun create(parent: ViewGroup) : SpotViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.spot_item_view, parent, false)
            return SpotViewHolder(view)
        }
    }
}

// ----- Comparator class makes it easy to tell if two Spots are the same or have similar contents! -----
class SpotComparator : DiffUtil.ItemCallback<Spot>() {
    override fun areItemsTheSame(oldItem: Spot, newItem: Spot): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Spot, newItem: Spot): Boolean {
        return oldItem.title == newItem.title && oldItem.description == newItem.description
    }

}