package com.game.vladykastudio.cryptocurency;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    TextView txt, txt2, txt3, txt4;
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
        txt4 = (TextView)findViewById(R.id.textView2);
        btn = (Button)findViewById(R.id.button2);
        img = (ImageView)findViewById(R.id.imageView);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        if(isOnline()) {
            updateRate();
            setVis2();
        }
        else
            setVis();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isOnline()) {
                    setVis2();
                    updateRate();
                } else
                    setVis();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                }, 0);
            }
        });
    }
    public String setCryptocurrency(String cryptocurrencyName, int subIndex) {
        CryptocurrencyRate getCryptocurrency = new CryptocurrencyRate(cryptocurrencyName);
        getCryptocurrency.execute();
        try {
            String string = getCryptocurrency.get().split(" ")[0];
            float uah = Float.parseFloat(string.substring(0, string.length()-1));
            String hrn = String.valueOf(uah * getHrn());
            if (flag)
                return cryptocurrencyName + " - " + hrn.substring(0, hrn.length()-subIndex) + "₴";
            else
                return cryptocurrencyName + " - " + string;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void onClickConvert(View v) {
        if(isOnline()) {
            flag = !flag;
            updateRate();
        } else {
            Toast.makeText(this, "No internet connection...", Toast.LENGTH_SHORT).show();
            setVis();
        }
    }
    class CryptocurrencyRate extends AsyncTask<Void, Void, String> {
        Elements elem;
        String cryptocurrencyName;

        public CryptocurrencyRate(String cryptocurrencyName) {
            this.cryptocurrencyName = cryptocurrencyName;
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
    public void setVis() {
        txt.setVisibility(View.INVISIBLE);
        txt2.setVisibility(View.INVISIBLE);
        txt3.setVisibility(View.INVISIBLE);
        txt4.setVisibility(View.VISIBLE);
        btn.setVisibility(View.INVISIBLE);
        img.setVisibility(View.VISIBLE);
    }
    public void setVis2() {
        txt.setVisibility(View.VISIBLE);
        txt2.setVisibility(View.VISIBLE);
        txt3.setVisibility(View.VISIBLE);
        txt4.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.VISIBLE);
        img.setVisibility(View.INVISIBLE);
    }
    public void updateRate() {
        txt.setText(setCryptocurrency("Bitcoin", 0));
        txt2.setText(setCryptocurrency("Ethereum", 2));
        txt3.setText(setCryptocurrency("Litecoin", 2));
    }
}