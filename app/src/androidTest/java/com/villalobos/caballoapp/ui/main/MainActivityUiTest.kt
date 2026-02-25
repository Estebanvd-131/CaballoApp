package com.villalobos.caballoapp.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.villalobos.caballoapp.R
import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {

    @Test
    fun mainScreen_displaysPrimaryActions() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.getSharedPreferences("tutorial_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("no_mostrar_tutorial", true)
            .commit()

        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btnIniciar)).check(matches(isDisplayed()))
            onView(withId(R.id.btnAccesibilidad)).check(matches(isDisplayed()))
            onView(withId(R.id.btnCreditos)).check(matches(isDisplayed()))
        }
    }
}
