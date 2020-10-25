package kr.hs.emirim.seungmin.javaproject_azaz.category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import kr.hs.emirim.seungmin.javaproject_azaz.Adapter.ReviewRecyclerAdapter;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.Review;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.User;
import kr.hs.emirim.seungmin.javaproject_azaz.NewPostActivity;
import kr.hs.emirim.seungmin.javaproject_azaz.R;

public class FocusFragment extends Fragment {

    private RecyclerView review_list_exercise;
    private List<Review> review_list;
    private List<User> user_list;

    private FirebaseFirestore firebaseFirestore;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private FirebaseAuth firebaseAuth;

    private FloatingActionButton add_review;

    private Boolean isFirstPageFirstLoad = true;


    public FocusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_focus, container, false);

        review_list = new ArrayList<>();
        user_list = new ArrayList<>();
        review_list_exercise = view.findViewById(R.id.review_list_exercise);

        add_review = view.findViewById(R.id.add_review_exercise);

        firebaseAuth = FirebaseAuth.getInstance();

        reviewRecyclerAdapter = new ReviewRecyclerAdapter(review_list, user_list);
        review_list_exercise.setLayoutManager(new LinearLayoutManager(getActivity()));
        review_list_exercise.setAdapter(reviewRecyclerAdapter);

        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_review_intent = new Intent(getContext(), NewPostActivity.class);
                startActivity(add_review_intent);
            }
        });


        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            review_list_exercise.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if(reachedBottom) {
                        //loadMoreReview();
                    }


                }
            });

            Query firstQuery = firebaseFirestore.collection("Reviews").whereEqualTo("item_category","운동기관");
//                    .orderBy("timestamp",Query.Direction.DESCENDING);

            firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(firebaseAuth.getCurrentUser() != null) {
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
                                            Log.e("test","firebasfirestore 동작 good");
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
                                        reviewRecyclerAdapter.notifyDataSetChanged();
                                    }

                                });
                            }
                        }

                    }
                }

            });
        }

        return view;
    }



    private void loadMoreReview() {

        if(firebaseAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection("Reviews")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(!value.isEmpty()) {

                        if (error != null) {
                            System.err.println(error);
                        }

                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String reviewId = doc.getDocument().getId();
                                final Review review = doc.getDocument().toObject(Review.class).withId(reviewId);

                                String reviewUserId = doc.getDocument().getString("user_id");
                                firebaseFirestore.collection("Users").document(reviewUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()) {
                                            Log.e("test", "firebasfirestore 동작 good2");
                                            User user = task.getResult().toObject(User.class);

                                            user_list.add(user);
                                            review_list.add(review);

                                        }
                                        reviewRecyclerAdapter.notifyDataSetChanged();
                                    }

                                });
                            }
                        }
                    }
                }
            });
        }


    }
}