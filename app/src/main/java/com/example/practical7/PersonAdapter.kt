package com.example.practical7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(private val items: MutableList<Person>) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
        val phoneText: TextView = view.findViewById(R.id.phoneText)
        val emailText: TextView = view.findViewById(R.id.emailText)
        val addressText: TextView = view.findViewById(R.id.addressText)
        val deleteBtn: ImageButton = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(v)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val p = items[position]
        holder.nameText.text = p.name
        holder.phoneText.text = p.phoneNo
        holder.emailText.text = p.emailId
        holder.addressText.text = p.address

        holder.deleteBtn.setOnClickListener { v ->
            val ctx = v.context
            val db = DatabaseHelper(ctx)
            db.deletePerson(p)
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }


    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newList: List<Person>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
