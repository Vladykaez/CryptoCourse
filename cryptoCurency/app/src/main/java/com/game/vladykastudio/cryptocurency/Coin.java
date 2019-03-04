package com.game.vladykastudio.cryptocurency;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coin {

    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("symbol")
    @Expose
    private String mSymbol;

    @SerializedName("slug")
    @Expose
    private String mSlug;

    @SerializedName("is_active")
    @Expose
    private int mIsActive;

    @SerializedName("first_historical_data")
    @Expose
    private String mFirstHistoricalData;

    @SerializedName("last_historical_data")
    @Expose
    private String mLastHistoricalData;

    @SerializedName("platform")
    @Expose
    private Object mPlatform;

    @SerializedName("quote")
    @Expose
    private Quote mQuote;

    public Coin() {

    }

    public Coin(int id, String name, String symbol, String slug, int isActive, String firstHistoricalData, String lastHistoricalData, Object platform, Quote quote) {
        mId = id;
        mName = name;
        mSymbol = symbol;
        mSlug = slug;
        mIsActive = isActive;
        mFirstHistoricalData = firstHistoricalData;
        mLastHistoricalData = lastHistoricalData;
        mPlatform = platform;
        mQuote = quote;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }

    public int isActive() {
        return mIsActive;
    }

    public void setActive(int active) {
        mIsActive = active;
    }

    public String getFirstHistoricalData() {
        return mFirstHistoricalData;
    }

    public void setFirstHistoricalData(String firstHistoricalData) {
        mFirstHistoricalData = firstHistoricalData;
    }

    public String getLastHistoricalData() {
        return mLastHistoricalData;
    }

    public void setLastHistoricalData(String lastHistoricalData) {
        mLastHistoricalData = lastHistoricalData;
    }

    public Object getPlatform() {
        return mPlatform;
    }

    public void setPlatform(Object platform) {
        mPlatform = platform;
    }

    public Quote getQuote() {
        return mQuote;
    }
    public void setQuote(Quote quote) {
        mQuote = quote;
    }
}