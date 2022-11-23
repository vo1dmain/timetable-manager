package ru.vo1d.ttmanager.ui.sections.instructors.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.databinding.CardInstructorBinding
import ru.vo1d.ttmanager.ui.sections.instructors.list.InstructorsListAdapter.ViewHolder

internal class InstructorsListAdapter : ListAdapter<Instructor, ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardInstructorBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.name.text = item.fullName
        holder.binding.email.text = item.email
        holder.binding.literal.text = item.fullName[0].uppercase()
    }

    override fun getItemId(position: Int) =
        getItem(position).id.toLong()


    internal class ViewHolder(internal val binding: CardInstructorBinding) :
        RecyclerView.ViewHolder(binding.root)


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Instructor>() {
            override fun areItemsTheSame(old: Instructor, new: Instructor) =
                old.id == new.id

            override fun areContentsTheSame(old: Instructor, new: Instructor) =
                old == new
        }
    }


    init {
        setHasStableIds(true)
    }
}