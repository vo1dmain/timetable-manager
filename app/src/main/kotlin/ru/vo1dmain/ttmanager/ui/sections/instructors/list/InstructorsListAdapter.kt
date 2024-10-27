package ru.vo1dmain.ttmanager.ui.sections.instructors.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import ru.vo1dmain.ttmanager.data.DatabaseEntity.Companion.INVALID_ID
import ru.vo1dmain.ttmanager.data.entities.instructors.Instructor
import ru.vo1dmain.ttmanager.databinding.CardInstructorBinding
import ru.vo1dmain.ttmanager.ui.common.selection.SelectableListAdapter
import ru.vo1dmain.ttmanager.ui.common.selection.SelectableViewHolder
import ru.vo1dmain.ttmanager.ui.sections.instructors.list.InstructorsListAdapter.ViewHolder

internal class InstructorsListAdapter :
    SelectableListAdapter<Long, Instructor, ViewHolder>(diffCallback) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardInstructorBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    
    override fun getItemId(position: Int) =
        getItem(position).id.toLong()
    
    override fun getItemKey(position: Int) =
        getItemId(position)
    
    
    internal inner class ViewHolder(private val binding: CardInstructorBinding) :
        SelectableViewHolder<Long>(binding.root) {
        
        override var bindItemId = INVALID_ID.toLong()
        
        override fun onSelectionChanged(selected: Boolean) {
            binding.checkbox.isChecked = selected
        }
        
        
        fun bind(item: Instructor) {
            bindItemId = item.id.toLong()
            isSelected = tracker.isSelected(bindItemId)
            
            with(binding) {
                name.text = item.fullName
                email.text = item.email
                literal.text = item.fullName[0].uppercase()
                
                checkbox.setOnCheckedChangeListener(::onCheckboxChecked)
            }
        }
        
        
        private fun onCheckboxChecked(button: CompoundButton, isChecked: Boolean) {
            binding.root.isChecked = isChecked
            
            when (isChecked) {
                true -> tracker.select(bindItemId)
                false -> tracker.deselect(bindItemId)
            }
            button.postOnAnimation { binding.literal.isVisible = isChecked.not() }
        }
    }
    
    
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