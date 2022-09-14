package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

public class MyItemTuchHelperCallBack extends ItemTouchHelper.Callback {

    CallBackItemTouch callBackItemTouch;

    public MyItemTuchHelperCallBack(CallBackItemTouch callBackItemTouch){
        this.callBackItemTouch = callBackItemTouch;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        callBackItemTouch.itemTuchOnMove(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        callBackItemTouch.onSwiped(viewHolder,viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
        }else{
            //maybe
            final View forigroungView = ((MyAdapter1.MyViewHolder)viewHolder).viewB;
            getDefaultUIUtil().onDrawOver(c,recyclerView,forigroungView,dX,dY,actionState,isCurrentlyActive);

        }
        // in this we will


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if(actionState != ItemTouchHelper.ACTION_STATE_DRAG) {
            final View forigroungView = ((MyAdapter1.MyViewHolder) viewHolder).viewB;
            getDefaultUIUtil().onDrawOver(c, recyclerView, forigroungView, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //super.clearView(recyclerView, viewHolder);
        final View forigroungView = ((MyAdapter1.MyViewHolder) viewHolder).viewB;
        forigroungView.setBackgroundColor(ContextCompat.getColor(((MyAdapter1.MyViewHolder) viewHolder).viewF.getContext(),R.color.white));
        getDefaultUIUtil().clearView(forigroungView);

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder != null){
            final View foregroundView = ((MyAdapter1.MyViewHolder)viewHolder).viewF;
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                foregroundView.setBackgroundColor(Color.WHITE);
            }
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


}
