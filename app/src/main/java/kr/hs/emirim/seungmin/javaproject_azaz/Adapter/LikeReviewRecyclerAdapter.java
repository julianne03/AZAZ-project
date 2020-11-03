package kr.hs.emirim.seungmin.javaproject_azaz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.hs.emirim.seungmin.javaproject_azaz.CommentsActivity;
import kr.hs.emirim.seungmin.javaproject_azaz.R;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.Review;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.User;

public class LikeReviewRecyclerAdapter extends RecyclerView.Adapter<LikeReviewRecyclerAdapter.ViewHolder>{
    public List<Review> review_list;
    public List<User> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public LikeReviewRecyclerAdapter(List<Review> review_list, List<User> user_list) {
        this.review_list = review_list;
        this.user_list = user_list;
    }

    @NonNull
    @Override
    public LikeReviewRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item_like,parent,false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new LikeReviewRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LikeReviewRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String ReviewId = review_list.get(position).ReviewId;

        String itemImage1 = review_list.get(position).getItem_image1();
        holder.setItemImage(itemImage1);

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comments = new Intent(context, CommentsActivity.class);
                comments.putExtra("review_id",ReviewId);
                context.startActivity(comments);
            }
        });
    }

    @Override
    public int getItemCount() {
        return review_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private ImageView itemImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setItemImage(String itemImage1) {
            itemImage = mView.findViewById(R.id.item_image_like);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.default_image);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(itemImage1).thumbnail().into(itemImage);
        }
    }
}
