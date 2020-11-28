package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kr.hs.emirim.seungmin.javaproject_azaz.R;
import kr.hs.emirim.seungmin.javaproject_azaz.imgBanerFragment.Banner2Fragment;


public class Reccomend2Fragment extends Fragment {

    ImageView imgBanner2;
    Fragment Banner2Fragment;

    public Reccomend2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reccomend2, container, false);

        imgBanner2 = v.findViewById(R.id.imgBanner2);
        Banner2Fragment = new Banner2Fragment();

        imgBanner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.category_container, Banner2Fragment).commit();
            }
        });

        return v;
    }
}