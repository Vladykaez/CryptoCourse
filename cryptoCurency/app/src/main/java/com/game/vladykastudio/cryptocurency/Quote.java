package com.game.vladykastudio.cryptocurency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote {

    @SerializedName("USD")
    @Expose
    private USD mUSD;

    @SerializedName("BTS")
    @Expose
    private UAH mBTS;

    public USD getUsd() {
        return mUSD;
    }
    public void setUsd(USD mUSD) {
        this.mUSD = mUSD;
    }

    public UAH getBts() {
        return mBTS;
    }
    public void setBts(UAH mBTS) {
        this.mBTS = mBTS;
    }
}