package com.group1.project3;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.group1.project3.model.DataModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    /*Array List to hold DataModel*/
    private ArrayList<DataModel> dataSet;
    private Context mContext;

    /**
     * Inner class that for recylerView object.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView assigned_user;
        private TextView assigned_date;
        private TextView content;
        private ImageView imageView;
        private TextView options;

        /**
         * Constructor to link Ui and instantiate aa recycler object.
         * @param itemView
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.assigned_user = (TextView) itemView.findViewById(R.id.assigned_user);
            this.assigned_user = (TextView) itemView.findViewById(R.id.assigned_date);
            this.content = (TextView) itemView.findViewById(R.id.content);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.options = (TextView) itemView.findViewById(R.id.options);
        }
    }

    /**
     * Constructor for Cardview.
     * @param data
     */
    public MyAdapter(ArrayList<DataModel> data, Context mContext) {
        this.dataSet = data;
        this.mContext = mContext;
    }

    /**
     * This method creates and initilizes a new ViewHolder.
     * @param parent
     * @param viewType
     * @return a new viewholder.
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

//        view.setOnClickListener(MainActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    /**
     * This method associates a ViewHolder with data.
     * @param holder
     * @param listPosition
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textTitle = holder.title;
        TextView textUser = holder.assigned_user;
        TextView textDate = holder.assigned_date;
        TextView textOptions = holder.options;
        ImageView imageView = holder.imageView;

        textTitle.setText(dataSet.get(listPosition).getTitle());
        textUser.setText(dataSet.get(listPosition).getAssigned_user());
        textDate.setText(dataSet.get(listPosition).getAssigned_date());
        imageView.setImageResource(dataSet.get(listPosition).getImage());
        textOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Display options menu*/
                PopupMenu popOptions = new PopupMenu(mContext,textOptions);
                popOptions.inflate(R.menu.menu_options);
                popOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.add_item:
                                Toast.makeText(mContext, "Add", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.remove_item:
                                Toast.makeText(mContext, "Delete", Toast.LENGTH_LONG).show();
                                break;

                            case R.id.sort_item:
                                Toast.makeText(mContext, "Sort", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });
                popOptions.show();
            }

        });
    }

    /**
     * This method returns the size of data.
     * @return size of data.
     */
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
