package hrishikesh.deshpande.mysocialapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Hrishikesh Deshpande on 16-Nov-17.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostAdapterViewHolder> {
    List<Posts> postList ;
    Context context ;
    public boolean isOwnWall = false ;


    public PostAdapter(Context context,List<Posts> postList){
        this.postList = postList ;
        this.context = context ;
    }

    @Override
    public PostAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false) ;
        return new PostAdapterViewHolder(itemView) ;
    }

    @Override
    public void onBindViewHolder(PostAdapterViewHolder holder, int position) {
    final Posts post = postList.get(position) ;
        final int finalPosition = position ;
        holder.postName.setText(post.getFrom());
        holder.postName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post.getEmailId().equals(AuthenticationUtilities.getEmailId())) {
                    Utilities.$si(context,UserWall.class,null);
                }else{
                    Intent intent = new Intent(context, UserWall.class);
                    intent.putExtra("dn", post.getFrom());
                    intent.putExtra("email", post.getEmailId());
                    context.startActivity(intent);
                }
            }
        });
        PrettyTime p = new PrettyTime() ;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy") ;
        try{
        holder.postTime.setText(p.format(sdf.parse(post.getDate())));
        }catch (Exception e) {

            holder.postTime.setText(post.getDate());
        }
        holder.postText.setText(post.getPost());
        if(isOwnWall) {
            holder.deleteIcon.setVisibility(View.VISIBLE);
            holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilities.confirmationDialog(context, "Are you sure you want to delete your post ?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postList.remove(finalPosition) ;
                            notifyDataSetChanged();
                            DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts") ;
                            postReference.child(post.getPostId()).removeValue() ;
                        }
                    });

                }
            });
        }else{
            holder.deleteIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public class PostAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView postName ;
        public TextView postTime ;
        public TextView postText ;
        public ImageView deleteIcon ;
        public PostAdapterViewHolder(View view) {
            super(view) ;
            postName = (TextView)view.findViewById(R.id.postName) ;
            postTime = (TextView)view.findViewById(R.id.postTime) ;
            postText = (TextView)view.findViewById(R.id.postText) ;
            deleteIcon=(ImageView) view.findViewById(R.id.deleteIconIv) ;
        }

    }
}
