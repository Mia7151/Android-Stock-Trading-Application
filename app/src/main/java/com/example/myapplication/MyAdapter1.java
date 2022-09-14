package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyViewHolder>
        implements RecyclerRowMoveCallback.RecyclerViewRowTouchHelperContract
{
    List<String> stock = new ArrayList<>();
    List<String> inc;
    List<Double> price_f;
    List<Double> d_f;
    List<Double> pc_d;
    Context context;
    public MyAdapter1(Context ct, List<String> s1, List<String> s2, List<Double> s3, List<Double> s4,List<Double> s5){
        context = ct;
        stock = s1;
        inc = s2;
        price_f = s3;
        d_f = s4;
        pc_d = s5;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fav_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("price222","start");
        holder.stock11.setText(stock.get(position));
        holder.inc11.setText(inc.get(position));
        Double price0 = price_f.get(position);
        DecimalFormat f = new DecimalFormat("##0.00");
        String f1 = f.format(price0);
        String c1 = "$" + f1;
        holder.price11.setText(c1);

        if (d_f.get(position) > 0) {
            holder.price_change_f.setTextColor(Color.rgb(0, 200, 0));
            holder.arrow11.setImageResource(R.drawable.trendingup1);
            holder.arrow11.setColorFilter(context.getResources().getColor(R.color.green));
        } else if (d_f.get(position) < 0) {
            holder.price_change_f.setTextColor(Color.rgb(200, 0, 0));
            holder.arrow11.setImageResource(R.drawable.trendingdown);
            holder.arrow11.setColorFilter(context.getResources().getColor(R.color.red));
        }
        String p1 = f.format(d_f.get(position));
        String p2 = f.format(pc_d.get(position));
        String p3 = "$" + p1 + " (" + p2+ "% )";
        holder.price_change_f.setText(p3);

        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(context,searchRes.class);
                intent2.putExtra("stock",stock.get(position));
                context.startActivity(intent2);

                //searchRes.stock_get = stock.get(position);
                //context.startActivity(new Intent(context,searchRes.class));
            }
        });




    }

    @Override
    public int getItemCount() {
        return stock.size() ;
    }

    @Override
    public void onRowMoved(int from, int to) {
        if(from < to){
            for (int i = from; i < to ; i++){
                Collections.swap(stock, i, i+1);
            }
        }else {
            for (int i = from; i > to; i--){
                Collections.swap(stock, i, i-1);
            }
        }
        notifyItemMoved(from, to);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView stock11, inc11,price11,price_change_f;
        ImageView arrow11,button1;
        RelativeLayout viewB,viewF;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stock11 = itemView.findViewById(R.id.stock_f);
            inc11 = itemView.findViewById(R.id.company_f);
            price11 = itemView.findViewById(R.id.price_f);
            price_change_f = itemView.findViewById(R.id.price1_f);
            arrow11 = itemView.findViewById(R.id.arrow_f);
            viewB = itemView.findViewById(R.id.backgroud_d);
            viewF = itemView.findViewById(R.id.row);
            button1 = itemView.findViewById(R.id.arrow_right);


        }
    }

    //for remove and restore
    public  void removeItem(int position){

        Gson gson = new Gson();
        stock.remove(position);
        inc.remove(position);
        price_f.remove(position);
        d_f.remove(position);
        pc_d.remove(position);

        SharedPreferences pref = context.getSharedPreferences("mykey", 0);
        SharedPreferences.Editor editor = pref.edit();
        String jsonText = gson.toJson(stock);
        String jsonText1 = gson.toJson(inc);
        String jsonText2 = gson.toJson(price_f);
        String jsonText3 = gson.toJson(d_f);
        String jsonText4 = gson.toJson(pc_d);

        editor.putString("stock_f", jsonText);
        editor.putString("stock_f_name", jsonText1);
        editor.putString("stock_f_pricea", jsonText2);
        editor.putString("stock_f_priceb", jsonText3);
        editor.putString("stock_f_pricec", jsonText4);

        editor.commit();

        notifyItemRemoved(position);
    }
    public void restoreItem(String s1, String s2, Double d1, Double d2, Double d3, int position){
        stock.add(position,s1);
        inc.add(position,s2);
        price_f.add(position,d1);
        d_f.add(position,d2);
        pc_d.add(position,d3);
        notifyItemInserted(position);

    }

}

