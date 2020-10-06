package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.hs.emirim.seungmin.javaproject_azaz.R;

public class InventoryFragment extends Fragment {

    private TextView favorite_drawer;
    Fragment FavoriteFragment;

    public InventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_inventory, container, false);
        FavoriteFragment = new FavoriteFragment();

        favorite_drawer = mView.findViewById(R.id.favorite_drawer);

        favorite_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.inventory_fragment_container,FavoriteFragment).commit();
            }
        });

        return mView;
    }
}