package com.game.vladykastudio.cryptocurency;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bitcoin();
        ethereum();
        litecoin();
    }
    public void bitcoin() {
        BitCoinCourse getbitcoin = new BitCoinCourse();
        getbitcoin.execute();
        txt = (TextView) findViewById(R.id.txt);
        try {
            String string = getbitcoin.get();
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
        EthereumCourse getEthereum = new EthereumCourse();
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
        LiteCoinCourse getLitecoin = new LiteCoinCourse();
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
    public void onClickGetRate(View v) {
        bitcoin();
        ethereum();
        litecoin();
        Toast.makeText(this, "This may take some time...", Toast.LENGTH_SHORT).show();
    }
    public void onClickConvert(View v) {
        if(flag) flag = false;
        else flag = true;
        bitcoin();
        ethereum();
        litecoin();
    }
    class BitCoinCourse extends AsyncTask<Void, Void, String> {
        Elements elem;
        @Override
        protected String doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://myfin.by/crypto-rates/bitcoin").get();
                elem = doc.select(".birzha_info_head_rates");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = elem.text();
            return str;
        }
    }
    class EthereumCourse extends AsyncTask<Void, Void, String> {
        Elements elem;
        @Override
        protected String doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://myfin.by/crypto-rates/ethereum").get();
                elem = doc.select(".birzha_info_head_rates");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = elem.text();
            return str;
        }
    }
    class LiteCoinCourse extends AsyncTask<Void, Void, String> {
        Elements elem;
        @Override
        protected String doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://myfin.by/crypto-rates/litecoin").get();
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
}