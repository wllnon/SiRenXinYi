package com.test.wllnon.sirenxinyi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.test.wllnon.sirenxinyi.R;

/**
 * Created by Administrator on 2016/3/27.
 */
public class PersonalInfoDescFragment extends BaseTabFragment {

    private TagView tagView;

    public PersonalInfoDescFragment() {
        title = "Description";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_info_desc, container, false);

        updateFromNetwork();

        tagView = (TagView) rootView.findViewById(R.id.tagview_personal_info);

        Tag tag = new Tag("gynecologist");
        tag.tagTextColor = getResources().getColor(R.color.colorPrimary);
        tag.background = getResources().getDrawable(R.color.colorLightPrimary);
        tag.radius = 10;
        tag.isDeletable = false;
        tagView.addTag(tag);

        tag = new Tag("psychiatrist");
        tag.tagTextColor = getResources().getColor(R.color.colorPrimary);
        tag.background = getResources().getDrawable(R.color.colorLightPrimary);
        tag.radius = 10;
        tag.isDeletable = false;
        tagView.addTag(tag);

        tag = new Tag("nurse");
        tag.tagTextColor = getResources().getColor(R.color.colorPrimary);
        tag.background = getResources().getDrawable(R.color.colorLightPrimary);
        tag.radius = 10;
        tag.isDeletable = false;
        tagView.addTag(tag);

        return rootView;
    }

    public void updateFromNetwork() {
        // TODO: 2016/3/26 some network
    }

}
