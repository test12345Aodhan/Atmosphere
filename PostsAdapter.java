package com.application.atmosphereApp.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.usage.NetworkStats;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.application.atmosphereApp.FollowersActivity;
import com.application.atmosphereApp.Fragments.PostsDetails;
import com.application.atmosphereApp.Fragments.Profile;
import com.application.atmosphereApp.Models.Posts;
import com.application.atmosphereApp.Models.Venues;
import com.application.atmosphereApp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    List<Venues> venuesList;
    Context context;
    List<Posts> postsList;

    public PostsAdapter(Context context, List<Posts> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

   public PostsAdapter(){

   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater to reference the post items xml
        View v = LayoutInflater.from(context).inflate(R.layout.posts_items,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String postID = postsList.get(position).getPostID();
        String postedBy = postsList.get(position).getPostedBy();
        String postDescription = postsList.get(position).getPostDescription();
        String postImage = postsList.get(position).getPostImage();
//        String ImageURL = venuesList.get(position).getImageURL();

        holder.description.setText(postDescription);
        holder.postedBy.setText(postedBy);


//        if (postImage.equals("default")) {
  //          holder.post_image.setVisibility(View.GONE);
    //    } else {

            try {

            } catch (Exception e) {

            }

            //set up data
            try {
            //    Picasso.get().load(ImageURL).placeholder(R.mipmap.ic_launcher).into(holder.image_profile);
            } catch (Exception e) {

            }
            try {
                Glide.with(context).load(postImage).into(holder.post_image);
            } catch (Exception e) {

            }


        }


    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{
        public CircleImageView image_profile;
        public ImageView post_image, like, save, more;
        public TextView username, likes, postedBy, description;
        public ViewHolder(@NonNull View view){
            super(view);
            image_profile = itemView.findViewById(R.id.profile_img_posts);
            username = itemView.findViewById(R.id.username_posts);
            post_image = itemView.findViewById(R.id.post_img_posts);
            like = itemView.findViewById(R.id.like_post);
            save = itemView.findViewById(R.id.save_img);
            likes = itemView.findViewById(R.id.likes_amount);
            postedBy = itemView.findViewById(R.id.postedBy);
            description = itemView.findViewById(R.id.post_description);
            more = itemView.findViewById(R.id.options);
        }
    }
}