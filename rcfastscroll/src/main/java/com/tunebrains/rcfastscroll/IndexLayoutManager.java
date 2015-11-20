package com.tunebrains.rcfastscroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * Created by edgar on 5/31/15.
 */
public class IndexLayoutManager extends FrameLayout {

    private TextView stickyIndex;
    private RecyclerView indexList;

    public IndexLayoutManager(Context context) {
        super(context);
    }

    public IndexLayoutManager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexLayoutManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IndexLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.internal_index_layout, this, true);
        init();
    }

    public void attach(RecyclerView pRecyclerView) {
        indexList = pRecyclerView;
        pRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                update(recyclerView, dx, dy);
            }
        });
        update(pRecyclerView, 0, 0);
    }

    public void dettach(RecyclerView pRecyclerView) {
        pRecyclerView.setOnScrollListener(null);
    }

    public void refresh() {
        update(indexList, 0, 0);
    }
    private void init() {
        stickyIndex = (TextView) findViewById(R.id.section_title);
    }

    private Boolean isHeader(TextView prev, TextView act) {
        if (isSameChar(prev.getText().charAt(0), act.getText().charAt(0))) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private Boolean isSameChar(char prev, char curr) {
        if (Character.toLowerCase(prev) == Character.toLowerCase(curr)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private void updatePosBasedOnReferenceList(RecyclerView referenceRv) {
        View firstVisibleView = referenceRv.getChildAt(0);
        int actual = referenceRv.getChildPosition(firstVisibleView);
        ((LinearLayoutManager) indexList.getLayoutManager()).scrollToPositionWithOffset(actual, firstVisibleView.getTop() + 0);
    }

    // SUBSCRIBER INTERFACE ________________________________________________________________________

    public void update(RecyclerView referenceList, float dx, float dy) {
        if (indexList != null && indexList.getChildCount() > 2) {
            show();
            updatePosBasedOnReferenceList(referenceList);

            View firstVisibleView = indexList.getChildAt(0);
            View secondVisibleView = indexList.getChildAt(1);


            TextView firstRowIndex = (TextView) firstVisibleView.findViewById(R.id.section_title);
            TextView secondRowIndex = (TextView) secondVisibleView.findViewById(R.id.section_title);

            int visibleRange = indexList.getChildCount();
            int actual = indexList.getChildPosition(firstVisibleView);
            int next = actual + 1;
            int last = actual + visibleRange;

            // RESET STICKY LETTER INDEX
            stickyIndex.setText(String.valueOf(getIndexContext(firstRowIndex)).toUpperCase());
            stickyIndex.setVisibility(TextView.VISIBLE);
            ViewCompat.setAlpha(firstRowIndex, 1);

            if (dy > 0) {
                // USER SCROLLING DOWN THE RecyclerView
                if (next <= last) {
                    if (isHeader(firstRowIndex, secondRowIndex)) {
                        stickyIndex.setVisibility(TextView.INVISIBLE);
                        firstRowIndex.setVisibility(TextView.VISIBLE);
                        ViewCompat.setAlpha(firstRowIndex, (1 - (Math.abs(firstVisibleView.getY()) / firstRowIndex.getHeight())));
                        secondRowIndex.setVisibility(TextView.VISIBLE);
                    } else {
                        firstRowIndex.setVisibility(TextView.INVISIBLE);
                        stickyIndex.setVisibility(TextView.VISIBLE);
                    }
                }
            } else if (dy < 0) {
                // USER IS SCROLLING UP THE RecyclerVIew
                if (next <= last) {
                    // RESET FIRST ROW STATE
                    firstRowIndex.setVisibility(TextView.INVISIBLE);
                    if ((isHeader(firstRowIndex, secondRowIndex) || (getIndexContext(firstRowIndex) != getIndexContext(secondRowIndex))) && isHeader(firstRowIndex, secondRowIndex)) {
                        stickyIndex.setVisibility(TextView.INVISIBLE);
                        firstRowIndex.setVisibility(TextView.VISIBLE);
                        ViewCompat.setAlpha(firstRowIndex, 1 - (Math.abs(firstVisibleView.getY()) / firstRowIndex.getHeight()));
                        secondRowIndex.setVisibility(TextView.VISIBLE);
                    } else {
                        secondRowIndex.setVisibility(TextView.INVISIBLE);
                    }
                }
            }

            if (stickyIndex.getVisibility() == TextView.VISIBLE) {
                firstRowIndex.setVisibility(TextView.INVISIBLE);
            }
        } else {
            hide();
        }
    }

    // GETTERS AND SETTERS _________________________________________________________________________
    public void setIndexList(RecyclerView indexList) {
        this.indexList = indexList;
    }

    /**/
    private char getIndexContext(TextView index) {
        return index.getText().charAt(0);
    }

    public TextView getStickyIndex() {
        return stickyIndex;
    }

    public void show() {
        stickyIndex.setVisibility(View.VISIBLE);
    }

    public void hide() {
        stickyIndex.setVisibility(View.GONE);
    }
}