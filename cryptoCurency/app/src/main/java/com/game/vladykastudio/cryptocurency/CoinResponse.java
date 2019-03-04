package com.game.vladykastudio.cryptocurency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinResponse {

    @SerializedName("data")
    @Expose
    private List<Coin> mCoinList;

    @SerializedName("status")
    @Expose
    private Status mStatus;

    public CoinResponse() {

    }

    public CoinResponse(List<Coin> coinList, Status status) {
        mCoinList = coinList;
        mStatus = status;
    }

    public List<Coin> getCoinList() {
        return mCoinList;
    }

    public void setCoinList(List<Coin> coinList) {
        mCoinList = coinList;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }
}
