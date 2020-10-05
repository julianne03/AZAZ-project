package kr.hs.emirim.seungmin.javaproject_azaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.InventoryFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.RecommendFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.ReviewFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.SettingFragment;
import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.SettingPageFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    Fragment ReviewFragment;
    Fragment InventoryFragment;
    Fragment RecommendFragment;
    Fragment SettingPageFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,ReviewFragment).commit();
                    return true;
                case R.id.action_inventory :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,InventoryFragment).commit();
                    return true;
                case R.id.action_recommend :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,RecommendFragment).commit();
                    return true;
                case R.id.action_settings :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,SettingPageFragment).commit();
                    return true;
            }
            return false;
        }
    };

    private String current_userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        ReviewFragment = new ReviewFragment();
        InventoryFragment = new InventoryFragment();
        RecommendFragment = new RecommendFragment();
        SettingPageFragment = new SettingPageFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,RecommendFragment).commit();

        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        ab.hide();

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        } else {
            current_userId = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        if (!task.getResult().exists()) {
                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);

                        }
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_top_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout :
                logout();
                return true;
            case R.id.action_setup :
                settings();
                return true;
            case R.id.action_inventory :
                Inventory();
            default:
                return false;
        }
    }

    private void Inventory() {
        Intent InventoryIntent = new Intent(MainActivity.this, InventoryActivity.class);
        startActivity(InventoryIntent);
    }

    private void settings() {
        Intent setIntent = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(setIntent);
    }

    private void logout() {

        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}