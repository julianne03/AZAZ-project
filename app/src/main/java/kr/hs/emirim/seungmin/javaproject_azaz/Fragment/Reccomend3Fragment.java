package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kr.hs.emirim.seungmin.javaproject_azaz.R;
import kr.hs.emirim.seungmin.javaproject_azaz.imgBanerFragment.Banner2Fragment;
import kr.hs.emirim.seungmin.javaproject_azaz.imgBanerFragment.Banner3Fragment;

public class Reccomend3Fragment extends Fragment {

    ImageView imgBanner3;
    Fragment Banner3Fragment;

    public Reccomend3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reccomend3, container, false);

        imgBanner3 = v.findViewById(R.id.imgBanner3);
        Banner3Fragment = new Banner3Fragment();

        imgBanner3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.category_container, Banner3Fragment).commit();
            }
        });

        return v;
    }
}