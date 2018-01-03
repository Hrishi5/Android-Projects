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
 * Created by sanket on 11/16/2017.
 */

public class requestAdapter extends RecyclerView.Adapter<requestAdapter.RequestViewHolder> {
    public Context context ;
    public List<requestInfo> list = requestFragment.pendingRequestList;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request") ;
    DatabaseReference friendListref = FirebaseDatabase.getInstance().getReference().child("friendList") ;
    public requestAdapter(Context context){
        this.context=context;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView =LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.requestitem, parent, false);
        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, final int position) {
        String s=null;
        if(list.get(position).getTo().equalsIgnoreCase(MainActivity.currentUserEmail)){
            s = list.get(position).getFromName();

        }else {
            s = list.get(position).getToName();
            holder.acceptImage.setVisibility(View.GONE);
        }
        holder.userName.setText(s);

        holder.acceptImage.setImageResource(R.drawable.accept);
        holder.declineImage.setImageResource(R.drawable.decline);
        holder.acceptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // requestManager.updateFriendList=true;
                ref.child(list.get(position).getRequestId()).removeValue() ;
                friendInfo friendInfo = new friendInfo();
                friendInfo.setFirstName(list.get(position).getFromName());
                friendInfo.setEmail(list.get(position).getFrom());
                friendListref.child(MainActivity.currentUserEmail.replace('.','1')).child(list.get(position).getFrom().replace('.','1')).setValue(friendInfo);

                friendInfo friendInfo1 = new friendInfo();
                friendInfo.setFirstName(MainActivity.currentUserName);
                friendInfo.setEmail(MainActivity.currentUserEmail);
                friendListref.child(list.get(position).getFrom().replace('.','1')).child(MainActivity.currentUserEmail.replace('.','1')).setValue(friendInfo);
                //pendingRequestAdapter adapter = new pendingRequestAdapter(context);
                //adapter.list.add(list.get(position).getFrom());
                //adapter.notifyDataSetChanged();
                pendingRequest.friendList.add(friendInfo);
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.declineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child(list.get(position).getRequestId()).removeValue() ;
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView acceptImage,declineImage ;
        View ownView ;

        public RequestViewHolder(View view) {
            super(view) ;
            userName = (TextView) view.findViewById(R.id.userName) ;
            acceptImage = (ImageView)view.findViewById(R.id.accept) ;
            declineImage =(ImageView)view.findViewById(R.id.decline);
            ownView = view ;
        }
    }
}
