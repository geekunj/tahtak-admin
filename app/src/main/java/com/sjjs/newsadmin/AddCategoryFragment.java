package com.sjjs.newsadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sjjs.newsadmin.models.Category;
import com.sjjs.newsadmin.utils.Config;

public class AddCategoryFragment extends Fragment {

    private Category mCategory;

    private TextInputEditText categoryNameText;
    private Switch catPublishSwitch;
    private Button submitCatButton;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mFirebaseFirestoreReference;

    public AddCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_category, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Category");

        categoryNameText = view.findViewById(R.id.et_cat_name);
        catPublishSwitch = view.findViewById(R.id.switch_publish_cat);
        submitCatButton = view.findViewById(R.id.btn_submit_cat);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestoreReference = mFirestore.collection("category");
        mCategory = new Category();

        mCategory.setPublished(false);


        catPublishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCategory.setPublished(isChecked);
            }
        });


        submitCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(categoryNameText.getText().toString().trim())){
                    mCategory.setCategoryName(categoryNameText.getText().toString().trim());
                    mFirebaseFirestoreReference.add(mCategory).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("AddCategoryFrag", "success");
                            Toast.makeText(getContext(), "Category Added ", Toast.LENGTH_SHORT).show();
                            Config.globalCategoryList.add(categoryNameText.getText().toString().trim());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to add category: " + e + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        return view;
    }
}