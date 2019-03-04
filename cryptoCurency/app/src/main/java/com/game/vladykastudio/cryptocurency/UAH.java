package com.game.vladykastudio.cryptocurency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UAH {

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("volume_24h")
    @Expose
    private double mVolume24;

    @SerializedName("percent_change_1h")
    @Expose
    private double mPercentC1h;

    @SerializedName("percent_change_24h")
    @Expose
    private double mPercentC24h;

    @SerializedName("percent_change_7d")
    @Expose
    private double mPercentC7d;

    @SerializedName("market_cap")
    @Expose
    private double mMarketCap;

    @SerializedName("last_updated")
    @Expose
    private String mlastUpdate;


    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public double getVolume24() {
        return mVolume24;
    }
    public void setVolume24(double volume24) {
        this.mVolume24 = volume24;
    }

    public double getPercentC1h() {
        return mVolume24;
    }
    public void setPercentC1h(double percentC1h) {
        this.mPercentC1h = percentC1h;
    }

    public double getPercentC24h() {
        return mPercentC24h;
    }
    public void setPercentC24h(double percentC24h) {
        this.mPercentC24h = percentC24h;
    }

    public double getPercentC7d() {
        return mPercentC7d;
    }
    public void setPercentC7d(double percentC7d) {
        this.mPercentC7d = percentC7d;
    }

    public double getMarketCap() {
        return mMarketCap;
    }
    public void setMarketCap(double marketCap) {
        this.mMarketCap = marketCap;
    }

    public String getLastUpdate() {
        return mlastUpdate;
    }
    public void setLastUpdate(String lastUpdate) {
        this.mlastUpdate = lastUpdate;
    }
}