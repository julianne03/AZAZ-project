package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.hs.emirim.seungmin.javaproject_azaz.Adapter.PagerAdapter;
import kr.hs.emirim.seungmin.javaproject_azaz.R;
import me.relex.circleindicator.CircleIndicator3;

public class RecommendFragment extends Fragment {

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    private CircleIndicator3 mIndicator;

    public RecommendFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend, container, false);

        //ViewPager2
        mPager = v.findViewById(R.id.viewpager);
        //Adapter
        pagerAdapter = new PagerAdapter(getActivity(), num_page);
        mPager.setAdapter(pagerAdapter);
        //Indicator
        mIndicator = v.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);
        //ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(1500);
        mPager.setOffscreenPageLimit(3);


        return v;
    }
}