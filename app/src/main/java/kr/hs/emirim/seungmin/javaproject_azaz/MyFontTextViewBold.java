package kr.hs.emirim.seungmin.javaproject_azaz;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class MyFontTextViewBold extends androidx.appcompat.widget.AppCompatTextView {


    public MyFontTextViewBold(Context context) {
        super(context);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/TmoneyRoundWindExtraBold.ttf");
        setTypeface(typeface);
    }

    public MyFontTextViewBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/TmoneyRoundWindExtraBold.ttf");
        setTypeface(typeface);
    }
}
