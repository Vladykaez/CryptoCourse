package com.game.vladykastudio.cryptocurency;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {

    private List<Coin> coinsList = new ArrayList<>();

    public CoinAdapter(List<Coin> coins) {
        coinsList = coins;
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
        TextView name, rate;
        ImageView logo;

        public CoinViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rate = itemView.findViewById(R.id.rate);
            logo = itemView.findViewById(R.id.coin_logo);
        }

        void bind(Coin coin) {
            String logoUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/" + coin.getId() + ".png";

            Picasso.get().load(logoUrl).into(logo);

            name.setText(coin.getName());
            rate.setText(Double.toString(coin.getQuote().getUsd().getPrice()).split("\\.")[0] + "."
                    +  Double.toString(coin.getQuote().getUsd().getPrice()).split("\\.")[1].substring(0, 2) + "$");
        }
    }

}
