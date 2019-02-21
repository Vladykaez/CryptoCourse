package com.game.vladykastudio.cryptocurency;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView txt, txt2, txt3;
    boolean flag = false;
    float UAHCourse = getHrn();
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        swipe = (SwipeRefreshLayout)findViewById(R.id.swipe);
        updateRate();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateRate();
                Toast.makeText(MainActivity.this, "This may take some time...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                }, 0);
            }
        });
    }
    public void bitcoin() {
        CryptocurrencyRate getBitcoin = new CryptocurrencyRate("bitcoin");
        getBitcoin.execute();
        txt = (TextView) findViewById(R.id.txt);
        try {
            String string = getBitcoin.get();
            if(flag)
                convertToUAH(string, "Bitcoin", 7, txt);
            else
                txt.setText("Bitcoin - " + string.split(" ")[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void ethereum() {
        CryptocurrencyRate getEthereum = new CryptocurrencyRate("ethereum");
        getEthereum.execute();
        txt2 = (TextView)findViewById(R.id.txt2);
        try {
            String string = getEthereum.get();
            if (flag)
                convertToUAH(string, "Ethereum", 5, txt2);
            else
                txt2.setText("Ethereum - " + string.split(" ")[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void litecoin() {
        CryptocurrencyRate getLitecoin = new CryptocurrencyRate("litecoin");
        getLitecoin.execute();
        txt3 = (TextView) findViewById(R.id.txt3);
        try {
            String string = getLitecoin.get();
            if(flag)
                convertToUAH(string, "Litecoin", 4, txt3);
            else
                txt3.setText("Litecoin - " + string.split(" ")[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void convertToUAH(String string, String cryptoCurrencyName, int subEndIndex, TextView txt) {
        float UAH = Float.parseFloat(string.substring(0, subEndIndex));
        UAH *= UAHCourse;
        String uah = Float.toString(UAH).split("\\.")[0] +"."+ Float.toString(UAH).split("\\.")[0].substring(0,2);
        txt.setText(cryptoCurrencyName + " - " + uah + "₴");
    }
    public void onClickConvert(View v) {
        if(flag) flag = false;
        else flag = true;
        updateRate();
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
    public void updateRate() {
        bitcoin();
        ethereum();
        litecoin();
    }
}