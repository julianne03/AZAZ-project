package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kr.hs.emirim.seungmin.javaproject_azaz.R;
import kr.hs.emirim.seungmin.javaproject_azaz.imgBanerFragment.Banner1Fragment;


public class Reccomend1Fragment extends Fragment {

    ImageView imgBanner1;
    Fragment Banner1Fragment;

    public Reccomend1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reccomend1, container, false);

        imgBanner1 = v.findViewById(R.id.imgBanner1);
        Banner1Fragment = new Banner1Fragment();

        imgBanner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.category_container, Banner1Fragment).commit();
            }
        });

        return v;
    }
}