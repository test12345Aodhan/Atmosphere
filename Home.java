package com.application.atmosphereApp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.atmosphereApp.Adapter.PostsAdapter;
import com.application.atmosphereApp.Adapter.StoryAdapter;

import com.application.atmosphereApp.Models.Posts;
import com.application.atmosphereApp.Models.TheAtmosphereStory;

import com.application.atmosphereApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    public Home(){

    }

    private RecyclerView recyclerViewPosts;
    List<Posts> postsList;
    PostsAdapter postsAdapter;
    FirebaseAuth firebaseAuth;


    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<TheAtmosphereStory> storiesList;

    private List<String> followingList;

    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

            firebaseAuth = FirebaseAuth.getInstance();

            recyclerViewPosts = view.findViewById(R.id.recycle_view_posts);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setStackFromEnd(true);
            layoutManager.setReverseLayout(true);
            postsList = new ArrayList<>();
            recyclerViewPosts.setLayoutManager(layoutManager);
            progressBar = view.findViewById(R.id.progress_bar);

            checkIfFollowing();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView_story.setLayoutManager(linearLayoutManager1);
   //     storiesList = new ArrayList<>();
       // storyAdapter = new StoryAdapter(getContext(), storiesList);
//        recyclerView_story.setAdapter(storyAdapter);
        followingList = new ArrayList<>();




        return view;
    }



    private void checkIfFollowing() {

        followingList = new ArrayList<>();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for (DataSnapshot dSnapshot : snapshot.getChildren()) {
                    followingList.add(dSnapshot.getKey());
                }

                readInStory();
                loadPosts();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void readInStory () {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Stories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long time = System.currentTimeMillis();
//                storiesList.clear();
//                storiesList.add(new TheAtmosphereStory("", 0, 0, "", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
  //              for (String id : followingList) {
    //                int count = 0;
      //              TheAtmosphereStory story = null;
        //            for (DataSnapshot snap : snapshot.child(id).getChildren()) {
          //              story = snap.getValue(TheAtmosphereStory.class);
            //            if (time > Objects.requireNonNull(story).getTimeStart() && time < story.getTimeEnd()) {
              ///              count++;
                 //       }
                   // }
                    //if (count > 0) {
                      //  storiesList.add(story);
                   // }
              //  }
              //  storyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Posts posts = snapshot1.getValue(Posts.class);

                    postsList.add(posts);

                    postsAdapter = new PostsAdapter(getActivity(), postsList);

                    recyclerViewPosts.setAdapter(postsAdapter);


                }
                postsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
