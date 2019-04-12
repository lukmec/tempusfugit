package de.lumdev.tempusfugit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//Source: https://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
public class MainPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Context context;

    public MainPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        if (position == 0){ //set icon for first tab (see https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout#add-custom-view-to-tablayout)
//            // Generate title based on item position
//            Drawable image = context.getResources().getDrawable(R.drawable.ic_star_white_24dp);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            // Replace blank spaces with image icon
//            SpannableString sb = new SpannableString("   " + mFragmentTitleList.get(position));
//            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
//            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
//        }else {
            //set title for all other tabs (no icons)
            return mFragmentTitleList.get(position);
//        }
    }

    public View getPageView(int position){
        //according to https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout#add-custom-view-to-tablayout
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.tab_custom, null);
        TextView tv = (TextView) v.findViewById(R.id.custom_tab_text_view);
        tv.setText(mFragmentTitleList.get(position));
        ImageView img = (ImageView) v.findViewById(R.id.custom_tab_image_view);
        if (position == 0) img.setImageResource(R.drawable.ic_star_white_24dp);
        return v;
    }

    public void setNewDoneStatesInOvrvwEventFragment(int positionOfOvrvwEFragm){
        if (mFragmentList.size() >=2) {
//            if (mFragmentList.get(positionOfOvrvwEFragm).getClass() == OverviewEventFragment.class){
                OverviewEventFragment fragm = (OverviewEventFragment) mFragmentList.get(positionOfOvrvwEFragm);
                fragm.saveNewDoneStates();
//            }
        }
    }

    public void setNewDoneStatesInOvrvwToDoEventFragment(int positionOfOvrvwTDEFragm){
        if (mFragmentList.size() >=2) {
//            if (mFragmentList.get(positionOfOvrvwEFragm).getClass() == OverviewEventFragment.class){
            OverviewToDoEventFragment fragm = (OverviewToDoEventFragment) mFragmentList.get(positionOfOvrvwTDEFragm);
            fragm.saveNewDoneStates();
//            }
        }
    }

}
