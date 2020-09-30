package com.sjjs.newsadmin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sjjs.newsadmin.R;
import com.sjjs.newsadmin.activities.NewsListActivity;
import com.sjjs.newsadmin.activities.RegisterActivity;
import com.sjjs.newsadmin.adapters.NewsCategoryAdapter;
import com.sjjs.newsadmin.models.Banner;
import com.sjjs.newsadmin.models.Category;
import com.sjjs.newsadmin.utils.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class HomeFragment extends Fragment implements NewsCategoryAdapter.CategoryItemClickListener {

    private RecyclerView categoryRecycler;
    private NewsCategoryAdapter categoryAdapter;
    /*private RecyclerView.LayoutManager layoutManager;
    private List<Category>newsCategoryList;
    private int[] CATEGORY_IMAGE_RES = {R.drawable.world,
            R.drawable.sports, R.drawable.entertainment,
            R.drawable.national, R.drawable.business, R.drawable.tech,
            R.drawable.science, R.drawable.health, R.drawable.lifestyle};*/

    private Query mQuery;
    private FirebaseFirestore mFirestore;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (categoryAdapter != null) {
            categoryAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (categoryAdapter != null) {
            categoryAdapter.stopListening();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecycler = view.findViewById(R.id.rv_categories);

        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("category").orderBy("categoryName").limit(50);

        if(Config.bannerState == null){
            Config.bannerState = new Banner();
        }

        if(Config.globalCategoryList == null){
            Config.globalCategoryList = new ArrayList<>();
            mFirestore.collection("category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                Category category = new Category();
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for(QueryDocumentSnapshot document: task.getResult()){
                            category = document.toObject(Category.class);
                            Config.globalCategoryList.add(category.getCategoryName());
                        }
                    } else {
                        Log.d("EditNewsActivity", "Error getting categories: ", task.getException());
                    }
                }
            });
        }



       /* newsCategoryList = new ArrayList<>();

        int i = 0;
        for(String categoryName: getResources().getStringArray(R.array.news_categories)){
            Category categoryModel = new Category(categoryName, CATEGORY_IMAGE_RES[i]);
            newsCategoryList.add(categoryModel);
            i++;
        }*/


        categoryAdapter = new NewsCategoryAdapter(mQuery, this.getActivity(), this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    categoryRecycler.setVisibility(View.GONE);
                    //mEmptyView.setVisibility(View.VISIBLE);

                } else {
                    categoryRecycler.setVisibility(View.VISIBLE);
                    //mEmptyView.setVisibility(View.GONE);
                }
            }
            @Override
            protected void onError (FirebaseFirestoreException e){
                Log.e("error", e.getMessage());
            }
        };
        //categoryRecycler.setHasFixedSize(true);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        categoryRecycler.setAdapter(categoryAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                categoryAdapter.deleteCategory(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(categoryRecycler);


        return view;
    }

    @Override
    public void onCategoryItemClick(DocumentSnapshot documentSnapshot, int clickedItemIndex, String categoryName) {

        Intent mIntent = new Intent(getActivity(), NewsListActivity.class);
        Toast.makeText(this.getActivity(), "Item clicked " + clickedItemIndex + "", Toast.LENGTH_SHORT).show();
        //Bundle bundle = new Bundle();
        mIntent.putExtra("category",categoryName);
        startActivity(mIntent);
    }
}