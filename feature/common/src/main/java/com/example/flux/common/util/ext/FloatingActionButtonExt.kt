package com.example.flux.common.util.ext

import com.google.android.material.floatingactionbutton.FloatingActionButton

object FloatingActionButtonExt {
    fun FloatingActionButton.showWithDelayed() {
        postDelayed(
            {
                show()
            },
            300,
        )
    }
}
