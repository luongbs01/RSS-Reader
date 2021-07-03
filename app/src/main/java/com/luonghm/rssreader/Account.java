package com.luonghm.rssreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Account extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 19;
    private FirebaseAuth mAuth;
    private DbReadPosts dbReadPosts = new DbReadPosts(this);
    private Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("737174498367-pha454f66q0clsbq548tokkeqre6q620.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent.getIntExtra("SignOut", 0) == 1) {
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                dbReadPosts.deleteAll(DbReadPosts.TABLE_NAME1);
                dbReadPosts.deleteAll(DbReadPosts.TABLE_NAME2);
            });
            FirebaseAuth.getInstance().signOut();
            if (mAuth.getCurrentUser() == null)
                mAuth.signInAnonymously();
        }

        findViewById(R.id.sign_in_button).setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    signIn();
                    break;
                default:
                    break;
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
            FirebaseAuth.getInstance().getCurrentUser().delete();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(Account.this);
                            myAlertBuilder.setTitle("Đăng nhập thành công");
                            myAlertBuilder.setMessage("Bạn có muốn đồng bộ dữ liệu đã có với tài khoản không?");
                            myAlertBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new myTask().execute(DbReadPosts.TABLE_NAME1);
                                    new myTask().execute(DbReadPosts.TABLE_NAME2);
                                    finish();
                                }

                            });
                            myAlertBuilder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbReadPosts.deleteAll(DbReadPosts.TABLE_NAME1);
                                    dbReadPosts.deleteAll(DbReadPosts.TABLE_NAME2);
                                    new myTask2().execute(DbReadPosts.TABLE_NAME1);
                                    new myTask2().execute(DbReadPosts.TABLE_NAME2);
                                    finish();
                                }
                            });
                            myAlertBuilder.show();
                        }
                    }
                });

            } catch (ApiException e) {
                Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private class myTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            List<RSSModel> rssModelList = new ArrayList<>();
            rssModelList.addAll(dbReadPosts.getAll(strings[0]));
            FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                    child(strings[0]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RSSModel rssModel = dataSnapshot.getValue(RSSModel.class);
                        if (dbReadPosts.check(rssModel, strings[0])) {
                            dbReadPosts.insert(rssModel, strings[0]);
                        } else rssModelList.remove(rssModel);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            for (int i = 0; i < rssModelList.size(); i++) {
                FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child(strings[0]).child(rssModelList.get(i).getTitle()).setValue(rssModelList.get(i));
            }

            return true;
        }
    }

    private class myTask2 extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                    child(strings[0]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RSSModel rssModel = dataSnapshot.getValue(RSSModel.class);
                        if (dbReadPosts.check(rssModel, strings[0]))
                            dbReadPosts.insert(rssModel, strings[0]);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return true;
        }
    }

}