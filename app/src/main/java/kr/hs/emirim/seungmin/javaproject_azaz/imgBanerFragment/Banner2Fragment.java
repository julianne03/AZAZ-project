package kr.hs.emirim.seungmin.javaproject_azaz.imgBanerFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.RecommendFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.R;

public class Banner2Fragment extends Fragment {

    ImageView back_main;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_banner2, container, false);

        back_main = v.findViewById(R.id.back_main);
        back_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.category_container);
                if(fragment!=null) {
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        });

        v.findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://seoultoy.or.kr/new/main/index.php"));
                startActivity(intent);
            }
        });

        return v;
    }
}