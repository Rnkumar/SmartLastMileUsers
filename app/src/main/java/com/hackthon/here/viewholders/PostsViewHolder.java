package com.hackthon.here.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackthon.here.R;
import com.hackthon.here.models.PostsModel;
import com.squareup.picasso.Picasso;

public class PostsViewHolder extends RecyclerView.ViewHolder {

    private TextView locationText, comment, username, upvotes, downvotes,dateTime;
    private ImageView postImage;

    public PostsViewHolder(@NonNull View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View itemView) {
        postImage = itemView.findViewById(R.id.posts_image);
        locationText = itemView.findViewById(R.id.posts_user_location_text);
        comment = itemView.findViewById(R.id.posts_user_comment);
        username = itemView.findViewById(R.id.posts_user_name);
        upvotes = itemView.findViewById(R.id.posts_users_upvote);
        downvotes = itemView.findViewById(R.id.posts_users_downvote);
        dateTime = itemView.findViewById(R.id.posts_date_time);
    }

    public void setData(PostsModel postsModel){
        Picasso.get().load(postsModel.getImageUrl()).into(postImage);
        locationText.setText(postsModel.getLocationText());
        comment.setText(postsModel.getComment());
        username.setText(postsModel.getUsername());
        upvotes.setText(postsModel.getUpvotes());
        downvotes.setText(postsModel.getDownvotes());
        dateTime.setText(postsModel.getDatetime());
    }

}
