package kr.hs.emirim.seungmin.javaproject_azaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class UpdateReviewActivity extends AppCompatActivity {

    private EditText item_name;
    private EditText item_price;
    private EditText item_brand;
    private ImageView item_image1;

    private EditText item_good;
    private EditText item_bad;
    private EditText item_recommend;
    private EditText item_etc;

    private ImageView back_btn;
    private ConstraintLayout update_post_next_btn;

    private Uri item_image1_Uri = null;
    private Spinner category;
    private String category_text;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;
    private String review_id;

    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_review);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        final androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        ab.hide();

        item_name = findViewById(R.id.item_name);
        item_price = findViewById(R.id.item_price);
        item_brand = findViewById(R.id.item_brand);
        item_image1 = findViewById(R.id.item_image1);

        item_good = findViewById(R.id.item_good);
        item_bad = findViewById(R.id.item_bad);
        item_recommend = findViewById(R.id.item_recommend);
        item_etc = findViewById(R.id.item_etc);

        back_btn = findViewById(R.id.back_btn);
        category = findViewById(R.id.spinner);

        review_id = getIntent().getStringExtra("review_id");

        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_dropdown_item);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category_text = parent.getItemAtPosition(position).toString();
                Log.e("test", category_text);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        update_post_next_btn = findViewById(R.id.update_post_next_btn);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseFirestore.collection("Reviews").document(review_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {

                    if(task.getResult().exists()) {

                        String name = task.getResult().getString("item_name");
                        String price = task.getResult().getString("item_price");
                        String brand = task.getResult().getString("item_brand");
                        String image = task.getResult().getString("item_image1");
                        String category = task.getResult().getString("item_category");
                        String good = task.getResult().getString("item_good");
                        String bad = task.getResult().getString("item_bad");
                        String recommend = task.getResult().getString("item_recommend");
                        String etc = task.getResult().getString("item_etc");

                        item_image1_Uri = Uri.parse(image);
                        item_name.setText(name);
                        item_price.setText(price);
                        item_brand.setText(brand);
                        category_text = category;
                        item_good.setText(good);
                        item_bad.setText(bad);
                        item_recommend.setText(recommend);
                        item_etc.setText(etc);


                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_image);

                        Glide.with(getApplicationContext()).setDefaultRequestOptions(placeholderRequest).load(image).into(item_image1);


                    }
                }
            }
        });


        update_post_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateReviewActivity.this);
                builder.setTitle("리뷰 수정").setMessage("정말 리뷰를 수정하시겠습니까?");

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = item_name.getText().toString();
                        final String price = item_price.getText().toString();
                        final String brand = item_brand.getText().toString();

                        final String good = item_good.getText().toString();
                        final String bad = item_bad.getText().toString();
                        final String recommend = item_recommend.getText().toString();
                        final String etc = item_etc.getText().toString();

                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(brand) && category_text != null
                                && !TextUtils.isEmpty(good) && !TextUtils.isEmpty(bad) && !TextUtils.isEmpty(recommend) && !TextUtils.isEmpty(etc)
                        ) {

                            findViewById(R.id.update_review_progress).setVisibility(View.VISIBLE);

                            Map<String, Object> itemMap = new HashMap<>();

                            itemMap.put("item_name", name);
                            itemMap.put("item_price", price);
                            itemMap.put("item_brand", brand);
                            itemMap.put("item_category", category_text);
                            itemMap.put("user_id", current_user_id);
                            itemMap.put("item_image1", item_image1_Uri.toString());
                            itemMap.put("timestamp", FieldValue.serverTimestamp());
                            itemMap.put("item_good", good);
                            itemMap.put("item_bad", bad);
                            itemMap.put("item_recommend", recommend);
                            itemMap.put("item_etc", etc);

                            firebaseFirestore.collection("Reviews")
                                    .document(review_id)
                                    .set(itemMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(UpdateReviewActivity.this, "리뷰가 성공적으로 수정되었습니다!", Toast.LENGTH_LONG).show();
                                                Intent mainIntent = new Intent(UpdateReviewActivity.this, MainActivity.class);
                                                startActivity(mainIntent);


                                            }
                                        }
                                    });

                            firebaseFirestore.collection("Users/" + current_user_id + "/reviews")
                                    .document(review_id)
                                    .set(itemMap)

                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.e("NewPost -> User Post", "User 데베에 추가됨.");
                                            }
                                        }
                                    });


                        }
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

    }

}