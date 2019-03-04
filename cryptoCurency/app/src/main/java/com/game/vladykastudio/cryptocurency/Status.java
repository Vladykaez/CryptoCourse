package com.game.vladykastudio.cryptocurency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("timestamp")
    @Expose
    private String mTimeStamp;

    @SerializedName("error_code")
    @Expose
    private int mErrorCode;

    @SerializedName("error_message")
    @Expose
    private String mErrorMessage;

    @SerializedName("elapsed")
    @Expose
    private int mElapsed;

    @SerializedName("credit_count")
    @Expose
    private int mCreditCount;

    public Status() {

    }

    public Status(String timeStamp, int errorCode, String errorMessage, int elapsed, int creditCount) {
        mTimeStamp = timeStamp;
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
        mElapsed = elapsed;
        mCreditCount = creditCount;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public int getElapsed() {
        return mElapsed;
    }

    public void setElapsed(int elapsed) {
        mElapsed = elapsed;
    }

    public int getCreditCount() {
        return mCreditCount;
    }

    public void setCreditCount(int creditCount) {
        mCreditCount = creditCount;
    }
}