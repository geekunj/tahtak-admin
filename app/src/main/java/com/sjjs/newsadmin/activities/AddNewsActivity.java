package com.sjjs.newsadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sjjs.newsadmin.R;
import com.sjjs.newsadmin.models.News;
import com.sjjs.newsadmin.utils.BitmapTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNewsActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1;
    private static String[] CATEGORIES;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String imageUrl;

    private TextInputEditText titleText, contentText, authorText, dateTimeText;
    private ImageView newsImage;
    private ImageButton addNewsButton;
    private Button submitNewsButton;
    private ProgressDialog uploadProgress;
    private AutoCompleteTextView categoryDropdown;
    private ArrayAdapter<String> categoryAdapter;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mFirebaseFirestoreReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        titleText = findViewById(R.id.et_news_title);
        contentText = findViewById(R.id.et_news_content);
        authorText = findViewById(R.id.et_news_author);
        dateTimeText = findViewById(R.id.et_news_date_time);
        newsImage = findViewById(R.id.iv_news_image);
        addNewsButton = findViewById(R.id.ib_add_image);
        submitNewsButton = findViewById(R.id.btn_submit_news);
        categoryDropdown = findViewById(R.id.category_dropdown);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestoreReference = mFirestore.collection("news");

        CATEGORIES = getResources().getStringArray(R.array.news_categories);
        categoryAdapter = new ArrayAdapter<>(AddNewsActivity.this, R.layout.support_simple_spinner_dropdown_item, CATEGORIES);
        categoryDropdown.setAdapter(categoryAdapter);

        dateTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                dateTimeText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewsActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                dateTimeText.append(" " + hourOfDay + ":" + minute);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        addNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_IMAGE);
            }
        });

        submitNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog newsItemuploadProgress = new ProgressDialog(AddNewsActivity.this);
                newsItemuploadProgress.setCanceledOnTouchOutside(false);
                newsItemuploadProgress.setMessage("loading....");
                newsItemuploadProgress.setTitle("Uploading News Item");
                newsItemuploadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                newsItemuploadProgress.show();
                News news = new News();
                if (!TextUtils.isEmpty(titleText.getText().toString().trim())){

                    news.setNewsTitle(titleText.getText().toString().trim());
                }
                if(!TextUtils.isEmpty(categoryDropdown.getText().toString().trim())){

                    news.setNewsCategory(categoryDropdown.getText().toString().trim());

                }
                if(!TextUtils.isEmpty(contentText.getText().toString().trim())){

                    news.setNewsContent(contentText.getText().toString().trim());
                }
                if(!TextUtils.isEmpty(authorText.getText().toString().trim())){

                    news.setAuthor(authorText.getText().toString().trim());
                }
                if(!TextUtils.isEmpty(dateTimeText.getText().toString().trim())){

                    news.setDatePublished(dateTimeText.getText().toString().trim());
                }

                if(!TextUtils.isEmpty(imageUrl)){

                    news.setNewsImageUrl(imageUrl);
                    mFirebaseFirestoreReference.add(news).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                newsItemuploadProgress.dismiss();
                                Toast.makeText(AddNewsActivity.this, "news added", Toast.LENGTH_SHORT).show();
                                titleText.setText("");
                                contentText.setText("");
                                categoryDropdown.setText("");
                                imageUrl="";
                                newsImage.setImageResource(android.R.color.transparent);
                                addNewsButton.setVisibility(View.VISIBLE);
                                authorText.setText("");
                                dateTimeText.setText("");
                            }
                            else{
                                Log.d("AddNewsFrag", "failed");
                            }
                        }
                    });

                }else{
                    Toast.makeText(AddNewsActivity.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void putImageInStorage(StorageReference storageReference, final Uri uri, final String key) {
        uploadProgress = new ProgressDialog(AddNewsActivity.this);
        uploadProgress.setCanceledOnTouchOutside(false);
        uploadProgress.setMessage("uploading image....");
        uploadProgress.setTitle("Please Wait");
        uploadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        uploadProgress.show();
        storageReference.putFile(uri).addOnCompleteListener(AddNewsActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(AddNewsActivity.this, new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("AddNewsFrag", "Image upload task was successful ");
                                                uploadProgress.dismiss();

                                                imageUrl = task.getResult().toString();
                                                Uri imageUri = Uri.parse(uri.toString());
                                                try {
                                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddNewsActivity.this.getContentResolver(), imageUri);
                                                    Glide.with(AddNewsActivity.this).load(bitmap).transform(new BitmapTransformer(MAX_WIDTH, MAX_HEIGHT)).into(newsImage);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }
                                    });
                        } else {
                            Log.w("CatalogueMain", "Image upload task was not successful.",
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
                Log.d("AddNewsFrag", "Uri: " + uri.toString());
                //TODO : add firestore logic here


                StorageReference storageReference = FirebaseStorage.getInstance()
                        .getReference(mFirebaseUser.getUid())
                        .child("newsImages")
                        .child(uri.getLastPathSegment());
                putImageInStorage(storageReference, uri, "123456789");

            }
        }
    }

}