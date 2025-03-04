package com.example.flux.common.delegate

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder
import kotlin.reflect.KProperty

class FragmentGroupAdapterDelegate {
    private var groupAdapter: GroupAdapter<GroupieViewHolder<*>>? = null
    private var lifecycle: Lifecycle? = null

    operator fun getValue(thisRef: Fragment, property: KProperty<*>): GroupAdapter<GroupieViewHolder<*>> {
        return (groupAdapter ?: GroupAdapter()).also {
            groupAdapter = it
            if (thisRef.view == null) {
                // Can't access the Fragment View's LifecycleOwner when getView() is null
                return@also
            }
            lifecycle = thisRef.viewLifecycleOwner.lifecycle.also { lifecycle ->
                lifecycle.removeObserver(observer)
                lifecycle.addObserver(observer)
            }
        }
    }

    private val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyView() {
            lifecycle?.removeObserver(this)
            groupAdapter?.clear()
            groupAdapter = null
        }
    }
}

fun Fragment.groupAdapter(): FragmentGroupAdapterDelegate = FragmentGroupAdapterDelegate()
