package kr.hs.emirim.seungmin.javaproject_azaz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentReference;
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

public class NewPostActivity extends AppCompatActivity {

    private EditText item_name;
    private EditText item_price;
    private EditText item_brand;
    private ImageView item_image1;
    private ImageView item_image2;

    private EditText item_good;
    private EditText item_bad;
    private EditText item_recommend;

    private FloatingActionButton back_btn;
    private FloatingActionButton new_post_next_btn;

    private Uri item_image1_Uri = null;
    private Uri item_image2_Uri = null;

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
        item_image2 = findViewById(R.id.item_image2);
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
                Log.e("test",category_text);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(700, 512)
                        .setAspectRatio(1, 1)
                        .start(NewPostActivity.this);


            }
        });

        item_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(500, 500)
                        .setAspectRatio(1, 1)
                        .start(NewPostActivity.this);
            }
        });

        new_post_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = item_name.getText().toString();
                final String price = item_price.getText().toString();
                final String brand = item_brand.getText().toString();

                final String good = item_good.getText().toString();
                final String bad = item_bad.getText().toString();
                final String recommend = item_recommend.getText().toString();



                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(brand) && category != null
                        && !TextUtils.isEmpty(good) && !TextUtils.isEmpty(bad) && !TextUtils.isEmpty(recommend)
//                        && item_image1_Uri!=null && item_image2_Uri!=null
                        ) {

                    final String randomName = UUID.randomUUID().toString();

                    final StorageReference filepath = storageReference.child("item_images").child(randomName + ".jpg");
                    final UploadTask uploadTask1 = filepath.putFile(item_image1_Uri);
                    final UploadTask uploadTask2 = filepath.putFile(item_image2_Uri);

                    Task<Uri> urlTask = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull final Task<Uri> task) {

                            final String downloadUri1 = task.getResult().toString();

                            if (task.isSuccessful()) {

                                File newImageFile = new File(item_image1_Uri.getPath());

                                try {
                                    compressedImageFile = new Compressor(NewPostActivity.this)
                                            .setMaxHeight(30)
                                            .setMaxWidth(30)
                                            .setQuality(2)
                                            .compressToBitmap(newImageFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                uploadTask1.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                        Map<String, Object> itemMap = new HashMap<>();
                                        itemMap.put("item_name", name);
                                        itemMap.put("item_price",price);
                                        itemMap.put("item_brand",brand);
                                        itemMap.put("item_category",category_text);
                                        itemMap.put("item_image1",downloadUri1);
                                        itemMap.put("user_id", current_user_id);
                                        itemMap.put("timestamp", FieldValue.serverTimestamp());
                                        itemMap.put("item_good", good);
                                        itemMap.put("item_bad", bad);
                                        itemMap.put("item_recommend", recommend);

                                        firebaseFirestore.collection("Reviews").add(itemMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if(task.isSuccessful()) {
                                                    Toast.makeText(NewPostActivity.this,"리뷰가 추가되었습니다!",Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                }
                                            }
                                        });


                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(NewPostActivity.this,"빈 칸을 채워주세요!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                item_image1_Uri = result.getUri();
                item_image2_Uri = result.getUri();

                item_image1.setImageURI(item_image1_Uri);
                item_image2.setImageURI(item_image2_Uri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}