package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.Help;
import com.mikhaellopez.circularimageview.CircularImageView;

import kr.hs.emirim.seungmin.javaproject_azaz.LoginActivity;
import kr.hs.emirim.seungmin.javaproject_azaz.R;
import kr.hs.emirim.seungmin.javaproject_azaz.SetupActivity;

public class SettingPageFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private CircularImageView user_image;
    private TextView user_name;
    private TextView update_user;
    private TextView help_btn;
    private TextView action_logout;
    private TextView review_count;

    private Fragment SettingFragment;
    private Fragment HelpFragment;

    public SettingPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() != null) {
            final String currentUserId = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users/"+ currentUserId + "/reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error==null) {
                        if(!value.isEmpty()) {
                            int count = value.size();
                            review_count.setText("작성한 리뷰 ("+count+")");

                        } else {
                            review_count.setText("작성한 리뷰 (0)");
                        }
                    }

                }
            });

            firebaseFirestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        if (task.getResult().exists()) {

                            String name = task.getResult().getString("name");
                            String image = task.getResult().getString("image");

                            assert name != null;
                            Log.e("시험", name);
                            assert image != null;
                            Log.e("시험", image);

                            user_name.setText(name+" 님");

                            RequestOptions placeholderRequest = new RequestOptions();
                            placeholderRequest.placeholder(R.drawable.profile);

                            Glide.with(getContext()).setDefaultRequestOptions(placeholderRequest).load(image).into(user_image);
                            Log.e("시험", "ㅎㅎㅎㅎㅎㅎㅎㅎ");


                        }
                    }
                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_setting_page, container, false);
        SettingFragment = new SettingFragment();
        HelpFragment = new HelpFragment();
        initial();


        review_count = mView.findViewById(R.id.review_count);
        action_logout = mView.findViewById(R.id.set_logout_btn);
        user_image = mView.findViewById(R.id.user_profile_image);
        user_name = mView.findViewById(R.id.user_profile_name);
        update_user = mView.findViewById(R.id.update_user);
        help_btn = mView.findViewById(R.id.help_btn);

        action_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.set_page_fragment_container,SettingFragment).commit();
            }
        });

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.set_page_fragment_container,HelpFragment).commit();
            }
        });

        review_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.set_page_fragment_container,new MyReviewFragment()).commit();
            }
        });

        return mView;
    }

    private void initial() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }
}