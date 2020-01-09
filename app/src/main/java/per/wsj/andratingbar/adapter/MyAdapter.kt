package per.wsj.andratingbar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import per.wsj.andratingbar.R
import per.wsj.library.AndRatingBar

class MyAdapter(val mData:List<String>):RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item,null,false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = mData[position]
        holder.andRatingBar.rating = (position%6).toFloat()
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)
        var andRatingBar = itemView.findViewById<AndRatingBar>(R.id.andRatingBar)
    }
}