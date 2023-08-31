package com.heetVadecha.vadecha_news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.google.android.gms.ads.AdRequest;
import com.heetVadecha.vadecha_news.Model.Articles;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    List<Articles> articles;
    Activity mActivity;
    AdRequest adRequest;

    public Adapter(Context context, List<Articles> articles,Activity activity) {
        this.context = context;
        this.articles = articles;
        this.mActivity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final Articles a = articles.get(position);

        String imageUrl = a.getUrlToImage();
        String url = a.getUrl();

        Picasso.get().load(imageUrl).into(holder.imageView);
        holder.tvTitle.setText(a.getTitle());
        holder.tvSource.setText(a.getSource().getName());
        if (a.getPublishedAt() != null && !Objects.equals(a.getPublishedAt(), "")){
            holder.tvDate.setText( dateTime(a.getPublishedAt()));
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,Detailed.class);
                intent.putExtra("title",a.getTitle());
                intent.putExtra("source",a.getSource().getName());
                if (a.getPublishedAt() != null && !Objects.equals(a.getPublishedAt(), "")){
                    intent.putExtra("time",dateTime(a.getPublishedAt()));
                }
                intent.putExtra("desc",a.getDescription());
                intent.putExtra("imageUrl",a.getUrlToImage());
                intent.putExtra("url",a.getUrl());
                context.startActivity(intent);
                loadInterstitialAd();
            }
        });

    }
    public void loadInterstitialAd() {
        adRequest = new AdRequest.Builder()
                .setRequestAgent(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        InterstitialAd.load(context.getApplicationContext(), "ca-app-pub-6788818386275854/6176674732", adRequest, new InterstitialAdLoadCallback() {

            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // this method is called when ad is loaded in that case we are displaying our ad.
                interstitialAd.show(mActivity);
            }


            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // this method is called when we get any error
                Toast.makeText(mActivity, "Fail to load ad..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvSource,tvDate;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }


    public String dateTime(String t){
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:",Locale.getDefault());
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }


}
