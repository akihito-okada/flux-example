package com.example.flux.common.util.ext

import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder

object GroupAdapterExt {
    fun GroupAdapter<GroupieViewHolder<*>>.refresh(newGroups: Collection<Group?>) {
        clear()
        update(newGroups, false)
    }
}
