package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.MyViewHolder> {
    List<String> stock;
    List<Integer> shares;
    List<Double> market_v;
    List<Double> price_change;
    List<Double> price_change_p;
    Context context;
    public MyAdapter3(Context ct,List<String> s1, List<Integer> s2, List<Double> s3, List<Double> s4,List<Double> s5){
        context = ct;
        stock = s1;
        shares = s2;
        market_v = s3;
        price_change = s4;
        price_change_p = s5;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.por_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter3.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("price222","start222");
        DecimalFormat f = new DecimalFormat("##0.00");
        holder.stock1.setText(stock.get(position));
        holder.share1.setText(Integer.toString(shares.get(position))+" shares");
        holder.market_value1.setText("$"+f.format(market_v.get(position)));

        if (price_change.get(position) > 0) {
            holder.price_port1.setTextColor(Color.rgb(0, 200, 0));
            holder.arrow.setImageResource(R.drawable.trendingup1);
            holder.arrow.setColorFilter(context.getResources().getColor(R.color.green));
        } else if (price_change.get(position) < 0) {
            holder.price_port1.setTextColor(Color.rgb(200, 0, 0));
            holder.arrow.setImageResource(R.drawable.trendingdown);
            holder.arrow.setColorFilter(context.getResources().getColor(R.color.red));
        }

        String price_port = "$"+ f.format(price_change.get(position))+ " (" +f.format(price_change_p.get(position)) +"% )";
        holder.price_port1.setText(price_port);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(context,searchRes.class);
                intent2.putExtra("stock",stock.get(position));
                context.startActivity(intent2);


            }
        });


    }

    @Override
    public int getItemCount() {
        return stock.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView stock1,share1,market_value1,price_port1;
        ImageView arrow,button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stock1 = itemView.findViewById(R.id.stock_p);
            share1 = itemView.findViewById(R.id.shares);
            market_value1 = itemView.findViewById(R.id.market_value);
            price_port1 = itemView.findViewById(R.id.price_port);
            arrow = itemView.findViewById(R.id.arrow_p);
            button = itemView.findViewById(R.id.arrow_right_p);
        }
    }
}
