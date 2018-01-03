package hrishikesh.deshpande.mysocialapp;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by sanket on 11/17/2017.
 */

public class friendListAdapter extends RecyclerView.Adapter<friendListAdapter.friendListViewHolder> {
    public List<friendInfo> friendlist = friendsFragment.friendListofUser;
    public Context context ;
    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    DatabaseReference mrrot = database.getReference("request") ;
    private FirebaseAuth mAuth1 ;
    private requestInfo requestInfo;

    public friendListAdapter(Context context){
        userInfo userInfo = new userInfo();
        this.context=context;
        requestInfo = new requestInfo();

    }

    @Override
    public friendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.friendlistitem, parent, false);
        return new friendListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(friendListViewHolder holder, final int position) {
        String s = friendlist.get(position).getFirstName();
        holder.friendName.setText(s);
        holder.addfriend.setImageResource(R.drawable.add_new_friend);

        holder.addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth1 = FirebaseAuth.getInstance() ;
                DatabaseReference uniqueRef = mrrot.push() ;
                requestInfo.setTo(friendlist.get(position).getEmail());
                requestInfo.setToName(friendlist.get(position).getFirstName());
                requestInfo.setFrom(MainActivity.currentUserEmail);
                requestInfo.setFromName(MainActivity.currentUserName);
                requestInfo.setRequestId(uniqueRef.getKey());
                uniqueRef.setValue(requestInfo) ;
                friendlist.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendlist.size();
    }

    public class friendListViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        ImageView addfriend;
        View ownView ;

        public friendListViewHolder(View view) {
            super(view) ;
            friendName = (TextView) view.findViewById(R.id.friendName) ;
            addfriend = (ImageView)view.findViewById(R.id.addfriend) ;
            ownView = view ;
        }
    }
}

