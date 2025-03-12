package ru.vo1d.ttmanager.ui.sections.subjects.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vo1d.ttmanager.data.entities.subjects.Subject
import ru.vo1d.ttmanager.databinding.CardSubjectBinding
import ru.vo1d.ttmanager.ui.sections.subjects.list.SubjectsListAdapter.ViewHolder

internal class SubjectsListAdapter : ListAdapter<Subject, ViewHolder>(diffCallback) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardSubjectBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        
        holder.binding.title.text = item.title
    }
    
    override fun getItemId(position: Int) =
        getItem(position).id.toLong()
    
    
    internal class ViewHolder(internal val binding: CardSubjectBinding) :
        RecyclerView.ViewHolder(binding.root)
    
    
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Subject>() {
            override fun areItemsTheSame(old: Subject, new: Subject) =
                old.id == new.id
            
            override fun areContentsTheSame(old: Subject, new: Subject) =
                old == new
        }
    }
    
    init {
        setHasStableIds(true)
    }
}