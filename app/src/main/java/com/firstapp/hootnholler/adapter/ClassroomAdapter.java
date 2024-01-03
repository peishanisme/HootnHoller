package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.Models.ClassroomModel;
import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.RecyViewInterface;
import com.firstapp.hootnholler.databinding.ItemClassroomBinding;

import java.util.ArrayList;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.viewHolder> {

    private RecyViewInterface recyViewInterface;
    Context context;
    ArrayList<ClassroomModel> list;
    ArrayList<Integer> selectedItems;

    public ClassroomAdapter(RecyViewInterface recyViewInterface, Context context, ArrayList<ClassroomModel> list) {
        this.recyViewInterface = recyViewInterface;
        this.context = context;
        this.list = list;
        this.selectedItems = new ArrayList<>();
    }

    public void setSelectedItems(int position) {
        Integer pos = position;
        selectedItems.add(pos);
        notifyItemChanged(position);
    }

    public void deleteSelectedItems(int position) {
        Integer pos = position;
        selectedItems.remove(pos);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classroom, parent, false);
        return new viewHolder(view, recyViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.binding.classroomName.setText(list.get(position).getClassroomName());

        if (selectedItems.contains(position)) {
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#909090"));
        } else {
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#F4C7EA"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        ItemClassroomBinding binding;
        public viewHolder(@NonNull View itemView, RecyViewInterface recyViewInterface) {
            super(itemView);
            binding = ItemClassroomBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyViewInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(recyViewInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });
        }

    }
}
