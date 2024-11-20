package ru.vo1dmain.timetables.sections.timetable.sessions.list

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vo1dmain.timetables.data.entities.session.Session
import ru.vo1dmain.timetables.databinding.CardSessionBinding
import ru.vo1dmain.timetables.sections.timetable.sessions.list.SessionsListAdapter.ViewHolder

internal class SessionsListAdapter(private val preferences: SharedPreferences) :
    ListAdapter<Session, ViewHolder>(diffCallback) {
    
    private val editionModeListeners = mutableListOf<(Boolean) -> Unit>()
    
    private var isEditionMode = false
    private var shouldSelectAll = false
    
    private var itemClickListener: (Session) -> Unit = { }
    private var selectionChangedListener: (Session, Boolean) -> Unit = { _, _ -> }
    
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardSessionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    
    }
    
    override fun getItemId(position: Int) =
        getItem(position).id.toLong()
    
    
    @SuppressLint("NotifyDataSetChanged")
    fun selectAll() {
        shouldSelectAll = !shouldSelectAll
        notifyDataSetChanged()
    }
    
    
    internal class ViewHolder(binding: CardSessionBinding) : RecyclerView.ViewHolder(binding.root)
    
    
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Session>() {
            override fun areItemsTheSame(old: Session, new: Session) =
                old.id == new.id
            
            override fun areContentsTheSame(old: Session, new: Session) =
                old == new
        }
    }
    
    
    init {
        setHasStableIds(true)
    }
}