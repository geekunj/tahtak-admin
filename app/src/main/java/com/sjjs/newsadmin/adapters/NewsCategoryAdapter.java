package com.sjjs.newsadmin.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.sjjs.newsadmin.R;
import com.sjjs.newsadmin.models.Category;
import com.sjjs.newsadmin.models.News;
import com.sjjs.newsadmin.utils.BitmapTransformer;
import com.sjjs.newsadmin.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsCategoryAdapter extends FirestoreAdapter<NewsCategoryAdapter.CategoryViewHolder> {

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    private Context context;
    private List<Category> data = new ArrayList<>();
    private CategoryItemClickListener categoryItemClickListener;
    private FirebaseFirestore db;


    public NewsCategoryAdapter(Query query, Context context, CategoryItemClickListener categoryItemClickListener) {
        super(query);
        this.context = context;
        //this.data = data;
        this.categoryItemClickListener = categoryItemClickListener;
        db = FirebaseFirestore.getInstance();
    }


    public interface CategoryItemClickListener {
        void onCategoryItemClick(DocumentSnapshot documentSnapshot, int clickedItemIndex, String categoryName);
    }


    @NonNull
    @Override
    public NewsCategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_category_list_item, parent, false);
        CategoryViewHolder holder = new NewsCategoryAdapter.CategoryViewHolder(view);
        return holder;
    }

    public void deleteCategory(int position){

        String categoryId;

        categoryId = getSnapshot(position).getId();

        getSnapshot(position).getReference().delete();
        Config.globalCategoryList.remove(getSnapshot(position).get("categoryName"));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsCategoryAdapter.CategoryViewHolder holder, int position) {
        /*Category category = data.get(position);
        holder.categoryName.setText(category.getCategoryName());
        Log.d("ImageRes", category.getCategoryImgRes() + "");*/
        /*Glide.with(holder.itemView.getContext())
                .load(category.getCategoryImgRes())
                .into(holder.categoryImage);*/
        holder.bind(getSnapshot(position), categoryItemClickListener);
    }



    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        TextView newsCountText;
        Switch catPublishSwitch;
        //ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.tv_cat_name);
            newsCountText = itemView.findViewById(R.id.tv_news_count);
            catPublishSwitch = itemView.findViewById(R.id.switch_publish_cat);
            //categoryImage = itemView.findViewById(R.id.iv_cat_image);
            //itemView.setOnClickListener(this);
        }


        public void bind(final DocumentSnapshot snapshot, final CategoryItemClickListener listener) {

            final Category category = snapshot.toObject(Category.class);

            categoryName.setText(category.getCategoryName());
            catPublishSwitch.setChecked(category.isPublished());
            /*Glide.with(context)
                    .load(news.getNewsImageUrl())
                    .into(newsImage);*/
            catPublishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Map<String, Object> updatedCategory = new HashMap<>();
                    updatedCategory.put("published", isChecked);
                    db.collection("category")
                            .document(snapshot.getId())
                            .update(updatedCategory)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("CategoryAdapter", "success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("CategoryAdapter", "failed");
                                }
                            });
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onCategoryItemClick(getSnapshot(position), position, category.getCategoryName());
                    }
                }
            });

        }

    }

}
