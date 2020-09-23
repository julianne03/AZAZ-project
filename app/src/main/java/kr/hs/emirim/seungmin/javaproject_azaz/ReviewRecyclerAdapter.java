package kr.hs.emirim.seungmin.javaproject_azaz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private CircularImageView userImage;
        private TextView userName;

        private ImageView itemImage1;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemBrand;

        private TextView itemGood;
        private TextView itemBad;
        private TextView itemRecommend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setItemData(String name, String price, String brand, String good, String bad, String recommend) {

            itemName = mView.findViewById(R.id.review_item_name);
            itemPrice = mView.findViewById(R.id.review_item_price);
            itemBrand = mView.findViewById(R.id.review_item_brand);

            itemGood = mView.findViewById(R.id.review_item_good);
            itemBad = mView.findViewById(R.id.review_item_bad);
            itemRecommend = mView.findViewById(R.id.review_item_recommend);

            itemName.setText(name);
            itemPrice.setText(price);
            itemBrand.setText(brand);

            itemGood.setText(good);
            itemBad.setText(bad);
            itemRecommend.setText(recommend);

        }

        public void setUserData(String name, String image) {
            userName = mView.findViewById(R.id.review_user_image);
            userImage = mView.findViewById(R.id.review_user_name);

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
    }
}
