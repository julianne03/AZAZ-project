package kr.hs.emirim.seungmin.javaproject_azaz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText item_name;
    private EditText item_price;
    private EditText item_brand;
    private ImageView item_image1;

    private EditText item_good;
    private EditText item_bad;
    private EditText item_recommend;
    private EditText item_etc;

    private ImageView back_btn;
    private ConstraintLayout new_post_next_btn;

    private Uri item_image1_Uri = null;
    private ArrayList imageList = new ArrayList();
    private int upload_count = 0;
    private ArrayList urlStrings;
    private Spinner category;
    private String category_text;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Bitmap compressedImageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

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
        //item_image2 = findViewById(R.id.item_image2);
        item_good = findViewById(R.id.item_good);
        item_bad = findViewById(R.id.item_bad);
        item_recommend = findViewById(R.id.item_recommend);

        back_btn = findViewById(R.id.back_btn);
        category = findViewById(R.id.spinner);

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


        new_post_next_btn = findViewById(R.id.new_post_next_btn);

        final String randomName = UUID.randomUUID().toString();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        item_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);

            }
        });

//        item_image2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setMinCropResultSize(500, 500)
//                        .setAspectRatio(1, 1)
//                        .start(NewPostActivity.this);
//            }
//        });

        new_post_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = item_name.getText().toString();
                final String price = item_price.getText().toString();
                final String brand = item_brand.getText().toString();

                final String good = item_good.getText().toString();
                final String bad = item_bad.getText().toString();
                final String recommend = item_recommend.getText().toString();
                final String etc = item_etc.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(brand) && category_text != null
                        && !TextUtils.isEmpty(good) && !TextUtils.isEmpty(bad) && !TextUtils.isEmpty(recommend) && !TextUtils.isEmpty(etc)
                        && item_image1_Uri != null
                ) {

                    findViewById(R.id.new_review_progress).setVisibility(View.VISIBLE);
                    //get images url
                    urlStrings = new ArrayList();
                    final StorageReference imageFolder = storageReference.child("item_images");

                    for (upload_count = 0; upload_count < imageList.size(); upload_count++) {
                        Uri IndividualImage = (Uri) imageList.get(upload_count);
                        final StorageReference imageName = imageFolder.child("Images" + IndividualImage.getLastPathSegment());

                        imageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageName.getDownloadUrl().addOnSuccessListener(
                                        new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                urlStrings.add(String.valueOf(uri));

                                                if (urlStrings.size() == imageList.size()) {
                                                    storeLink(urlStrings, name,price,brand,good,bad,recommend,etc);
                                                }
                                            }
                                        });
                            }
                        });

                    }

                }
            }
        });
    }


    private void storeLink(ArrayList<String> urlStrings,String name,String price,String brand,String good,String bad,String recommend,String etc) {


        Map<String, Object> itemMap = new HashMap<>();

        for (int i = 0; i < urlStrings.size(); i++) {
            itemMap.put("item_image" + i, urlStrings.get(i));
        }
        itemMap.put("item_name", name);
        itemMap.put("item_price", price);
        itemMap.put("item_brand", brand);
        itemMap.put("item_category", category_text);
        itemMap.put("user_id", current_user_id);
        itemMap.put("timestamp", FieldValue.serverTimestamp());
        itemMap.put("item_good", good);
        itemMap.put("item_bad", bad);
        itemMap.put("item_recommend", recommend);
        itemMap.put("item_etc", etc);

        firebaseFirestore.collection("Reviews").

                add(itemMap).

                addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(NewPostActivity.this, "리뷰가 추가되었습니다!", Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                            startActivity(mainIntent);


                        }
                    }
                });

        firebaseFirestore.collection("Users/" + current_user_id + "/reviews").

                add(itemMap).

                addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.e("NewPost -> User Post", "User 데베에 추가됨.");
                        }
                    }
                });

        imageList.clear();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {


                if (data.getClipData() != null) {

                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSlect = 0;

                    while (currentImageSlect < countClipData) {

                        item_image1_Uri = data.getClipData().getItemAt(currentImageSlect).getUri();
                        imageList.add(item_image1_Uri);
                        item_image1.setImageURI(item_image1_Uri);
                        currentImageSlect = currentImageSlect + 1;
                    }

                    Log.d("image url", String.valueOf(imageList));

                } else {
                    Toast.makeText(this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}