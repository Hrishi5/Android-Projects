package hrishikesh.deshpande.mysocialapp;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by sanket on 11/17/2017.
 */

public class pendingRequestAdapter extends RecyclerView.Adapter<pendingRequestAdapter.pendingRequestViewHolder> {
    public Context context ;
    public List<friendInfo> list = pendingRequest.friendList;

    public List<friendInfo> getList() {
        return list;
    }

    public void setList(List<friendInfo> list) {
        this.list = list;
    }

    public pendingRequestAdapter(Context context){
        this.context=context;
    }


    @Override
    public pendingRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.requestpendinglist, parent, false);
        return new pendingRequestAdapter.pendingRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(pendingRequestViewHolder holder, final int position) {
        String s = list.get(position).getFirstName();
        holder.userName.setText(s);

        holder.removeFriendicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference friendListref = FirebaseDatabase.getInstance().getReference().child("friendList") ;
                friendListref.child(MainActivity.currentUserEmail.replace('.', '1')).child(list.get(position).getEmail().replace('.', '1')).removeValue();
                friendListref.child(list.get(position).getEmail().replace('.', '1')).child(MainActivity.currentUserEmail.replace('.', '1')).removeValue();
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class pendingRequestViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView removeFriendicon;
        View ownView ;

        public pendingRequestViewHolder(View view) {
            super(view) ;
            userName = (TextView) view.findViewById(R.id.friendName1) ;

            removeFriendicon = (ImageView)view.findViewById(R.id.removeFriendicon) ;
            ownView = view ;
        }
    }
}
