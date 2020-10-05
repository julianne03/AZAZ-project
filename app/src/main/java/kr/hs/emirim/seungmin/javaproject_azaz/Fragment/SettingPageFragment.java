package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import kr.hs.emirim.seungmin.javaproject_azaz.R;

public class SettingPageFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private TextView review_count;


    public SettingPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_setting_page, container, false);

        initial();

        final String currentUserId = firebaseAuth.getCurrentUser().getUid();
        review_count = mView.findViewById(R.id.review_count);

        firebaseFirestore.collection("Users/"+ currentUserId + "/reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(!value.isEmpty()) {
                    int count = value.size();
                    review_count.setText("작성한 리뷰 ("+count+")");

                } else {
                    review_count.setText("작성한 리뷰 (0)");
                }
            }
        });

        return mView;
    }

    private void initial() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }
}