package sourabh.pal.mandi.common.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sourabh.pal.mandi.find.presentation.views.PlanetSlidePageFragment

private const val PLANETS_COUNT: Int = 6

class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = PLANETS_COUNT

    override fun createFragment(position: Int): Fragment = PlanetSlidePageFragment(position)
}