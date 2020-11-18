package kr.hs.emirim.seungmin.javaproject_azaz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import kr.hs.emirim.seungmin.javaproject_azaz.DetailPageActivity;
import kr.hs.emirim.seungmin.javaproject_azaz.R;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.Review;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.User;

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

        final String itemName = review_list.get(position).getItem_name();
        final String itemPrice = review_list.get(position).getItem_price();
        final String itemBrand = review_list.get(position).getItem_brand();
        final String itemCategory = review_list.get(position).getItem_category();
        final String user_id = review_list.get(position).getUser_id();

        final String itemImage1 = review_list.get(position).getItem_image1();
        holder.setItemImage1(itemImage1);

        final String itemGood = review_list.get(position).getItem_good();
        final String itemBad = review_list.get(position).getItem_bad();
        final String itemRecommend = review_list.get(position).getItem_recommend();
        final String itemEtc = review_list.get(position).getItem_etc();

        final String userName = user_list.get(position).getName();
        final String userImage = user_list.get(position).getImage();

        holder.setUserData(userName,userImage);
        holder.setItemData(itemName, itemPrice, itemBrand, itemCategory);


        holder.findId();

        //update like count
        if(currentUserId != null) {
            firebaseFirestore.collection("Reviews/" + ReviewId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error==null) {
                        if (!value.isEmpty()) {

                            int count = value.size();

                            holder.updateLikesCount(count);

                        } else {
                            holder.updateLikesCount(0);
                        }
                    }

                }
            });

        }


        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(context, DetailPageActivity.class);
                detail.putExtra("review_id",ReviewId);
                detail.putExtra("user_image",userImage);
                detail.putExtra("user_name",userName);
                detail.putExtra("item_name",itemName);
                detail.putExtra("item_price",itemPrice);
                detail.putExtra("item_brand",itemBrand);
                detail.putExtra("item_category",itemCategory);
                detail.putExtra("item_image",itemImage1);
                detail.putExtra("item_user_id",user_id);
                detail.putExtra("item_good",itemGood);
                detail.putExtra("item_bad",itemBad);
                detail.putExtra("item_recommend",itemRecommend);
                detail.putExtra("item_etc",itemEtc);
                context.startActivity(detail);
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

        private ConstraintLayout item_view;

        private ImageView itemImage1;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemBrand;

        private ImageView likeBtn;
        private TextView likeCount;

        private TextView itemCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setItemData(String name, String price, String brand, String category) {

            itemName = mView.findViewById(R.id.review_item_name);
            itemPrice = mView.findViewById(R.id.review_item_price);
            itemBrand = mView.findViewById(R.id.review_item_brand);
            itemCategory = mView.findViewById(R.id.review_category);

            itemName.setText(name);
            itemPrice.setText(price+"원");
            itemBrand.setText(brand);
            itemCategory.setText(category);

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
            requestOptions.placeholder(R.drawable.default_image);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail().into(itemImage1);
        }


        public void findId() {
            item_view = mView.findViewById(R.id.item_view);
        }

        public void updateLikesCount(int count) {
            likeCount = mView.findViewById(R.id.item_like_count);
            likeCount.setText(""+count);
        }
    }
}
