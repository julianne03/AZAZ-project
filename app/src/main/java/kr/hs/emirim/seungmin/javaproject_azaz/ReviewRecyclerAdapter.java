package kr.hs.emirim.seungmin.javaproject_azaz;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {

    public List<Review> review_list;
    public List<User> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public ReviewRecyclerAdapter(List<Review> review_list, List<User> user_list) {
        this.review_list = review_list;
        this.user_list = user_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item,parent,false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String ReviewId = review_list.get(position).ReviewId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String itemName = review_list.get(position).getItem_name();
        String itemPrice = review_list.get(position).getItem_price();
        String itemBrand = review_list.get(position).getItem_brand();
        String itemCategory = review_list.get(position).getItem_category();
        String user_id = review_list.get(position).getUser_id();

        String itemImage1 = review_list.get(position).getItem_image1();
        holder.setItemImage1(itemImage1);

        String itemGood = review_list.get(position).getItem_good();
        String itemBad = review_list.get(position).getItem_bad();
        String itemRecommend = review_list.get(position).getItem_recommend();


        String userName = user_list.get(position).getName();
        String userImage = user_list.get(position).getImage();

        holder.setUserData(userName,userImage);
        holder.setItemData(itemName, itemPrice, itemBrand, itemCategory, itemGood, itemBad, itemRecommend);

        try {
            long milliseconds = review_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("yyyy.MM.dd", new Date(milliseconds)).toString();

            holder.setTime(dateString);
        } catch (Exception e) {
            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        firebaseFirestore.collection("Reviews/" + ReviewId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (!value.isEmpty()) {

                    int count = value.size();

                    holder.updateLikesCount(count);

                } else {
                    holder.updateLikesCount(0);
                }
            }
        });


        firebaseFirestore.collection("Reviews/"+ ReviewId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.like_btn_image_accent));
                } else {
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.like_btn_image));
                }
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore.collection("Reviews/" + ReviewId + "/Likes")
                        .document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {

                            firebaseFirestore.collection("Reviews")
                                    .document(ReviewId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.getResult().exists()) {

                                        Review review = task.getResult().toObject(Review.class);

                                        Map<String, Object> likesMap = new HashMap<>();
                                        likesMap.put("timestamp", FieldValue.serverTimestamp());

                                        Map<String, Object> itemMap = new HashMap<>();
                                        itemMap.put("item_name", review.getItem_name());
                                        Log.e("test", "review item name : " + review.getItem_name());
                                        itemMap.put("item_price", review.getItem_price());
                                        itemMap.put("item_brand", review.getItem_brand());
                                        itemMap.put("item_category", review.getItem_category());
                                        itemMap.put("item_image1", review.getItem_image1());
                                        itemMap.put("user_id", review.getUser_id());
                                        itemMap.put("item_good", review.getItem_good());
                                        itemMap.put("item_bad", review.getItem_bad());
                                        itemMap.put("item_recommend", review.getItem_recommend());
                                        itemMap.put("timestamp", FieldValue.serverTimestamp());

                                        firebaseFirestore.collection("Reviews/" + ReviewId + "/Likes")
                                                .document(currentUserId).set(likesMap);

                                        firebaseFirestore.collection("Users/" + currentUserId + "/Likes")
                                                .document(ReviewId).set(itemMap);
                                    }
                                }
                            });
                        }
                        else {
                            firebaseFirestore.collection("Reviews/" + ReviewId + "/Likes")
                                    .document(currentUserId).delete();
                            firebaseFirestore.collection("Users/"+currentUserId+"/Likes")
                                    .document(ReviewId).delete();
                        }
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return review_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private CircularImageView userImage;
        private TextView userName;

        private ImageView itemImage1;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemDate;
        private TextView itemBrand;

        private TextView itemCategory;

        private TextView itemGood;
        private TextView itemBad;
        private TextView itemRecommend;

        private ImageView likeBtn;
        private TextView likeCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            likeBtn = mView.findViewById(R.id.like_btn);
        }

        public void setItemData(String name, String price, String brand, String category, String good, String bad, String recommend) {

            itemName = mView.findViewById(R.id.review_item_name);
            itemPrice = mView.findViewById(R.id.review_item_price);
            itemBrand = mView.findViewById(R.id.review_item_brand);
            itemCategory = mView.findViewById(R.id.review_category);

            itemGood = mView.findViewById(R.id.review_item_good);
            itemBad = mView.findViewById(R.id.review_item_bad);
            itemRecommend = mView.findViewById(R.id.review_item_recommend);

            itemName.setText(name);
            itemPrice.setText(price);
            itemBrand.setText(brand);
            itemCategory.setText(category);

            itemGood.setText(good);
            itemBad.setText(bad);
            itemRecommend.setText(recommend);

        }

        public void setUserData(String name, String image) {
            userName = mView.findViewById(R.id.review_user_name);
            userImage = mView.findViewById(R.id.review_user_image);

            userName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(userImage);
        }

        public void setItemImage1(String downloadUri) {

            itemImage1 = mView.findViewById(R.id.review_image1);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.add_image);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail().into(itemImage1);
        }

        public void setTime(String date) {

            itemDate = mView.findViewById(R.id.item_date);
            itemDate.setText(date);

        }

        public void updateLikesCount(int count) {
            likeCount = mView.findViewById(R.id.item_like_count);
            likeCount.setText(count + " Likes");

        }
    }
}
