package com.game.vladykastudio.cryptocurency;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLogTags;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe;
    private Retrofit retrofit;
    private CoinApi coins;
    private String apiKey = "6005b29b-783d-45e3-aa48-cabeb0fcee14";
    private Integer start = 1;
    private Integer limit = 50;

    private RecyclerView recyclerView;
    private CoinAdapter coinAdapter;
    private List<Coin> coinsList = new ArrayList<>();
    private boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pro-api.coinmarketcap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coins = retrofit.create(CoinApi.class);

        initRecyclerView();
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));

        coinAdapter = new CoinAdapter(coinsList);

        recyclerView.setAdapter(coinAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading && ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() == recyclerView.getLayoutManager().getItemCount() - 1) {
                    setCryptocurrency(apiKey, start, limit);
                    isLoading = false;
                }
            }
        });

        setCryptocurrency(apiKey, start, limit);
    }
    public void setCryptocurrency(String apiKey, Integer mStart, Integer mLimit) {
        final Call<CoinResponse> coin = coins.getCoinListResponse(apiKey, mStart, mLimit);
        coin.enqueue(new Callback<CoinResponse>() {
            @Override
            public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
                if (response.body().getStatus().getErrorCode() == 0) {
                    coinsList.addAll(response.body().getCoinList());
                    coinAdapter.notifyDataSetChanged();
                    start += limit;
                }
                isLoading = true;
            }
            @Override
            public void onFailure(Call<CoinResponse> call, Throwable t) {
                Log.i("Error", "onFailure: " + t);
            }
        });
    }
}