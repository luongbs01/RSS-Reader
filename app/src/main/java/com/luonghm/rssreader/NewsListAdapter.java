package com.luonghm.rssreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.Holder> {
    private List<Newspaper_model> news_list;
    private LayoutInflater layoutInflater;

    public NewsListAdapter(Context context, List<Newspaper_model> news_list) {
        layoutInflater = LayoutInflater.from(context);
        this.news_list = news_list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.news_list_item, parent, false);
        return new Holder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.name.setText(news_list.get(position).getName());
        Picasso.get().load(news_list.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return news_list.size();
    }
    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView name;
        public final ImageView image;
        final NewsListAdapter adapter;

        public Holder(@NonNull View itemView, NewsListAdapter adapter) {
            super(itemView);
            name = itemView.findViewById(R.id.newspaper_name);
            image = itemView.findViewById(R.id.newspaper_image);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(v.getContext()).edit();
            editor.putInt("Newspaper_list", getLayoutPosition());
            editor.apply();
            Toast.makeText(v.getContext(), "Đã chọn " + new NewsPaperList().getItem(getLayoutPosition()).getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
