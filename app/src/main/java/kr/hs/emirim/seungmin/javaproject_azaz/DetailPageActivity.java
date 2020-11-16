package kr.hs.emirim.seungmin.javaproject_azaz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.hs.emirim.seungmin.javaproject_azaz.Adapter.CommentsRecyclerAdapter;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.Comments;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.User;

public class DetailPageActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView comment_list_view;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Comments> commentsList;
    private List<User> user_list;

    private ImageView detail_back_btn;
    private CircularImageView detail_user_image;
    private TextView detail_user_name;

    private ImageView like_btn;
    private TextView like_count;

    private TextView detail_item_name;
    private TextView detail_item_price;
    private TextView detail_item_brand;
    private TextView detail_item_category;

    private TextView detail_item_good;
    private TextView detail_item_bad;
    private TextView detail_item_recommend;

    private EditText comment_field;
    private ImageButton comment_btn;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String review_id;
    private String user_image;
    private String user_name;
    private String item_name;
    private String item_price;
    private String item_brand;
    private String item_category;
    private String item_good;
    private String item_bad;
    private String item_recommend;
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        ab.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        //전의 인텐트로부터 값 받아오기
        review_id = getIntent().getStringExtra("review_id");
        user_image = getIntent().getStringExtra("user_image");
        user_name = getIntent().getStringExtra("user_name");
        item_name = getIntent().getStringExtra("item_name");
        item_price = getIntent().getStringExtra("item_price");
        item_brand = getIntent().getStringExtra("item_brand");
        item_category = getIntent().getStringExtra("item_category");
        item_good = getIntent().getStringExtra("item_good");
        item_bad = getIntent().getStringExtra("item_bad");
        item_recommend = getIntent().getStringExtra("item_recommend");

        findid();

        detail_back_btn.setOnClickListener(this);
        comment_btn.setOnClickListener(this);

        //댓글 리스트
        commentsList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        comment_list_view.setHasFixedSize(true);
        comment_list_view.setLayoutManager(new LinearLayoutManager(this));
        comment_list_view.setAdapter(commentsRecyclerAdapter);

        //user 정보 가져오기
        detail_user_name.setText(user_name);
        RequestOptions placeholderOption = new RequestOptions();
        placeholderOption.placeholder(R.drawable.profile);
        Glide.with(getApplicationContext()).applyDefaultRequestOptions(placeholderOption).load(user_image).into(detail_user_image);

        //item 정보 가져오기
        detail_item_name.setText(item_name);
        detail_item_price.setText(item_price+"원");
        detail_item_brand.setText(item_brand);
        detail_item_category.setText(item_category);
        detail_item_good.setText(item_good);
        detail_item_bad.setText(item_bad);
        detail_item_recommend.setText(item_recommend);

        //댓글 불러오기
        firebaseFirestore.collection("Reviews/"+review_id+"/Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error == null) {
                            if (!value.isEmpty()) {

                                if (error != null) {
                                    System.err.println(error);
                                }

                                for (DocumentChange doc : value.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {

                                        String commentId = doc.getDocument().getId();
                                        Comments comments = doc.getDocument().toObject(Comments.class);
                                        commentsList.add(comments);
                                        commentsRecyclerAdapter.notifyDataSetChanged();

                                    }
                                }
                            }
                        }

                    }
                });

    }

    private void findid() {

        detail_back_btn = findViewById(R.id.detail_back_btn);
        detail_user_image = findViewById(R.id.detail_user_image);
        detail_user_name = findViewById(R.id.detail_user_name);

        like_btn = findViewById(R.id.detail_like_btn);
        like_count = findViewById(R.id.detail_like_count);

        detail_item_name = findViewById(R.id.detail_item_name);
        detail_item_price = findViewById(R.id.detail_item_price);
        detail_item_brand = findViewById(R.id.detail_item_brand);
        detail_item_category = findViewById(R.id.detail_item_category);

        detail_item_good = findViewById(R.id.detail_item_good);
        detail_item_bad = findViewById(R.id.detail_item_bad);
        detail_item_recommend = findViewById(R.id.detail_item_recommend);

        comment_field = findViewById(R.id.comment_field);
        comment_btn = findViewById(R.id.comment_btn);
        comment_list_view = findViewById(R.id.comment_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_back_btn :
                finish();
                break;
            case R.id.comment_btn :
                send_comment();
                break;
        }
    }

    private void send_comment() {
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_message = comment_field.getText().toString();

                if(!TextUtils.isEmpty(comment_message)) {
                    Map<String,Object> commentsMap = new HashMap<>();
                    commentsMap.put("message",comment_message);
                    commentsMap.put("user_id",current_user_id);
                    commentsMap.put("timestamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Reviews/" + review_id + "/Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(DetailPageActivity.this, "에러 발생: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            } else {
                                comment_field.setText("");
                            }
                        }
                    });
                } else {
                    Toast.makeText(DetailPageActivity.this, "댓글을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}