package com.test.wllnon.sirenxinyi.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.adapter.TabFragmentPagerAdapter;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.fragment.BaseTabFragment;
import com.test.wllnon.sirenxinyi.fragment.PersonalInfoDescFragment;
import com.test.wllnon.sirenxinyi.fragment.PersonalInfoIntroFragment;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonalInfoActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private CircleImageView avatar;

    private ViewPager viewPager;
    private TabFragmentPagerAdapter viewPagerAdapter;

    private List<BaseTabFragment> tabFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String json = getIntent().getStringExtra("user");
        User user = GsonUtils.newInstance().getGson().fromJson(json, User.class);

        avatar = (CircleImageView) findViewById(R.id.avatar_personal_info);
        assert avatar != null;
        Glide.with(this)
                .load(user.getAvatarUrl())
                .error(R.drawable.ic_account_circle_grey_600_48dp)
                .crossFade(1500)
                .into(avatar);

        tabFragmentList = new ArrayList<>();
        tabFragmentList.add(new PersonalInfoIntroFragment());
        tabFragmentList.add(new PersonalInfoDescFragment());

        viewPager = (ViewPager) findViewById(R.id.viewpager_personal_info);
        viewPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), tabFragmentList);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout= (TabLayout) findViewById(R.id.tablayout_personal_info);
        assert tabLayout != null;
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_personal_info);
        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setTitle(user.getName());
    }
}
