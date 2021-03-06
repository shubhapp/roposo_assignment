package com.roposo.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.roposo.assignment.data.Story;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncTaskFragment.TaskResultListener {

    private static final String TAG_FRAGMENT = "asyncFragment";

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private StoryAdapter storyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpaceItemDecoration(25, true, true));
    }

    @Override
    protected void onStart() {
        super.onStart();
        installAsyncFragment();
    }

    private void installAsyncFragment() {
        AsyncTaskFragment asyncFragment = (AsyncTaskFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        if (asyncFragment == null) {
            asyncFragment = new AsyncTaskFragment();
            installFragment(asyncFragment);
            asyncFragment.startTask();
        } else {
            if (!asyncFragment.isRunning()) {
                asyncFragment.startTask();
            }
        }
    }

    private void installFragment(AsyncTaskFragment asyncFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.async_task_fragment, asyncFragment, TAG_FRAGMENT)
                .commit();
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onTaskFinished(List<Story> result) {
        storyAdapter = new StoryAdapter(this,result);
        recyclerView.setAdapter(storyAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            int position = data.getIntExtra(Constants.EXTRA_POSITION, -1);
            if (position >= 0)
                storyAdapter.notifyItemChanged(position);
        }
    }
}
