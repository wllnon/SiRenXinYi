package com.test.wllnon.sirenxinyi.listener;

import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;

import com.test.wllnon.sirenxinyi.customview.NotifyingScrollView;

/**
 * Created by Administrator on 2016/3/25.
 */
public class ScrollViewOnScrollChangedListener implements NotifyingScrollView.OnScrollChangedListener {

    public final static String TAG = "scrollview";
    public enum ViewType {
        HEADER, FOOTER, BOTH
    }

    private final int paddingToParent = 8;

    private final ViewType mViewType;
    private final View mHeader;
    private final int mMinHeaderTranslation;
    private final View mFooter;
    private final int mMinFooterTranslation;

    private int mHeaderDifferTotal = 0;
    private int mFooterDifferTotal = 0;

    private ScrollViewOnScrollChangedListener(Builder builder) {
        mViewType = builder.mViewType;
        mHeader = builder.mHeader;
        mMinHeaderTranslation = builder.mMinHeaderTranslation;
        mFooter = builder.mFooter;
        mMinFooterTranslation = builder.mMinFooterTranslation;

        mHeaderDifferTotal = builder.mHeaderDifferTotal;
        mFooterDifferTotal = builder.mFooterDifferTotal;
        Log.i(TAG, "ScrollViewOnScrollChangedListener: " + mMinHeaderTranslation);
    }

    @Override
    public void onScrollChanged(NestedScrollView scrollView, int l, int t, int oldl, int oldt) {
        int diff = oldt - t;

        switch (mViewType) {
            case HEADER:
                calculateHeaderDifferTotal(diff);
                mHeader.setTranslationY(mHeaderDifferTotal);
                break;
            case FOOTER:
                calculateFooterDifferTotal(diff);
                mFooter.setTranslationY(-mFooterDifferTotal);
                break;
            case BOTH:
                calculateHeaderDifferTotal(diff);
                mHeader.setTranslationY(mHeaderDifferTotal);

                calculateFooterDifferTotal(diff);
                mFooter.setTranslationY(-mFooterDifferTotal);
        }
    }

    private void calculateHeaderDifferTotal(int diff) {
        if (diff <= 0) {
            mHeaderDifferTotal =
                    Math.max(mHeaderDifferTotal + diff,
                            mMinHeaderTranslation);
        } else {
            mHeaderDifferTotal =
                    Math.min(Math.max(mHeaderDifferTotal + diff,
                                    mMinHeaderTranslation),
                            paddingToParent);
        }
    }

    private void calculateFooterDifferTotal(int diff) {
        if (diff <= 0) {
            mFooterDifferTotal =
                    Math.max(mFooterDifferTotal + diff,
                            -mMinFooterTranslation);
        } else {
            mFooterDifferTotal =
                    Math.min(Math.max(mFooterDifferTotal + diff,
                                    -mMinFooterTranslation),
                            paddingToParent);
        }
    }

    public static class Builder {
        private final ViewType mViewType;

        private View mHeader = null;
        private int mMinHeaderTranslation = 0;
        private View mFooter = null;
        private int mMinFooterTranslation = 0;

        private int mHeaderDifferTotal = 0;
        private int mFooterDifferTotal = 0;

        public Builder(ViewType viewType) {
            mViewType = viewType;
        }

        public Builder header(View header) {
            mHeader = header;
            return this;
        }

        public Builder minHeaderTranslation(int minHeaderTranslation) {
            mMinHeaderTranslation = minHeaderTranslation;
            return this;
        }

        public Builder startHeaderTranslation(int startHeaderTranslation) {
            mHeaderDifferTotal = startHeaderTranslation;
            return this;
        }

        public Builder footer(View footer) {
            mFooter = footer;
            return this;
        }

        public Builder minFooterTranslation(int minFooterTranslation) {
            mMinFooterTranslation = minFooterTranslation;
            return this;
        }

        public Builder startFooterTranslation(int startFooterTranslation) {
            mFooterDifferTotal = startFooterTranslation;
            return this;
        }

        public ScrollViewOnScrollChangedListener build() {
            return new ScrollViewOnScrollChangedListener(this);
        }
    }
}
