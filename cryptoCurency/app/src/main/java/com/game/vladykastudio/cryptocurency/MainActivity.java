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

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView txt, txt2, txt3;
    Button btn;
    ImageView img;
    boolean flag = false;
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        txt = (TextView)findViewById(R.id.txt);
        txt2 = (TextView)findViewById(R.id.txt2);
        txt3 = (TextView)findViewById(R.id.txt3);
        txt.setText("Bitcoin - ???");
        txt2.setText("Ethereum - ???");
        txt3.setText("Litecoin - ???");
        final Intent intent = new Intent(this, Main2Activity.class);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
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
        new CryptocurrencyRate("Bitcoin", txt, 0).execute();
        new CryptocurrencyRate("Ethereum", txt2, 2).execute();
        new CryptocurrencyRate("Litecoin", txt3, 2).execute();
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

        public CryptocurrencyRate(String cryptocurrencyName, TextView txt, int subIndex) {
            this.cryptocurrencyName = cryptocurrencyName;
            this.txt = txt;
            this.subIndex = subIndex;
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
                String hrn = String.valueOf(uah * getHrn());
                txt.setText(cryptocurrencyName + " - " + hrn.substring(0, hrn.length()-subIndex) + "₴");
            } else
                txt.setText(cryptocurrencyName + " - " + str.split(" ")[0]);
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
    }
    public float getHrn() {
        HryvnaCourse getHrn = new HryvnaCourse();
        getHrn.execute();
        float hryvnaRate = 0;
        try {
            hryvnaRate = Float.parseFloat(getHrn.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return hryvnaRate;
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