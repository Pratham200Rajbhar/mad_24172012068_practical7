package com.example.practical7

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practical7.R
import com.example.practical7.DatabaseHelper
import com.example.practical7.Person
import com.example.practical7.MapActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PersonAdapter(private val items: MutableList<Person>) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
        val phoneText: TextView = view.findViewById(R.id.phoneText)
        val emailText: TextView = view.findViewById(R.id.emailText)
        val addressText: TextView = view.findViewById(R.id.addressText)
        val deleteFab: FloatingActionButton = view.findViewById(R.id.deleteFab)
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

        holder.deleteFab.setOnClickListener { v ->
            val ctx = v.context
            val db = DatabaseHelper(ctx)
            db.deletePerson(p)
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }

        // clicking entire item will open MapActivity
        holder.itemView.setOnClickListener { v ->
            val ctx = v.context
            val intent = Intent(ctx, MapActivity::class.java)
            intent.putExtra("Object", p) // pass Person as Serializable
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newList: List<Person>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
