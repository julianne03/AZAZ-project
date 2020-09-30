package kr.hs.emirim.seungmin.javaproject_azaz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class InventoryFragment extends Fragment {

    private RecyclerView like_review_list_view;
    private List<Review> review_list;
    private List<User> user_list;

    private FirebaseFirestore firebaseFirestore;
    private LikeReviewRecyclerAdapter likeReviewRecyclerAdapter;
    private FirebaseAuth firebaseAuth;

    private TextView review_count;
    private Boolean isFirstPageFirstLoad = true;

    public InventoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        review_list = new ArrayList<>();
        user_list = new ArrayList<>();
        like_review_list_view = view.findViewById(R.id.like_list_view);


        firebaseAuth = FirebaseAuth.getInstance();

        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        likeReviewRecyclerAdapter = new LikeReviewRecyclerAdapter(review_list, user_list);
        like_review_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        like_review_list_view.setAdapter(likeReviewRecyclerAdapter);
        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

        }

        firebaseFirestore.collection("Users/"+ currentUserId + "/reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(!value.isEmpty()) {
                    int count = value.size();
                    review_count = view.findViewById(R.id.user_review_count);
                    review_count.setText(count + "개");

                }
            }
        });


        Query firstQuery = firebaseFirestore.collection("Users/"+currentUserId+"/Likes");

        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Log.e("test","firstQuery OK");

                if(!value.isEmpty()) {

                    if(isFirstPageFirstLoad) {
                        review_list.clear();
                        user_list.clear();
                    }

                }
                if (error != null) {
                    System.err.println(error);
                }

                for(DocumentChange doc : value.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        String reviewId = doc.getDocument().getId();
                        final Review review = doc.getDocument().toObject(Review.class).withId(reviewId);

                        String reviewUserId = doc.getDocument().getString("user_id");
                        firebaseFirestore.collection("Users").document(reviewUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()) {
                                    Log.e("test","inventory firebasfirestore 동작 good");
                                    User user = task.getResult().toObject(User.class);

                                    if(isFirstPageFirstLoad) {
                                        user_list.add(user);
                                        review_list.add(review);
                                    } else {
                                        user_list.add(0,user);
                                        review_list.add(0,review);
                                    }

                                    Log.e("test","firebase add good");

                                }
                                likeReviewRecyclerAdapter.notifyDataSetChanged();
                            }

                        });


                    }
                }

            }
        });
        return view;
    }
}