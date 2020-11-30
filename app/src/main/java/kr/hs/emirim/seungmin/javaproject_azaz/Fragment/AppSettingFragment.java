package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kr.hs.emirim.seungmin.javaproject_azaz.R;

public class AppSettingFragment extends Fragment {

    private ImageView back_main;
    private Fragment SettingPageFragment;

    public AppSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_app_setting, container, false);

        back_main = v.findViewById(R.id.back_main);
        SettingPageFragment = new SettingPageFragment();

        back_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.set_page_fragment_container, SettingPageFragment).commit();
            }
        });




        return v;
    }
}