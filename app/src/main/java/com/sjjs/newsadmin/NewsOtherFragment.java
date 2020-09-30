package com.sjjs.newsadmin;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sjjs.newsadmin.utils.Config;


public class NewsOtherFragment extends Fragment {

    private TextInputEditText keywordsText, videoUrlText;
    private Switch newsPublishSwitch;
    private Button newsSubmitButton;
    private FirebaseFirestore db;

    public NewsOtherFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_other, container, false);

        keywordsText = view.findViewById(R.id.et_search_keys);
        videoUrlText = view.findViewById(R.id.et_video_url);
        newsPublishSwitch = view.findViewById(R.id.switch_publish_news);
        newsSubmitButton = view.findViewById(R.id.btn_submit_news);

        db = FirebaseFirestore.getInstance();

        keywordsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Config.newsState.setSearchKeywords(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        videoUrlText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Config.newsState.setVideoUrl(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newsPublishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config.newsState.setPublished(isChecked);
            }
        });

        newsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog newsItemuploadProgress = new ProgressDialog(getActivity());
                newsItemuploadProgress.setCanceledOnTouchOutside(false);
                newsItemuploadProgress.setMessage("loading....");
                newsItemuploadProgress.setTitle("Uploading News Item");
                newsItemuploadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                newsItemuploadProgress.show();

                if (Config.isNewsItemContainsEmpty()){
                    db.collection("news").add(Config.newsState)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    newsItemuploadProgress.dismiss();
                                    Toast.makeText(getContext(), "success: news added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("NewsOther", "failed to add news");
                                }
                            });
                }

            }
        });





        return view;
    }
}