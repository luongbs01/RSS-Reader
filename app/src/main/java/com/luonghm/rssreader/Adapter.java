package com.luonghm.rssreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private List<RSSModel> rssModelList;
    private LayoutInflater layoutInflater;
    private String TABLE_NAME;
    private boolean check = true;
    private DbReadPosts dbReadPosts;
    private SharedPreferences sharedPreferences;

    public Adapter(Context context, List<RSSModel> rssModelList) {
        layoutInflater = LayoutInflater.from(context);
        this.rssModelList = rssModelList;
    }

    @NonNull
    @Override
    public Adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.item, parent, false);
        return new Holder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.Holder holder, int position) {
        holder.title.setText(rssModelList.get(position).getTitle());
        Picasso.get().load(rssModelList.get(position).getImage()).into(holder.imageView);
        holder.pubDate.setText(rssModelList.get(position).getPubDate());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(holder.title.getContext());
        if (sharedPreferences.getString("text_size", "1").equalsIgnoreCase("0"))
            holder.title.setTextSize(16);
        else if (sharedPreferences.getString("text_size", "1").equalsIgnoreCase("1"))
            holder.title.setTextSize(20);
        else holder.title.setTextSize(24);
    }


    @Override
    public int getItemCount() {
        if (rssModelList != null)
            return rssModelList.size();
        else return 0;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final TextView title;
        public final ImageView imageView;
        public final TextView pubDate;
        final Adapter adapter;

        public Holder(@NonNull View itemView, Adapter adapter) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            pubDate = itemView.findViewById(R.id.pubDate);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            dbReadPosts = new DbReadPosts(v.getContext());
            RSSModel rssModel = rssModelList.get(getLayoutPosition());
            if (dbReadPosts.check(rssModel, DbReadPosts.TABLE_NAME1)) {
                FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child(DbReadPosts.TABLE_NAME1).child(rssModel.getTitle()).setValue(rssModel);
                dbReadPosts.insert(rssModel, DbReadPosts.TABLE_NAME1);
            }
            if (v.getContext() instanceof MainActivity3 || v.getContext() instanceof MainActivity4) {
                int mPosition = getLayoutPosition();
                String url = rssModelList.get(mPosition).getLink();
                Intent intent = new Intent(v.getContext(), MainActivity2.class);
                intent.putExtra("title", rssModelList.get(mPosition).getTitle());
                intent.putExtra("image", rssModelList.get(mPosition).getImage());
                intent.putExtra("pubDate", rssModelList.get(mPosition).getPubDate());
                intent.putExtra("link", url);
                v.getContext().startActivity(intent);
            } else {
                WebViewFragment webViewFragment = WebViewFragment.newInstance(rssModel.getTitle(),
                        rssModel.getImage(), rssModel.getPubDate(), rssModel.getLink());
                FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.add(R.id.fragment_container,
                        webViewFragment).addToBackStack(null).commit();
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if (v.getContext() instanceof MainActivity3 || v.getContext() instanceof MainActivity4) {
                if (v.getContext() instanceof MainActivity3)
                    TABLE_NAME = DbReadPosts.TABLE_NAME1;
                else TABLE_NAME = DbReadPosts.TABLE_NAME2;
                AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(v.getContext());
                myAlertBuilder.setTitle("Xóa tin");
                myAlertBuilder.setMessage("Bạn có chắc chắn muốn xóa tin này không?");
                myAlertBuilder.setPositiveButton("Đồng ý", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference()
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(TABLE_NAME)
                            .child(rssModelList.get(getLayoutPosition()).getTitle()).removeValue();
                    dbReadPosts = new DbReadPosts(v.getContext());
                    dbReadPosts.delete(rssModelList.get(getLayoutPosition()), TABLE_NAME);
                    rssModelList.remove(getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());
                });
                myAlertBuilder.setNegativeButton("Hủy bỏ", (dialog, which) -> {
                });
                myAlertBuilder.show();
                return false;
            }
            return false;
        }
    }
}
