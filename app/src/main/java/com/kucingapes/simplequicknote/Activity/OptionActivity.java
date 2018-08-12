package com.kucingapes.simplequicknote.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kucingapes.simplequicknote.Adapter.BackupAdapter;
import com.kucingapes.simplequicknote.Model.ModelBackup;
import com.kucingapes.simplequicknote.Model.ModelHistory;
import com.kucingapes.simplequicknote.R;
import com.kucingapes.simplequicknote.SharedPreferences.SharedList;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class OptionActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private String KEY = "resultKey";
    private String INDEX = "listhistory";
    private String COLOR_KEY = "colorResult";

    private ProgressDialog mProgressDialog;

    // [START declare_auth]
    private FirebaseAuth auth;
    private FirebaseUser user;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    private List<ModelBackup> backupList = new ArrayList<>();
    private List<ModelHistory> stringList;
    private BackupAdapter adapter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:kucingapes@outlook.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Simple Quick Note");
                startActivity(intent);
            }
        });

        findViewById(R.id.web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kucingapes.github.io"));
                startActivity(intent);
            }
        });

        String versionName = "";
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView textVersion = findViewById(R.id.version);
        textVersion.setText(getResources().getString(R.string.app_name)+" v."+versionName);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //setupListBackup();
    }

    private void setupListBackup() {
        //Toast.makeText(getApplicationContext(), "bor", Toast.LENGTH_SHORT).show();
        final RecyclerView recyclerView = findViewById(R.id.list_backup);
        adapter = new BackupAdapter(backupList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LayoutMarginDecoration marginDecoration = new LayoutMarginDecoration(10);
        marginDecoration.setPadding(recyclerView, 10);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.addItemDecoration(marginDecoration);

        if (user != null) {
            String authName = user.getDisplayName();
            if (authName != null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("userdata").child(authName);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        backupList.clear();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            ModelBackup modelBackup = snapshot.getValue(ModelBackup.class);
                            if (modelBackup != null) {
                                modelBackup.setDate(snapshot.getKey());
                                backupList.add(modelBackup);
                            }

                        }
                        findViewById(R.id.progbar).setVisibility(View.GONE);
                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                DividerItemDecoration.VERTICAL));
                        recyclerView.setAdapter(adapter);
                        Collections.reverse(backupList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "empty", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void setupBackup() {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-YYYY HH:mm:ss");
        String strDate = sdf.format(c.getTime());

        SharedPreferences preferences = getSharedPreferences("ganteng", MODE_PRIVATE);
        String dataList = preferences.getString(INDEX, "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        SharedList sharedList = new SharedList();
        stringList = sharedList.getFavorites(getApplicationContext());

        if (stringList == null) {
            stringList = new ArrayList<>();
        }

        String size = String.valueOf(stringList.size());

        user = auth.getCurrentUser();
        if (user != null) {
            String authName = user.getDisplayName();
            if (authName != null) {
                myRef.child("userdata").child(authName).child(strDate).child("json").setValue(dataList);
                myRef.child("userdata").child(authName).child(strDate).child("size").setValue(size);
                adapter.notifyDataSetChanged();
                Toast.makeText(OptionActivity.this, "saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            hideProgressDialog();

                            recreate();
                        } else {
                            Log.w(TAG,
                                    "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(FirebaseUser user) {
        final Button sign = findViewById(R.id.sign_btn);
        final Button backup = findViewById(R.id.btn_backup);
        final TextView signName = findViewById(R.id.sign_name);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupBackup();
            }
        });

        if (user != null){
            findViewById(R.id.progbar).setVisibility(View.VISIBLE);
            signName.setVisibility(View.VISIBLE);
            signName.setText("You sign as "+user.getDisplayName());
            sign.setVisibility(View.GONE);
            backup.setVisibility(View.VISIBLE);
            setupListBackup();
        } else {
            findViewById(R.id.progbar).setVisibility(View.GONE);
            signName.setVisibility(View.GONE);
            sign.setVisibility(View.VISIBLE);
            backup.setVisibility(View.GONE);
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
