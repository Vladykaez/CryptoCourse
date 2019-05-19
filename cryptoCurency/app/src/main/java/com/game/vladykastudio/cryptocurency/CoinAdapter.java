package com.game.vladykastudio.cryptocurency;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {

    private List<Coin> coinsList = new ArrayList<>();
    Context mContext;
    Dialog myDialog;

    public CoinAdapter(List<Coin> coins, Context cont) {
        coinsList = coins;
        mContext = cont;
    }

    public void setCoins(List<Coin> coins) {
        this.coinsList = coins;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_item, parent, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CoinViewHolder holder, int i) {
        holder.bind(coinsList.get(i));
    }

    @Override
    public int getItemCount() {
        return coinsList.size();
    }

    class CoinViewHolder extends RecyclerView.ViewHolder {
        TextView name, rate, dialog_name, dialog_price, dialog_last24h, dialog_last1h;
        ImageView logo, dialog_logo;
        boolean flag = false;
        LinearLayout item_coin;
        Coin coins;
        String logoUrl;

        public CoinViewHolder(final View itemView) {
            super(itemView);

            item_coin = itemView.findViewById(R.id.coin_item_id);
            name = itemView.findViewById(R.id.name);
            rate = itemView.findViewById(R.id.rate);
            logo = itemView.findViewById(R.id.coin_logo);

            myDialog = new Dialog(mContext);
            myDialog.setContentView(R.layout.dialog_coin);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            item_coin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_name = myDialog.findViewById(R.id.dialogName);
                    dialog_price = myDialog.findViewById(R.id.dialogPrice);
                    dialog_logo = myDialog.findViewById(R.id.dialogLogo);
                    dialog_last1h = myDialog.findViewById(R.id.dialog1h);
                    dialog_last24h = myDialog.findViewById(R.id.dialog24h);

                    dialog_name.setText(coins.getName());
                    dialog_price.setText(Double.toString(coins.getQuote().getUsd().getPrice()).split("\\.")[0] + "."
                            +  Double.toString(coins.getQuote().getUsd().getPrice()).split("\\.")[1].substring(0, 2) + "$");
                    dialog_last1h.setText("1h: " +Double.toString(coins.getQuote().getUsd().getPercentC1h()).split("\\.")[0] + "." +
                            Double.toString(coins.getQuote().getUsd().getPercentC1h()).split("\\.")[1].substring(0, 2) + "%");
                    dialog_last24h.setText("24h: "+ Double.toString(coins.getQuote().getUsd().getPercentC24h()).split("\\.")[0] + "." +
                            Double.toString(coins.getQuote().getUsd().getPercentC24h()).split("\\.")[1].substring(0, 2) + "%");

                    if(coins.getQuote().getUsd().getPercentC1h() < 0)
                        dialog_last1h.setTextColor(Color.RED);
                    else
                        dialog_last1h.setTextColor(Color.GREEN);

                    if(coins.getQuote().getUsd().getPercentC24h() < 0)
                        dialog_last24h.setTextColor(Color.RED);
                    else
                        dialog_last24h.setTextColor(Color.GREEN);

                    Picasso.get().load(logoUrl).into(dialog_logo);
                    myDialog.show();
                }
            });
        }

        public void bind(Coin coin) {
            coins = coin;

            logoUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/" + coin.getId() + ".png";

            Picasso.get().load(logoUrl).into(logo);

            name.setText(coin.getName());
            rate.setText(Double.toString(coin.getQuote().getUsd().getPrice()).split("\\.")[0] + "."
                    +  Double.toString(coin.getQuote().getUsd().getPrice()).split("\\.")[1].substring(0, 2) + "$");
        }
    }

}
