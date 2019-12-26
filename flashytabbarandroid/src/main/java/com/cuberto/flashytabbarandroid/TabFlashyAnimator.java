package com.cuberto.flashytabbarandroid;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabFlashyAnimator extends ViewPager.SimpleOnPageChangeListener {
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final List<Integer> mFragmentIconList = new ArrayList<>();
    private final List<Integer> mFragmentColorList = new ArrayList<>();
    private final List<Float> mFragmentSizeList = new ArrayList<>();
    private TabLayout tabLayout;
    private int previousPosition = -1;

    public TabFlashyAnimator(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public void addTabItem(String title, int tabIcon) {
        addTabItem(title, tabIcon, null, null);
    }

    public void addTabItem(String title, int tabIcon, int color) {
        addTabItem(title, tabIcon, color, null);
    }

    public void addTabItem(String title, int tabIcon, float size) {
        addTabItem(title, tabIcon, null, size);
    }

    public void addTabItem(String title, Integer tabIcon, Integer color, Float size) {
        mFragmentTitleList.add(title);
        mFragmentIconList.add(tabIcon);
        mFragmentColorList.add(color);
        mFragmentSizeList.add(size);
    }

    private void getTabView(int position, TabLayout.Tab tab, boolean isSelected) {
        View view = tab.getCustomView() == null ? LayoutInflater.from(tabLayout.getContext()).inflate(R.layout.custom_tab, null) : tab.getCustomView();
        if (tab.getCustomView() == null) {
            tab.setCustomView(view);
        }
        ImageView tabImageView = view.findViewById(R.id.tab_image);
        tabImageView.setImageResource(mFragmentIconList.get(position));
        ConstraintLayout layout = view.findViewById(R.id.root);
        ConstraintSet set = new ConstraintSet();
        ImageView foreground = view.findViewById(R.id.image_foreground);
        ImageView textForeground = view.findViewById(R.id.text_foreground);
        ImageView dot = view.findViewById(R.id.dot);
        TextView title = view.findViewById(R.id.tab_title);
        title.setText(mFragmentTitleList.get(position));
        title.setTextColor(mFragmentColorList.get(position) == null ? Color.argb(255, 40, 45, 130) : getColor(mFragmentColorList.get(position)));
        if(mFragmentSizeList.get(position) != null) {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mFragmentSizeList.get(position));
        }
        set.clone(layout);
        set.connect(textForeground.getId(), ConstraintSet.TOP, isSelected ? title.getId() : tabImageView.getId(), ConstraintSet.BOTTOM);
        if(isSelected) {
            set.clear(tabImageView.getId(), ConstraintSet.BOTTOM);
            set.connect(title.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            set.connect(title.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            dot.startAnimation(AnimationUtils.loadAnimation(tabLayout.getContext(), R.anim.show));
        } else {
            set.connect(tabImageView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            set.clear(title.getId(), ConstraintSet.BOTTOM);
            set.connect(title.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            if(position == previousPosition || previousPosition == -1) {
                dot.startAnimation(AnimationUtils.loadAnimation(tabLayout.getContext(), R.anim.hide));
            }
        }
        set.clear(foreground.getId(), isSelected ? ConstraintSet.TOP : ConstraintSet.BOTTOM);
        set.connect(foreground.getId(), isSelected ? ConstraintSet.BOTTOM : ConstraintSet.TOP, tabImageView.getId(), ConstraintSet.BOTTOM);
        set.applyTo(layout);
        tabImageView.setColorFilter(Color.argb(255, 40, 45, 130));
    }

    public void highLightTab(int position) {
        if (tabLayout != null) {
            TransitionManager.beginDelayedTransition(tabLayout, getTransitionSet());
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                assert tab != null;
                getTabView(i, tab, i == position);
                LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i));
                layout.setBackground(null);
                layout.setPaddingRelative(0, 0, 0, 0);
            }
            previousPosition = position;
        }
    }

    private TransitionSet getTransitionSet() {
        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeBounds().setDuration(250));
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        return set;
    }

    public void onStart(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public void onStop() {
        this.tabLayout = null;
    }

    @Override
    public void onPageSelected(int position) {
        highLightTab(position);
    }

    public int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(tabLayout.getContext(), colorRes);
    }

    public void setBadge(int count, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        TextView badge = tab.getCustomView().findViewById(R.id.badge);
        badge.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        badge.setText(String.valueOf(count));
    }
}
