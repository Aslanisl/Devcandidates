package ru.mail.aslanisl.devcandidates;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mList;
    private Call<ArrayList<String>> mCall;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null ) {
            getSupportActionBar().hide();
        }

        mList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), tabletSize ? 3 : 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new Adapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mCall = App.getApi().getList();

        if (savedInstanceState != null){
            mList = savedInstanceState.getStringArrayList("list");
            mAdapter.addAll(mList);
        } else mCall.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()){
                    mList = response.body();
                    mAdapter.addAll(mList);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("list", mList);
        super.onSaveInstanceState(outState);
    }
}
