package com.game.vladykastudio.cryptocurency;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        try {
            txt = (TextView)findViewById(R.id.txt);
            String string = getbitcoin.get();
            txt.setText("BitCoin - " + string.split(" ")[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void Ethereum() {
        getEthereumCourse getEthereum = new getEthereumCourse();
        getEthereum.execute();
        try {
            txt2 = (TextView)findViewById(R.id.txt2);
            String string = getEthereum.get();
            string = string.split(" ")[0];
            txt2.setText("Ethereum - " + string);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        Bitcoin();
        Ethereum();
        Toast.makeText(this, "This may take some time...", Toast.LENGTH_SHORT).show();
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
}