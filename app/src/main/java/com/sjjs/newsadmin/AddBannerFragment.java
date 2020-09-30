package com.sjjs.newsadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sjjs.newsadmin.models.Banner;
import com.sjjs.newsadmin.models.News;
import com.sjjs.newsadmin.utils.BitmapTransformer;
import com.sjjs.newsadmin.utils.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddBannerFragment extends Fragment {

    private String[] POSITIONS, CATEGORIES;
    private static final int REQUEST_IMAGE = 1;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private String imageUrl;

    private AutoCompleteTextView categoryDropdown, posDropdown;
    private ArrayAdapter<String> categoryAdapter, posAdapter;
    private TextInputEditText bannerNameText;
    private ImageView bannerImage;
    private ImageButton addBannerImageButton;
    private Button submitBannerButton;
    private ProgressDialog uploadProgress;
    private Switch bannerPublishSwitch;

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db;


    public AddBannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_banner, container, false);

        categoryDropdown = view.findViewById(R.id.category_dropdown);
        bannerPublishSwitch = view.findViewById(R.id.switch_publish_ban);
        bannerImage = view.findViewById(R.id.iv_banner_image);
        addBannerImageButton = view.findViewById(R.id.ib_add_image);
        posDropdown = view.findViewById(R.id.pos_dropdown);
        submitBannerButton = view.findViewById(R.id.btn_submit_ban);
        bannerNameText = view.findViewById(R.id.et_banner_name);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Banner");

        POSITIONS = getResources().getStringArray(R.array.banner_positions);

        if (Config.globalCategoryList != null) {
            CATEGORIES = Config.globalCategoryList.toArray(new String[0]);
        }

        categoryAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, CATEGORIES);
        posAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, POSITIONS);

        categoryDropdown.setAdapter(categoryAdapter);
        posDropdown.setAdapter(posAdapter);

        bannerNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Config.bannerState.setBannerName(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        categoryDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Config.bannerState.setCategoryName(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bannerPublishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config.bannerState.setPublished(isChecked);
            }
        });

        posDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Config.bannerState.setBannerPosition(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addBannerImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_IMAGE);
            }
        });

        submitBannerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(Config.isBannerItemContainsEmpty()){
                    Toast.makeText(getContext(), "Fill all the details", Toast.LENGTH_SHORT).show();
                } else {

                    final ProgressDialog bannerUploadProgress = new ProgressDialog(getActivity());
                    bannerUploadProgress.setCanceledOnTouchOutside(false);
                    bannerUploadProgress.setMessage("loading....");
                    bannerUploadProgress.setTitle("Uploading Banner");
                    bannerUploadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    bannerUploadProgress.show();

                    db.collection("banner").add(Config.bannerState)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    bannerUploadProgress.dismiss();
                                    Toast.makeText(getContext(), "success: banner added", Toast.LENGTH_SHORT).show();
                                    Config.bannerState = new Banner();
                                    resetFields();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("AddBannerFrag", "failed to add banner");
                                }
                            });
                }
            }
        });


        return view;
    }

    private void resetFields() {
        bannerNameText.setText("");
        categoryDropdown.setText("");
        bannerImage.setImageResource(android.R.color.transparent);
        bannerPublishSwitch.setChecked(false);
        posDropdown.setText("");
        imageUrl = null;
    }

    private void putImageInStorage(StorageReference storageReference, final Uri uri, final String key) {
        uploadProgress = new ProgressDialog(getActivity());
        uploadProgress.setCanceledOnTouchOutside(false);
        uploadProgress.setMessage("uploading image....");
        uploadProgress.setTitle("Please Wait");
        uploadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        uploadProgress.show();
        storageReference.putFile(uri).addOnCompleteListener(getActivity(),
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("AddNewsFrag", "Image upload task was successful ");
                                                uploadProgress.dismiss();
                                                imageUrl = task.getResult().toString();
                                                Config.bannerState.setBannerImageUrl(imageUrl);
                                                Uri imageUri = Uri.parse(uri.toString());
                                                try {
                                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                                                    Glide.with(getActivity()).load(bitmap).transform(new BitmapTransformer(MAX_WIDTH, MAX_HEIGHT)).into(bannerImage);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }
                                    });
                        } else {
                            Log.d("AddBannerFrag", "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();//evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        List<Uri> imageUriList = new ArrayList<>();
                        for (int i = 0; i < count; i++)
                            imageUriList.add(data.getClipData().getItemAt(i).getUri());
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                } else if (data.getData() != null) {
                    String imagePath = data.getData().getPath();
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
                Uri uri = data.getData();
                //Config.selectedImageUri = uri;
                Log.d("AddBannerFrag", "Uri: " + uri.toString());
                //TODO : add firestore logic here


                StorageReference storageReference = FirebaseStorage.getInstance()
                        .getReference(mFirebaseUser.getUid())
                        .child("bannerImages")
                        .child(uri.getLastPathSegment());
                putImageInStorage(storageReference, uri, "123456789");

            }
        }
    }

}