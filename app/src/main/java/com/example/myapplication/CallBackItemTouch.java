package com.example.myapplication;

import androidx.recyclerview.widget.RecyclerView;

public interface CallBackItemTouch {
    void itemTuchOnMove(int oldPosition, int newPosition);
    void onSwiped(RecyclerView.ViewHolder viewHolder, int position);

}
