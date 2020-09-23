package kr.hs.emirim.seungmin.javaproject_azaz;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class ReviewId {

    @Exclude
    public String ReviewId;

    public <T extends Review> T withId(@NonNull final String id) {
        this.ReviewId = id;
        return (T) this;
    }
}
