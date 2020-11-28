package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kr.hs.emirim.seungmin.javaproject_azaz.Adapter.PagerAdapter;
import kr.hs.emirim.seungmin.javaproject_azaz.R;
import kr.hs.emirim.seungmin.javaproject_azaz.category.ElsethingFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.category.ExerciseFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.category.FocusFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.category.HeadFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.category.HearingFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.category.ImagineFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.category.LanguageFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.category.SightFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.category.TouchFragment;
import me.relex.circleindicator.CircleIndicator3;

public class RecommendFragment extends Fragment implements View.OnClickListener {

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    private CircleIndicator3 mIndicator;

    private ImageView re_sight;
    private ImageView re_touch;
    private ImageView re_language;
    private ImageView re_focus;
    private ImageView re_head;
    private ImageView re_hear;
    private ImageView re_imagine;
    private ImageView re_etc;
    private ImageView re_exercise;

    private ImageView home_btn;

    private Fragment sightFragment;
    private Fragment touchFragment;
    private Fragment languageFragment;
    private Fragment focusFragment;
    private Fragment headFragment;
    private Fragment hearingFragment;
    private Fragment imagineFragment;
    private Fragment elsethFragment;
    private Fragment exerciseFragment;

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
        mPager.setCurrentItem(0,true);
        mPager.setOffscreenPageLimit(2);
        mPager.setSaveEnabled(false);

        //category initial
        re_etc = v.findViewById(R.id.re_etc);
        re_exercise = v.findViewById(R.id.re_exercise);
        re_focus = v.findViewById(R.id.re_focus);
        re_head = v.findViewById(R.id.re_head);
        re_hear = v.findViewById(R.id.re_hear);
        re_imagine = v.findViewById(R.id.re_imagine);
        re_language = v.findViewById(R.id.re_language);
        re_sight = v.findViewById(R.id.re_sight);
        re_touch = v.findViewById(R.id.re_touch);
        home_btn = v.findViewById(R.id.home_btn);
        category_init();

        re_sight.setOnClickListener(this);
        re_touch.setOnClickListener(this);
        re_language.setOnClickListener(this);
        re_imagine.setOnClickListener(this);
        re_hear.setOnClickListener(this);
        re_head.setOnClickListener(this);
        re_focus.setOnClickListener(this);
        re_etc.setOnClickListener(this);
        re_exercise.setOnClickListener(this);
        home_btn.setOnClickListener(this);


        return v;
    }

    private void category_init() {
        exerciseFragment = new ExerciseFragment();
        elsethFragment = new ElsethingFragment();
        focusFragment = new FocusFragment();
        headFragment = new HeadFragment();
        hearingFragment = new HearingFragment();
        imagineFragment = new ImagineFragment();
        languageFragment = new LanguageFragment();
        sightFragment = new SightFragment();
        touchFragment = new TouchFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_exercise :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container,exerciseFragment).commit();
                break;
            case R.id.re_sight :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container, sightFragment).commit();
                break;
            case R.id.re_etc :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container, elsethFragment).commit();
                break;
            case R.id.re_focus :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container, focusFragment).commit();
                break;
            case R.id.re_head :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container, headFragment).commit();
                break;
            case R.id.re_hear :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container, hearingFragment).commit();
                break;
            case R.id.re_imagine :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container, imagineFragment).commit();
                break;
            case R.id.re_language :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container, languageFragment).commit();
                break;
            case R.id.re_touch :
                home_btn.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.category_container, touchFragment).commit();
                break;
            case R.id.home_btn :
                Fragment fragment = getFragmentManager().findFragmentById(R.id.category_container);
                if(fragment!=null) {
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                }
                home_btn.setVisibility(View.INVISIBLE);
                break;
            default:
                return;

        }
    }

}