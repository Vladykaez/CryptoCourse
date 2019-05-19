package com.game.vladykastudio.cryptocurency;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class Main2Activity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setColorDoneText(Color.BLACK);
        setIndicatorColor(Color.BLACK, Color.BLACK);

        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Rates");
        sliderPage.setDescColor(Color.BLACK);
        sliderPage.setTitleColor(Color.BLACK);
        sliderPage.setDescription("Get the latest rates of the most popular cryptocurrencies");
        sliderPage.setImageDrawable(R.drawable.ic_bit);
        sliderPage.setBgColor(Color.WHITE);
        addSlide(AppIntroFragment.newInstance(sliderPage));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Information");
        sliderPage2.setDescColor(Color.BLACK);
        sliderPage2.setTitleColor(Color.BLACK);
        sliderPage2.setDescription("Get all the information about changing the cryptocurrency rate");
        sliderPage2.setImageDrawable(R.drawable.ic_ethe);
        sliderPage2.setBgColor(Color.WHITE);
        addSlide(AppIntroFragment.newInstance(sliderPage2));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
