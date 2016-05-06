package com.test.wllnon.sirenxinyi.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.pojo.Question;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;
import com.test.wllnon.sirenxinyi.utils.network.UrlUtils;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import me.gujun.android.taggroup.TagGroup;

public class QuestionEditActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextInputEditText titleEdit;
    private EditText contentEdit;
    private TagGroup tagGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_edit);

        findViews();
        configViews();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        titleEdit = (TextInputEditText) findViewById(R.id.title_question_edit);
        contentEdit = (EditText) findViewById(R.id.content_question_edit);
        tagGroup = (TagGroup) findViewById(R.id.tag_group_question_edit);
    }

    private void configViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            Question question = new Question(0,
                    tagGroup.getTags(),
                    titleEdit.getEditableText().toString(),
                    contentEdit.getEditableText().toString(),
                    0, 0,
                    new Time(System.currentTimeMillis()));

            Map<String, String> params = new HashMap<>();
            params.put("userId", "" + Application.getInstance().getUser().getId());
            params.put("question", GsonUtils.newInstance().getGson().toJson(question));

            Application.getInstance().getNetworkUtils().gsonRequestWithMethod(this,
                    Request.Method.GET,
                    params,
                    UrlUtils.QUESTION_SUBMIT_URL,
                    Integer.class,
                    new Response.Listener<Integer>() {
                        @Override
                        public void onResponse(Integer response) {
                            if (response == 1) {
                                Toast.makeText(QuestionEditActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(QuestionEditActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(QuestionEditActivity.this, "网络问题！", Toast.LENGTH_SHORT).show();
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
