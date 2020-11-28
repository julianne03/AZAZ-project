package kr.hs.emirim.seungmin.javaproject_azaz.imgBanerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.hs.emirim.seungmin.javaproject_azaz.R;

public class Banner1Fragment extends Fragment {


    public Banner1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_banner1, container, false);
    }
}