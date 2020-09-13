package kr.hs.emirim.seungmin.javaproject_azaz;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyFontTextViewRegular extends androidx.appcompat.widget.AppCompatTextView {


    public MyFontTextViewRegular(@NonNull Context context) {
        super(context);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/TmoneyRoundWindRegular.ttf");
        setTypeface(typeface);
    }

    public MyFontTextViewRegular(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/TmoneyRoundWindRegular.ttf");
        setTypeface(typeface);
    }
}
