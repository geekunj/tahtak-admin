package com.sjjs.newsadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sjjs.newsadmin.MainActivity;
import com.sjjs.newsadmin.R;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth fAuth;

    EditText lEmailEt, lPwdEt;
    Button lLoginBtn;

    TextView createAccountText;
    String htmlTextForCreateAccount;
    String userID;

    @Override
    protected void onStart() {
        super.onStart();
        createAccountText.setEnabled(true);
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            userID = fAuth.getCurrentUser().getUid();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth= FirebaseAuth.getInstance();

        lEmailEt = findViewById(R.id.l_email_et);
        lPwdEt = findViewById(R.id.l_pwd_et);
        lLoginBtn = findViewById(R.id.l_login_btn);
        createAccountText = findViewById(R.id.tv_create_account);

        htmlTextForCreateAccount = "<u>Don't have an account ? create here</u>";
        createAccountText.setText(Html.fromHtml(htmlTextForCreateAccount));

        lLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lEmailEt.getText().toString().trim();
                String password = lPwdEt.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    lEmailEt.setError("email is required");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    lPwdEt.setError("password is required");
                    return;
                } else if (password.length() < 6) {
                    lPwdEt.setError("password must be greater than 6");
                    return;
                } else {
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                lLoginBtn.setClickable(false);
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Register user first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountText.setEnabled(false);
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}