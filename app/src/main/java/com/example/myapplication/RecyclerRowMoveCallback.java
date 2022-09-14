package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.OnSelectionChangedListener;

public class RecyclerRowMoveCallback extends ItemTouchHelper.Callback {
    private RecyclerViewRowTouchHelperContract touchHelperContract;

    public RecyclerRowMoveCallback(RecyclerViewRowTouchHelperContract touchHelperContract){
        this.touchHelperContract = touchHelperContract;
    }
    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled(){
        return false;
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        return makeMovementFlags(dragFlag, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        this.touchHelperContract.onRowMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof MyAdapter1.MyViewHolder){
                MyAdapter1.MyViewHolder myViewHolder = (MyAdapter1.MyViewHolder)viewHolder;
                touchHelperContract.onRowSelected(myViewHolder);
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if(viewHolder instanceof MyAdapter1.MyViewHolder){
            MyAdapter1.MyViewHolder myViewHolder = (MyAdapter1.MyViewHolder)viewHolder;
            touchHelperContract.onRowClear(myViewHolder);
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface RecyclerViewRowTouchHelperContract{
        void onRowMoved(int from, int to);
        void onRowSelected(MyAdapter1.MyViewHolder myViewHolder);
        void onRowClear(MyAdapter1.MyViewHolder myViewHolder);
    }
}
