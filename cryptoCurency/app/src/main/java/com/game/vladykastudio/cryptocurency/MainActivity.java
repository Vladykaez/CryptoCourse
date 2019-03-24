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

    TextView name, rate, rate24, rate1, rate7;
    private Button btn;
    private ImageView img;
    private boolean flag = false;
    private SwipeRefreshLayout swipe;
    private float hryvnaCourse;
    private Retrofit retrofit;
    private CoinApi coins;
    private String apiKey = "6005b29b-783d-45e3-aa48-cabeb0fcee14";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        name = findViewById(R.id.name);
        rate = findViewById(R.id.rate);
        rate24 = findViewById(R.id.rate24);
        rate1 = findViewById(R.id.rate1);
        rate7 = findViewById(R.id.rate7);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pro-api.coinmarketcap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        coins = retrofit.create(CoinApi.class);

        final Intent intent = new Intent(this, Main2Activity.class);
        swipe = findViewById(R.id.swipe);
        if(isOnline()) {
            setCryptocurrency();
        } else {
            startActivity(intent);
        }
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isOnline())
                    setCryptocurrency();
                else
                    startActivity(intent);
                Toast.makeText(MainActivity.this, "Update...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                }, 0);
            }
        });
    }
    public void setCryptocurrency() {
        final Call<CoinResponse> coin = coins.getCoinListResponse(apiKey, 1, 1);
        coin.enqueue(new Callback<CoinResponse>() {
            @Override
            public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
                Double rateUsd = response.body().getCoinList().get(0).getQuote().getUsd().getPrice();
                name.setText(response.body().getCoinList().get(0).getSymbol() + " | " +  response.body().getCoinList().get(0).getName());
                rate.setText(Double.toString(rateUsd).split("\\.")[0] + "." + Double.toString(rateUsd).split("\\.")[1].substring(0, 2) + "$");
                rate24.setText("24h: " + Double.toString(response.body().getCoinList().get(0).getQuote().getUsd().getPercentC24h()).split("\\.")[0] +
                        "." + Double.toString(response.body().getCoinList().get(0).getQuote().getUsd().getPercentC24h()).split("\\.")[1].substring(0, 2) + "%");
                rate1.setText("1h: " + Double.toString(response.body().getCoinList().get(0).getQuote().getUsd().getPercentC1h()).split("\\.")[0] +
                        "." + Double.toString(response.body().getCoinList().get(0).getQuote().getUsd().getPercentC1h()).split("\\.")[1].substring(0, 2) + "%");
                rate7.setText("7d: " + Double.toString(response.body().getCoinList().get(0).getQuote().getUsd().getPercentC7d()).split("\\.")[0] +
                        "." + Double.toString(response.body().getCoinList().get(0).getQuote().getUsd().getPercentC7d()).split("\\.")[1].substring(0, 2) + "%");

                if (response.body().getCoinList().get(0).getQuote().getUsd().getPercentC24h() < 0)
                    rate24.setTextColor(Color.RED);
                else
                    rate24.setTextColor(Color.GREEN);

                if (response.body().getCoinList().get(0).getQuote().getUsd().getPercentC1h() < 0)
                    rate1.setTextColor(Color.RED);
                else
                    rate1.setTextColor(Color.GREEN);

                if (response.body().getCoinList().get(0).getQuote().getUsd().getPercentC7d() < 0)
                    rate7.setTextColor(Color.RED);
                else
                    rate7.setTextColor(Color.GREEN);

                if (response.body().getCoinList().get(0).getQuote().getUsd().getPercentC24h() == 0)
                    rate24.setTextColor(Color.GRAY);
                if (response.body().getCoinList().get(0).getQuote().getUsd().getPercentC1h() == 0)
                    rate1.setTextColor(Color.GRAY);
                if (response.body().getCoinList().get(0).getQuote().getUsd().getPercentC7d() == 0)
                    rate7.setTextColor(Color.GRAY);
            }
            @Override
            public void onFailure(Call<CoinResponse> call, Throwable t) {
                Log.d("Error", "onFailure: " + t);
            }
        });
    }
    boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }
}