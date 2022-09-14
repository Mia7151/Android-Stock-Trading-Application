package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<String> source;
    List<String> time;
    List<String> title;
    List<String> img;
    List<String> timed;
    List<String> summary;
    List<String> url;
    Context context;
    public MyAdapter(Context ct, List<String> s1, List<String> s2, List<String> s3, List<String> s4,List<String> s5,List<String> s6,List<String> s7){
        context = ct;
        source = s1;
        time = s2;
        title = s3;
        img = s4;
        timed = s5;
        summary = s6;
        url = s7;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Integer position1 = position + 1;
        holder.source11.setText(source.get(position1));
        holder.time11.setText(time.get(position1));
        holder.title11.setText(title.get(position1));

        holder.img11.setClipToOutline(true);
        //Picasso.get().load(img.get(position1)).fit().centerCrop().into(holder.img11);
        Glide.with(context).load(img.get(position1)).fitCenter().centerCrop().into(holder.img11);
        holder.img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.news_dialog);
                TextView source_dialog = (TextView) dialog.findViewById(R.id.source_dialog);
                source_dialog.setText(source.get(position1));
                TextView news = (TextView) dialog.findViewById(R.id.time_news);
                news.setText(timed.get(position1));
                TextView titled = (TextView) dialog.findViewById(R.id.title_dialog);
                titled.setText(title.get(position1));
                TextView summaryd = (TextView) dialog.findViewById(R.id.summary_dialog);
                summaryd.setText(summary.get(position1));

                String url1 = url.get(position1);
                String title1 = title.get(position1);
                ImageButton chrome=(ImageButton) dialog.findViewById(R.id.chrome);
                chrome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url=url1;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                });

                ImageButton twitter=(ImageButton) dialog.findViewById(R.id.twitter);
                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://twitter.com/intent/tweet?text="+title1+"&url="+url1;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                });

                ImageButton facebook=(ImageButton) dialog.findViewById(R.id.facebook);
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url="https://www.facebook.com/sharer/sharer.php?u="+url1+"&amp;src=sdkpreparse";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                });



                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return source.size() - 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView source11, time11,title11;
        ImageView img11;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            source11 = itemView.findViewById(R.id.source);
            time11 = itemView.findViewById(R.id.new_time);
            title11 = itemView.findViewById(R.id.title);
            img11 = itemView.findViewById(R.id.img_news);
        }
    }
}
