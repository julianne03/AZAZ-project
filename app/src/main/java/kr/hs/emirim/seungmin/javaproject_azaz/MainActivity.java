package kr.hs.emirim.seungmin.javaproject_azaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    MyPageAdapter viewPagerAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String current_userId;

    private int[] icon_list = {
            R.drawable.action_home,
            R.drawable.action_recommend,
            R.drawable.action_favorite,
            R.drawable.action_settings_black
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab();

        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        ab.show();
        ab.setTitle("아장아장");

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



    }
    private void tab() {
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new MyPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(new HomeFragment(),"HOME");
        viewPagerAdapter.AddFragment(new RecommendFragment(), "RECOMMEND");
        viewPagerAdapter.AddFragment(new InventoryFragment(), "FAVORITE");
        viewPagerAdapter.AddFragment(new SettingFragment(), "SETTINGS");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setupTabIcons() {
        for(int i=0; i<tabLayout.getTabCount(); i++) {

            tabLayout.getTabAt(i).setIcon(icon_list[i]);
            tabLayout.getTabAt(i).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
        }
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
            default:
                return false;
        }
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