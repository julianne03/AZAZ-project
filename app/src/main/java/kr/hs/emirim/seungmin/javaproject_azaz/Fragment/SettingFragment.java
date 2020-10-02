package kr.hs.emirim.seungmin.javaproject_azaz.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kr.hs.emirim.seungmin.javaproject_azaz.R;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment {
    private Uri mainImageURI = null;
    private CircularImageView fr_user_image;
    private boolean isChanged = false;

    private Context context;
    private String fr_user_id;
    private EditText fr_user_nickname;
    private EditText fr_user_intro;
    private Button complete_btn;
    private ProgressBar setup_progress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        fr_user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        fr_user_image = v.findViewById(R.id.fr_user_image);
        fr_user_nickname = v.findViewById(R.id.fr_user_nickname);
        fr_user_intro = v.findViewById(R.id.fr_user_intro);
        complete_btn = v.findViewById(R.id.fr_complete_btn);
        setup_progress = v.findViewById(R.id.fr_setup_progress);

        complete_btn.setEnabled(false);

        firebaseFirestore.collection("Users").document(fr_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        String name = task.getResult().getString("name");
                        String intro = task.getResult().getString("intro");
                        String image = task.getResult().getString("image");

                        Log.e("시험",name);
                        Log.e("시험",intro);
                        Log.e("시험",image);

                        mainImageURI = Uri.parse(image);

                        fr_user_nickname.setText("이승민");

                        fr_user_intro.setText(intro);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.profile);

                        Glide.with(getActivity()).setDefaultRequestOptions(placeholderRequest).load(image).into(fr_user_image);
                        Log.e("시험","ㅎㅎㅎㅎㅎㅎㅎㅎ");


                    } else {
                        Toast.makeText(getContext(), "데이터가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), "Firestore Retrieve Error: " + error, Toast.LENGTH_LONG).show();
                }
                setup_progress.setVisibility(View.INVISIBLE);
                complete_btn.setEnabled(true);
            }
        });
        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = fr_user_nickname.getText().toString();
                final String user_introduce = fr_user_intro.getText().toString();


                if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_introduce) && mainImageURI != null) {
                    setup_progress.setVisibility(View.VISIBLE);
                    if (isChanged) {

                        fr_user_id = firebaseAuth.getCurrentUser().getUid();

                        final StorageReference image_path = storageReference.child("profile_images").child(fr_user_id + ".jpg");
                        UploadTask uploadTask = image_path.putFile(mainImageURI);
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return image_path.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                if (task.isSuccessful()) {
                                    storeFireStore(task, user_name, user_introduce);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), "Image Error: " + error, Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    } else {
                        storeFireStore(null, user_name, user_introduce);
                    }
                }
            }
        });
        fr_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//                    } else {
//                        BringImagePicker();
//                    }
//                } else {
                    BringImagePicker();
//                }
            }
        });
        return inflater.inflate(R.layout.fragment_setting, container, false);

    }

    private void storeFireStore(Task<Uri> task, String user_name, String user_introduce) {
        Uri download_uri;

        if (task != null) {
            download_uri = task.getResult();
        } else {
            download_uri = mainImageURI;
        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("intro", user_introduce);
        userMap.put("image", download_uri.toString());

        firebaseFirestore.collection("Users").document(fr_user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "변경 내용이 성공적으로 업데이트 되었습니다.", Toast.LENGTH_LONG).show();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), "Firestore Error: " + error, Toast.LENGTH_LONG).show();
                }
                setup_progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getActivity());
    }


}