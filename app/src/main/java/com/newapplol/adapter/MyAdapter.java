package com.newapplol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newapplol.R;

import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> data = Collections.emptyList();

    public MyAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.match_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String item = data.get(position);
        ((MyViewHolder) holder).textViewKDA.setText(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        protected TextView textViewKDA;

        public MyViewHolder(View itemView){
            super(itemView);
            textViewKDA = (TextView) itemView.findViewById(R.id.tv_kda);
        }
    }
}