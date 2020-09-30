package com.sjjs.newsadmin.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sjjs.newsadmin.R;
import com.sjjs.newsadmin.models.News;

public class NewsListAdapter extends FirestoreAdapter<NewsListAdapter.NewsViewHolder> {

    private StorageReference newsImageStorageReference;
    private NewsListAdapter.OnNewsItemClickListener listener;
    private Context context;
    private Query mQuery;
    private FirebaseFirestore mFirestore;

    public NewsListAdapter(Query query, NewsListAdapter.OnNewsItemClickListener listener, Context context) {
        super(query);
        this.listener = listener;
        this.context = context;
        mFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public NewsListAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new NewsListAdapter.NewsViewHolder(inflater.inflate(R.layout.news_list_item, parent, false));
    }

    public void deleteNewsItem(int position){
        String newsImageUrl, productImageUrl;
        String newsItemId;
        newsImageUrl = getSnapshot(position).getString("newsImageUrl");
        newsItemId = getSnapshot(position).getId();


        if(newsImageUrl != null && !TextUtils.isEmpty(newsImageUrl)){

            newsImageStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(newsImageUrl);
            newsImageStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(context, "News Item Deleted", Toast.LENGTH_SHORT).show();

                }
            });
        }
        getSnapshot(position).getReference().delete();
    }



    @Override
    public void onBindViewHolder(@NonNull NewsListAdapter.NewsViewHolder holder, int position) {

        holder.bind(getSnapshot(position), listener);
    }


    public interface OnNewsItemClickListener {
        void onNewsItemClick(DocumentSnapshot documentSnapshot, int position, String newsId);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView newsImage;
        TextView newsTitleText;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.iv_news_image);
            newsTitleText = itemView.findViewById(R.id.tv_newstitle);
        }

        public void bind(final DocumentSnapshot snapshot, final OnNewsItemClickListener listener) {

            News news = snapshot.toObject(News.class);

            newsTitleText.setText(news.getNewsTitle());
            Glide.with(context)
                    .load(news.getNewsImageUrl())
                    .into(newsImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onNewsItemClick(getSnapshot(position), position, snapshot.getId());
                    }
                }
            });

        }
    }

}
