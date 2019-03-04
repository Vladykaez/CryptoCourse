package com.game.vladykastudio.cryptocurency;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.EventLogTags;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private TextView txt, txt2, txt3, txtName1, txtName2, txtName3, txtError;
    private Button btn;
    private ImageView img;
    private boolean flag = false;
    private SwipeRefreshLayout swipe;
    private float hryvnaCourse;
    private Retrofit retrofit;
    CoinApi coins;
    String apiKey = "6005b29b-783d-45e3-aa48-cabeb0fcee14";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new HryvnaCourse().execute();
        txt = findViewById(R.id.txt);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txtError = findViewById(R.id.txtError);

        txtName1 = findViewById(R.id.txtName1);
        txtName2 = findViewById(R.id.txtName2);
        txtName3 = findViewById(R.id.txtName3);

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
                Toast.makeText(MainActivity.this, "Update", Toast.LENGTH_SHORT).show();
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
        final Call<CoinResponse> coin = coins.getCoinListResponse(apiKey, 1, 5);
        coin.enqueue(new Callback<CoinResponse>() {
            @Override
            public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
                String bitcoin = Double.toString(response.body().getCoinList().get(0).getQuote().getUsd().getPrice());
                String ethereum = Double.toString(response.body().getCoinList().get(1).getQuote().getUsd().getPrice());
                String litecoin = Double.toString(response.body().getCoinList().get(4).getQuote().getUsd().getPrice());

                txt.setText(bitcoin.split("\\.")[0] + "." + bitcoin.split("\\.")[1].substring(0, 2) + "$");
                txt2.setText(ethereum.split("\\.")[0] + "." + ethereum.split("\\.")[1].substring(0, 2) + "$");
                txt3.setText(litecoin.split("\\.")[0] + "." + litecoin.split("\\.")[1].substring(0, 2) + "$");

                txtName1.setText(response.body().getCoinList().get(0).getName());
                txtName2.setText(response.body().getCoinList().get(1).getName());
                txtName3.setText(response.body().getCoinList().get(4).getName());
            }
            @Override
            public void onFailure(Call<CoinResponse> call, Throwable t) {
                txtError.setText(t.toString());
            }
        });
    }
    public void onClickConvert(View v) {
        final Intent intent = new Intent(this, Main2Activity.class);
        if(isOnline()) {
            flag = !flag;
            setCryptocurrency();
        } else {
            Toast.makeText(this, "No internet connection...", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
    class CryptocurrencyRate extends AsyncTask<Void, Void, String> {
        Elements elem;
        String cryptocurrencyName;
        TextView txt;
        int subIndex;

        public CryptocurrencyRate(String cryptocurrencyName, int subIndex, TextView txt) {
            this.cryptocurrencyName = cryptocurrencyName;
            this.subIndex = subIndex;
            this.txt = txt;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://myfin.by/crypto-rates/" + cryptocurrencyName).get();
                elem = doc.select(".birzha_info_head_rates");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = elem.text();
            return str;
        }
        @Override
        protected void onPostExecute(String str) {
            if(flag) {
                String string = str.split(" ")[0];
                float uah = Float.parseFloat(string.substring(0, string.length()-1));
                String hrn = String.valueOf(uah * hryvnaCourse);
                if(hrn.split("\\.")[1].length() > 2 && subIndex < 2)
                    subIndex++;
                txt.setText(hrn.substring(0, hrn.length()-subIndex) + "₴");
            } else {
                txt.setText(str.split(" ")[0]);
            }
        }
    }
    class HryvnaCourse extends AsyncTask<Void, Void, String> {
        Elements elem;
        @Override
        protected String doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://kurs.com.ua/valyuta/usd").get();
                elem = doc.select(".ipsKurs_rate");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = elem.text().split(" ")[0];
            return str;
        }
        @Override
        protected void onPostExecute(String str) {
            hryvnaCourse = Float.parseFloat(str);
        }
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }
}