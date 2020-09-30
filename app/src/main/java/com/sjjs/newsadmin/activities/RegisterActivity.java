package com.sjjs.newsadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sjjs.newsadmin.MainActivity;
import com.sjjs.newsadmin.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText rUserNameEt, rEmailEt, rPassEt, rConfirmPassEt;
    Button rRegBtn;
    CheckBox agreeToTermsCheck;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    FirebaseFirestore fstore;

    String userID, email, confirmPassword, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rUserNameEt = (EditText) findViewById(R.id.r_un_et);
        rEmailEt = (EditText) findViewById(R.id.r_email_et);
        rPassEt = (EditText) findViewById(R.id.r_pass_et);
        rConfirmPassEt = (EditText) findViewById(R.id.r_cn_pwd_et);
        rRegBtn = (Button) findViewById(R.id.r_reg_btn);
        agreeToTermsCheck = (CheckBox) findViewById(R.id.check_terms_and_condtions);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();

        if(currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        rPassEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() < 6){
                    rPassEt.setError(" Password must be greater than 6 characters");
                    //rRegBtn.setClickable(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = rEmailEt.getText().toString().trim();
                name = rUserNameEt.getText().toString().trim();
                password = rPassEt.getText().toString().trim();
                confirmPassword = rConfirmPassEt.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    rUserNameEt.setError("user name is required");
                }
                else if(TextUtils.isEmpty(email)){
                    rEmailEt.setError("email is required");
                }
                else if(TextUtils.isEmpty(password)){
                    rPassEt.setError("password is required");
                }
                else if(TextUtils.isEmpty(confirmPassword)){
                    rConfirmPassEt.setError("confirm your password");
                }
                else if(!password.equals(confirmPassword)){
                    rConfirmPassEt.setError("pass dont match");

                }
                else if(!agreeToTermsCheck.isChecked()){
                    agreeToTermsCheck.setError("please agree to the terms to sign in");

                }
                else {
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                rRegBtn.setEnabled(false);
                                Toast.makeText(RegisterActivity.this, "User Created in Auth .", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("users").document(userID);
                                HashMap<String, Object> user = new HashMap<>();
                                user.put("userName", name);
                                user.put("userEmail", email);
                                user.put("password", password);
                                //user.put("lastActive", )
                                documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {

                                        Toast.makeText(RegisterActivity.this, "User set in user table ", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();

                            } else {

                                Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });

    }
}