package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
    private List<String> peers;
    Context context;


    public MyAdapter2(Context ct, List<String> peer){
        context = ct;
        peers = peer;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.peers,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String peer = peers.get(position) + ",";
        holder.peerTxt.setPaintFlags(holder.peerTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.peerTxt.setText(peer);
        holder.peerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRes.count = 2;
                Intent intent2 = new Intent(context,searchRes.class);
                intent2.putExtra("stock",peers.get(position));
                context.startActivity(intent2);
            }
        });


    }

    @Override
    public int getItemCount() {
        return peers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView peerTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            peerTxt = itemView.findViewById(R.id.peers_text);
        }
    }
}
