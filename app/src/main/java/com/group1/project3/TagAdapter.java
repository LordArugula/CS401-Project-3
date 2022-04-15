package com.group1.project3;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group1.project3.model.Tag;
import com.group1.project3.databinding.FragmentTagBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Tag}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private final List<Tag> mValues;

    public TagAdapter(List<Tag> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(FragmentTagBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public Tag mItem;

        public ViewHolder(FragmentTagBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
        }
    }
}