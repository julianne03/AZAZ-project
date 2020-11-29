package kr.hs.emirim.seungmin.javaproject_azaz.imgBanerFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.RecommendFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.R;

public class Banner1Fragment extends Fragment implements View.OnClickListener {

    ImageView back_main;

    public Banner1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_banner1, container, false);

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

        v.findViewById(R.id.btn_1).setOnClickListener(this);
        v.findViewById(R.id.btn_2).setOnClickListener(this);
        v.findViewById(R.id.btn_3).setOnClickListener(this);
        v.findViewById(R.id.btn_4).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btn_1 :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://babynoriter.com/"));
                startActivity(intent);
                break;
            case R.id.btn_2 :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.greenkid.co.kr/"));
                startActivity(intent);
                break;
            case R.id.btn_3 :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bigbebeboxtoys.com/"));
                startActivity(intent);
            case R.id.btn_4 :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://seoul.childcare.go.kr/ccef/main.jsp"));
                startActivity(intent);
        }
    }
}