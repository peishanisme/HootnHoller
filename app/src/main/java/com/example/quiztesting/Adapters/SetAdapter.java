package com.example.quiztesting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiztesting.Models.SetModel;
import com.example.quiztesting.R;
import com.example.quiztesting.RecyViewInterface;
import com.example.quiztesting.databinding.ItemSetBinding;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.viewHolder> {

    private RecyViewInterface recyViewInterface;
    Context context;
    ArrayList<SetModel> list;

    public SetAdapter(RecyViewInterface recyViewInterface, Context context, ArrayList<SetModel> list) {
        this.recyViewInterface = recyViewInterface;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_set, parent, false);
        return new viewHolder(view, recyViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SetModel model = list.get(position);
        holder.binding.setName.setText(model.getSetName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        ItemSetBinding binding;
        public viewHolder(@NonNull View itemView, RecyViewInterface recyViewInterface) {
            super(itemView);
            binding = ItemSetBinding.bind(itemView);

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
