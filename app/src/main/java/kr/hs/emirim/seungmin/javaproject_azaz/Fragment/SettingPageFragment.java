package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import kr.hs.emirim.seungmin.javaproject_azaz.DetailPageActivity;
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

    private ConstraintLayout set_page_fragment;

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
                            review_count.setClickable(true);

                        } else {
                            review_count.setText("작성한 리뷰 (0)");
                            review_count.setClickable(false);
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
        set_page_fragment = mView.findViewById(R.id.set_page_fragment_container);

        action_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("로그아웃").setMessage("정말 로그아웃 하시겠습니까?");

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                final AlertDialog alertDialog = builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    }
                });
                alertDialog.show();
            }
        });

        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_page_fragment.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.set_page_fragment_container,SettingFragment).commit();
                button_click_false();
            }
        });

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_page_fragment.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.set_page_fragment_container,HelpFragment).commit();
                button_click_false();
            }
        });

        review_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_page_fragment.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.set_page_fragment_container,new MyReviewFragment()).commit();
                button_click_false();
            }
        });

        return mView;
    }

    private void button_click_false() {
        review_count.setClickable(false);
        update_user.setClickable(false);
        help_btn.setClickable(false);
        action_logout.setClickable(false);

    }

    private void initial() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }
}