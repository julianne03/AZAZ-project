package kr.hs.emirim.seungmin.javaproject_azaz.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.Reccomend1Fragment;
import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.Reccomend2Fragment;
import kr.hs.emirim.seungmin.javaproject_azaz.Fragment.Reccomend3Fragment;

public class PagerAdapter extends FragmentStateAdapter {

    public int mCount;

    public PagerAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new Reccomend1Fragment();
        else if(index==1) return new Reccomend2Fragment();
        else return new Reccomend3Fragment();
    }

    private int getRealPosition(int position) {
        return position % mCount;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
}
