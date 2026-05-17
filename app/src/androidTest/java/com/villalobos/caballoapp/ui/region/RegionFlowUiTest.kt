package com.villalobos.caballoapp.ui.region

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.main.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegionFlowUiTest {

    private fun disableTutorial(context: Context) {
        context.getSharedPreferences("tutorial_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("no_mostrar_tutorial", true)
            .commit()
    }

    @Test
    fun regionMenu_buttonsOpenTheirExpectedScreens() {
        ActivityScenario.launch(RegionMenu::class.java).use {
            onView(withId(R.id.btnRegionCabeza)).perform(click())
            onView(withId(R.id.hotspotZona1001)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionMenu::class.java).use {
            onView(withId(R.id.btnRegionCuello)).perform(click())
            onView(withId(R.id.hotspotZona2001)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionMenu::class.java).use {
            onView(withId(R.id.btnRegionTronco)).perform(click())
            onView(withId(R.id.hotspotZona3001)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionMenu::class.java).use {
            onView(withId(R.id.btnRegionToracica)).perform(click())
            onView(withId(R.id.hotspotZona4001)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionMenu::class.java).use {
            onView(withId(R.id.btnRegionPelvica)).perform(click())
            onView(withId(R.id.hotspotZona5001)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionMenu::class.java).use {
            onView(withId(R.id.btnRegionDistal)).perform(click())
            onView(withId(R.id.hotspotZona6001)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun cabezaHotspot_opensExpectedDetailScreen() {
        ActivityScenario.launch(RegionCabeza::class.java).use {
            onView(withId(R.id.hotspotZona1001)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Músculo Temporal")))
            onView(withId(R.id.imgMusculoDetalle)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun otherRegionHotspots_openTheirExpectedDetailScreens() {
        ActivityScenario.launch(RegionCuello::class.java).use {
            onView(withId(R.id.hotspotZona2001)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Músculo Braquiocefálico")))
        }

        ActivityScenario.launch(RegionTronco::class.java).use {
            onView(withId(R.id.hotspotZona3001)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Músculo Trapecio (Torácico)")))
        }

        ActivityScenario.launch(RegionToracica::class.java).use {
            onView(withId(R.id.hotspotZona4001)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Músculo Supraespinoso")))
        }

        ActivityScenario.launch(RegionPelvica::class.java).use {
            onView(withId(R.id.hotspotZona5001)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Músculo Sacrocaudal Dorsal")))
        }

        ActivityScenario.launch(RegionDistal::class.java).use {
            onView(withId(R.id.hotspotZona6001)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Corion de la Ranilla")))
        }
    }

    @Test
    fun toracicaAndPelvica_hotspotsOpenExpectedDetails() {
        ActivityScenario.launch(RegionToracica::class.java).use {
            onView(withId(R.id.hotspotZona4002)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Músculo Infraespinoso")))
        }

        ActivityScenario.launch(RegionToracica::class.java).use {
            onView(withId(R.id.hotspotZona4004)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Tríceps Braquial (Cabeza Lateral)")))
        }

        ActivityScenario.launch(RegionPelvica::class.java).use {
            onView(withId(R.id.hotspotZona5002)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Músculo Glúteo Medio")))
        }

        ActivityScenario.launch(RegionPelvica::class.java).use {
            onView(withId(R.id.hotspotZona5005)).perform(click())
            onView(withId(R.id.tvTituloMusculo)).check(matches(withText("Músculo Sacrocaudal Dorsal")))
        }
    }

    @Test
    fun backNavigation_returnsToExpectedParentScreens() {
        ActivityScenario.launch(RegionToracica::class.java).use {
            pressBack()
            onView(withId(R.id.btnRegionToracica)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionPelvica::class.java).use {
            onView(withId(R.id.hotspotZona5002)).perform(click())
            onView(withId(R.id.btnVolver)).perform(click())
            onView(withId(R.id.hotspotZona5001)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionToracica::class.java).use {
            onView(withId(R.id.btnQuizRegion)).perform(click())
            pressBack()
            onView(withText("Salir")).perform(click())
            onView(withId(R.id.hotspotZona4001)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun regionButtons_coverQuizHomeAndDetailBackFlows() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        disableTutorial(context)

        ActivityScenario.launch(RegionCabeza::class.java).use {
            onView(withId(R.id.btnQuizRegion)).perform(click())
            onView(withId(R.id.tvQuizTitle)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionCabeza::class.java).use {
            onView(withId(R.id.hotspotZona1001)).perform(click())
            onView(withId(R.id.btnVolver)).perform(click())
            onView(withId(R.id.hotspotZona1001)).check(matches(isDisplayed()))
        }

        ActivityScenario.launch(RegionCabeza::class.java).use {
            onView(withId(R.id.btnHome)).perform(click())
            onView(withId(R.id.btnIniciar)).check(matches(isDisplayed()))
        }
    }
}