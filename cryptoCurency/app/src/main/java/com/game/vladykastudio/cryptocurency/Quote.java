package com.game.vladykastudio.cryptocurency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote {

    @SerializedName("USD")
    @Expose
    private USD mUSD;

    public USD getUsd() {
        return mUSD;
    }
    public void setUsd(USD mUSD) {
        this.mUSD = mUSD;
    }
}