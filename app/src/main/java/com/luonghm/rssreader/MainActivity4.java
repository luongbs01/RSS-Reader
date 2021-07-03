package com.luonghm.rssreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<RSSModel> rssModelList;
    private Button button;
    private Adapter adapter;
    private DbReadPosts dbReadPosts = new DbReadPosts(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        button = findViewById(R.id.button4);
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button.setOnClickListener(v -> {
            AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(v.getContext());
            myAlertBuilder.setTitle("Xóa tất cả");
            myAlertBuilder.setMessage("Bạn có chắc chắn muốn xóa tất cả  tin không?");
            myAlertBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            child(DbReadPosts.TABLE_NAME2).removeValue();
                    dbReadPosts.deleteAll(DbReadPosts.TABLE_NAME2);
                    int size = rssModelList.size();
                    rssModelList.clear();
                    adapter.notifyItemRangeRemoved(0, size);
                }
            });
            myAlertBuilder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            myAlertBuilder.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rssModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview4);
        int gridColumnCount =
                getResources().getInteger(R.integer.grid_column_count);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity4.this,gridColumnCount));
        rssModelList.addAll(dbReadPosts.getAll(DbReadPosts.TABLE_NAME2));
        adapter = new Adapter(MainActivity4.this, rssModelList);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(DbReadPosts.TABLE_NAME2)
                        .child(rssModelList.get(viewHolder.getLayoutPosition()).getTitle()).removeValue();
                dbReadPosts.delete(rssModelList.get(viewHolder.getLayoutPosition()), DbReadPosts.TABLE_NAME2);
                rssModelList.remove(viewHolder.getLayoutPosition());
                adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}