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

    TextView txt;
    TextView txt2;
    boolean flag = false;
    float hrnrate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bitcoin();
        Ethereum();
    }
    public void Bitcoin() {
        getBitCoinCourse getbitcoin = new getBitCoinCourse();
        getbitcoin.execute();
        txt = (TextView) findViewById(R.id.txt);
        try {
            String string = getbitcoin.get();
            if(flag) {
                float hryvna = Float.parseFloat(string.substring(0,7));
                if (hrnrate == 0)
                    hrnrate = getHrn();
                hryvna *= hrnrate;
                String hrn = Float.toString(hryvna).split("\\.")[0] +"."+ Float.toString(hryvna).split("\\.")[0].substring(0,2);
                txt.setText("Bitcoin - " + hrn + "₴");
            } else {
                txt.setText("Bitcoin - " + string.split(" ")[0]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void Ethereum() {
        getEthereumCourse getEthereum = new getEthereumCourse();
        getEthereum.execute();
        txt2 = (TextView)findViewById(R.id.txt2);
        try {
            String string = getEthereum.get();
            if (flag) {
                float hrn = Float.parseFloat(string.substring(0,5));
                if (hrnrate == 0)
                    hrnrate = getHrn();
                hrn *= hrnrate;
                String hryvna = Float.toString(hrn).split("\\.")[0] + "." + Float.toString(hrn).split("\\.")[1].substring(0, 2);
                txt2.setText("Ethereum - " + hryvna + "₴");
            } else {
                txt2.setText("Ethereum - " + string.split(" ")[0]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void onClickGetRate(View v) {
        Bitcoin();
        Ethereum();
        Toast.makeText(this, "This may take some time...", Toast.LENGTH_SHORT).show();
    }
    public void onClickConvert(View v) {
        if(flag)
            flag = false;
        else
            flag = true;
        Bitcoin();
        Ethereum();
    }
    class getBitCoinCourse extends AsyncTask<Void, Void, String> {
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
    class getEthereumCourse extends AsyncTask<Void, Void, String> {
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
    class getHryvnaCourse extends AsyncTask<Void, Void, String> {
        Elements elem;
        @Override
        protected String doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://kurs.com.ua/valyuta/usd").get();
                elem = doc.select(".ipsKurs_rate");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = elem.text();
            str = str.split(" ")[0];
            return str;
        }
    }
    public float getHrn() {
        getHryvnaCourse getHrn = new getHryvnaCourse();
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