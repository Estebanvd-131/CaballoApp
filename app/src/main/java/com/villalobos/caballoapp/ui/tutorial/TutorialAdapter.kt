package com.villalobos.caballoapp.ui.tutorial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TutorialAdapter(
    fragmentActivity: FragmentActivity,
    private val pasos: List<TutorialPaso>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = pasos.size

    override fun createFragment(position: Int): Fragment {
        return TutorialPasoFragment.newInstance(pasos[position])
    }
} 