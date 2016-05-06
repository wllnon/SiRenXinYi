package com.test.wllnon.sirenxinyi.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.test.wllnon.sirenxinyi.R;

/**
 * Created by Administrator on 2016/3/26.
 */
public class PersonalInfoIntroFragment extends BaseTabFragment {

    private Button followButton;
    private Button dragButton;

    public PersonalInfoIntroFragment() {
        title = "Introduce";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_info_intro, container, false);

        updateFromNetwork();

        followButton = (Button) rootView.findViewById(R.id.follow_personal_info);
        dragButton = (Button) rootView.findViewById(R.id.drag_personal_info);

        return rootView;
    }

    public void updateFromNetwork() {
        // TODO: 2016/3/26 some network
    }

}
