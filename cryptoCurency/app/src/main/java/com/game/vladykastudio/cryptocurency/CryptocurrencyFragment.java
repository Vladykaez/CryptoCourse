package com.game.vladykastudio.cryptocurency;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CryptocurrencyFragment extends Fragment {

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private CoinAdapter coinAdapter;
    private List<Coin> coinsList = new ArrayList<>();
    private CoinApi coins;

    private boolean isLoading = true;
    private SwipeRefreshLayout swipeRefresh;
    private String apiKey = "6005b29b-783d-45e3-aa48-cabeb0fcee14";
    private Integer start = 1;
    private Integer limit = 30;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_cryptocurrency, container, false);

        swipeRefresh = rootView.findViewById(R.id.swipeRefresh);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pro-api.coinmarketcap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coins = retrofit.create(CoinApi.class);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setCryptocurrency(apiKey, start, limit, false);
                start += limit;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 400);
            }
        });
        initRecyclerView();
        return rootView;
    }
    public void initRecyclerView() {
        coinAdapter = new CoinAdapter(coinsList, getContext());
        recyclerView.setAdapter(coinAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading && ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() == recyclerView.getLayoutManager().getItemCount() - 1) {
                    setCryptocurrency(apiKey, start, limit, true);
                    isLoading = false;
                }
            }
        });
        setCryptocurrency(apiKey, start, limit, true);
    }
    public void setCryptocurrency(final String apiKey, Integer mStart, final Integer mLimit, final boolean flag) {
        if(flag == false) {
            mStart = 1;
            start = 1;
        }
        final Call<CoinResponse> coin = coins.getCoinListResponse(apiKey, mStart, mLimit);
        coin.enqueue(new Callback<CoinResponse>() {
            @Override
            public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
                if (flag == false) {
                    coinsList.clear();
                    coinsList.addAll(response.body().getCoinList());
                    coinAdapter.notifyDataSetChanged();
                }
                if(response.body().getStatus().getErrorCode() == 0 && flag == true) {
                    coinsList.addAll(response.body().getCoinList());
                    coinAdapter.notifyDataSetChanged();
                    start += limit;
                }
                isLoading = true;
            }
            @Override
            public void onFailure(Call<CoinResponse> call, Throwable t) {
                Log.i("Error", "onFailure: " + t);
            }
        });
    }
}
