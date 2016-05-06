package com.test.wllnon.sirenxinyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.utils.AnimationUtils;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.customview.NotifyingScrollView;
import com.test.wllnon.sirenxinyi.listener.ScrollViewOnScrollChangedListener;
import com.test.wllnon.sirenxinyi.utils.FormatUtils;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;
import com.test.wllnon.sirenxinyi.viewdata.AnswerCardData;

public class AnswerDescriptionActivity extends AppCompatActivity
        implements View.OnClickListener {

    private NotifyingScrollView notifyingScrollView;
    private CardView floatCardView;

    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton commentButton;
    private FloatingActionButton collectButton;

    private TextView questionTitle;
    private CircleImageView userAvatar;
    private TextView userName;
    private TextView userSignature;
    private TextView answerContent;
    private TextView answerPraise;

    private Toolbar toolbar;

    private AnswerCardData data;

    // TODO: 2016/4/2 here should store the data for the whole article, not just one field
    private static boolean isCollected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_description);

        findViews();
        setListeners();
        settingViews();
    }

    public void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // set the float header with on-scroll-listener
        notifyingScrollView = (NotifyingScrollView) findViewById(R.id.scrollview_content_answer);
        floatCardView = (CardView) findViewById(R.id.title_content_answer);

        questionTitle = (TextView) findViewById(R.id.title_answer);
        userAvatar = (CircleImageView) findViewById(R.id.avatar_friend_item);
        userName = (TextView) findViewById(R.id.name_friend_item);
        userSignature = (TextView) findViewById(R.id.signature_friend_item);
        answerContent = (TextView) findViewById(R.id.content_answer);
        answerPraise = (TextView) findViewById(R.id.praise_answer);

        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu_question);
        commentButton = (FloatingActionButton) findViewById(R.id.comment_answer);
        collectButton = (FloatingActionButton) findViewById(R.id.collect_answer);
    }

    private void setListeners() {
        notifyingScrollView.setOnScrollChangedListener(new ScrollViewOnScrollChangedListener
                .Builder(ScrollViewOnScrollChangedListener.ViewType.BOTH)
                .header(floatCardView)
                .minHeaderTranslation(-getResources().getDimensionPixelSize(R.dimen.float_header_height)
                        - getResources().getDimensionPixelSize(R.dimen.float_header_marginTop))
                .footer(floatingActionsMenu)
                .minFooterTranslation(getResources().getDimensionPixelSize(R.dimen.fab_size_normal)
                        + getResources().getDimensionPixelSize(R.dimen.fab_margin))
                .build());

        questionTitle.setOnClickListener(this);
        userAvatar.setOnClickListener(this);
        commentButton.setOnClickListener(this);
        collectButton.setOnClickListener(this);
    }

    public void settingViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        data = GsonUtils.newInstance().getGson().fromJson(
                getIntent().getStringExtra("data"), AnswerCardData.class);

        questionTitle.setText(data.getQuestion().getTitle());
        userName.setText(data.getUser().getName());
        userSignature.setText(data.getUser().getSignature());
        answerContent.setText(data.getAnswer().getSimpleContent());
        answerPraise.setText(FormatUtils.getInstance().decimalSimplifyFormatter(data.getAnswer().getPraiseNumber()));

        Glide.with(this)
                .load(data.getUser().getAvatarUrl())
                .error(R.drawable.ic_account_circle_grey_600_48dp)
                .crossFade(1500)
                .into(userAvatar);

        floatCardView.setVisibility(View.VISIBLE);
        notifyingScrollView.setOverScrollEnabled(false);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.collect_answer:
                collectClick();
                break;
            case R.id.comment_answer:
                floatingActionsMenu.collapse();
                startActivity(new Intent(AnswerDescriptionActivity.this, CommentActivity.class));
                break;
            case R.id.title_answer:
                intent = new Intent(this, QuestionDetailActivity.class);
                intent.putExtra("question", GsonUtils.newInstance().getGson().toJson(data.getQuestion()));
                startActivity(intent);
                break;
            case R.id.avatar_friend_item:
                intent = new Intent(this, PersonalInfoActivity.class);
                intent.putExtra("user", GsonUtils.newInstance().getGson().toJson(data.getUser()));
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void collectClick() {
        // TODO: 2016/4/2 do something
        isCollected = !isCollected;

        final int resId = isCollected ? R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp;
        final String title = isCollected ? "collected!" : "Collect it.";

        AnimationUtils.getInstance(this).animateFAB(collectButton, resId, title);
    }
}
