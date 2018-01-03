package hrishikesh.deshpande.mysocialapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserHome extends AppCompatActivity {
    FirebaseDatabase firebase = FirebaseDatabase.getInstance() ;
    DatabaseReference mainReference = firebase.getReference() ;
    DatabaseReference postsReference = mainReference.child("posts") ;
    DatabaseReference friendsReference = mainReference.child("friends") ;
    List<Posts> userHomePosts ;
    String displayName ;
    String userId ;
    private Context context ;
    List<friendInfo> friendList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        context = this ;
        displayName = AuthenticationUtilities.getDisplayName() ;
        userId = AuthenticationUtilities.getEmailId() ;
        if(userId == null) {
            Utilities.$t(context,"Authentication Error!");
            return ;
        }
        Utilities.$l("json",displayName);
        Utilities.$l("json",userId);
        Toolbar t = (Toolbar) findViewById(R.id.tToolbar);
        ActionMenuView amvMenu = (ActionMenuView) t.findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        setSupportActionBar(t);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar)findViewById(R.id.actionBarUH);
        TextView unameTB = (TextView)toolbar.findViewById(R.id.unameLabelUH) ;
        unameTB.setText(displayName);
        ImageView friendsIv = (ImageView)toolbar.findViewById(R.id.friendsUH) ;
        setFriendsIconTouchListener(friendsIv) ;
        setOnUsernameTouchListener(unameTB) ;
        //setValueChangedLIstener(postsReference);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference() ;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsmain : dataSnapshot.getChildren()) {
                    if(dsmain.getKey().equalsIgnoreCase("friendList")) {

                        friendList = new ArrayList<friendInfo>() ;
                        for(DataSnapshot ds : dsmain.child(userId.replace('.','1')).getChildren()) {
                            friendList.add(ds.getValue(friendInfo.class)) ;

                        }

                    }else if(dsmain.getKey().equalsIgnoreCase("posts")) {

                        userHomePosts = new ArrayList<Posts>() ;
                        for(DataSnapshot d : dsmain.getChildren()) {

                            Posts posts = d.getValue(Posts.class) ;
                            Calendar c = Calendar.getInstance() ;
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy") ;
                            Date date = null ;
                            try {
                                date = sdf.parse(posts.getDate());
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            c.add(Calendar.DATE,-2) ;
                            if(date.compareTo(c.getTime()) >= 0) {
                                userHomePosts.add(posts);
                            }else{
                                Log.d("json","date wrong comparison") ;
                            }
                        }
                        userHomePosts = reverseList(userHomePosts) ;
                        userHomePosts = filterList(userHomePosts,friendList) ;
                        initRecyclerView();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;
        
    }

    public List<Posts> filterList(List<Posts> userHomeList,List<friendInfo> friendList) {
        if(friendList == null) {
            friendList = new ArrayList<friendInfo>() ;
        }
        List<Posts> postList = new ArrayList<Posts>() ;
        if(userHomeList == null) {
            userHomeList = new ArrayList<Posts>() ;
        }
        for(Posts p:userHomeList) {
            if(userId != null && userId.equals(p.getEmailId())) {
                postList.add(p);
            }
        }
        for(Posts p : userHomeList) {
            for(friendInfo f : friendList) {
                if(p.getEmailId().equals(f.getEmail())) {
                    postList.add(p) ;
                }
            }
        }
        return postList ;
    }

    public void setOnUsernameTouchListener(TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHome.this,UserWall.class) ;
                startActivity(intent);
            }
        });
    }

    public void setFriendsIconTouchListener(ImageView iv) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHome.this,requestManager.class) ;
                startActivity(intent);
            }
        });
    }

    public void setValueChangedLIstener(DatabaseReference postReference) {
        postReference.orderByChild("date")
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("json","On value change " + String.valueOf(dataSnapshot)) ;
                userHomePosts = new ArrayList<Posts>() ;
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    //Log.d("json","Creating list " + d.getValue()) ;
                    Posts posts = d.getValue(Posts.class) ;
                    Calendar c = Calendar.getInstance() ;
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy") ;
                    Date date = null ;
                    try {
                        date = sdf.parse(posts.getDate());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DATE,-2) ;
                    if(date.compareTo(c.getTime()) >= 0) {
                        userHomePosts.add(posts);
                    }else{
                        Log.d("json","date wrong comparison") ;
                    }
                }
                userHomePosts = reverseList(userHomePosts) ;
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            /*case R.id.appLogout:
                Utilities.$t(context,"Logged Out!");
                AuthenticationUtilities.signout(context);
                Utilities.$si(context,MainActivity.class,null) ;
                return true;*/
            case R.id.logout:
                Utilities.$t(context,"Logged Out!");
                AuthenticationUtilities.signout(context);
                Utilities.$si(context,MainActivity.class,null) ;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public List<Posts> reverseList(List<Posts> list) {
        List<Posts> reversedList = new ArrayList<Posts>() ;
        for(int i=list.size()-1 ; i>=0 ; i--) {
            reversedList.add(list.get(i)) ;
        }
        return reversedList ;
    }


    public void initRecyclerView() {
        boolean isOwnWall = false ;
        RecyclerView rv = (RecyclerView)findViewById(R.id.userHomeRv) ;
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this) ;
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        PostAdapter adapter = new PostAdapter(this,userHomePosts) ;
        adapter.isOwnWall = isOwnWall ;
        rv.setAdapter(adapter);
    }

    public void onClickOfsendPost(View view) {
        EditText post = (EditText)findViewById(R.id.userHomePostEt) ;
        String postStr = post.getText().toString() ;
        if(postStr == null || "".equalsIgnoreCase(postStr.trim())) {
            Toast.makeText(this, "Cannot share empty posts!", Toast.LENGTH_SHORT).show();
            return ;
        }
        Posts newPost = new Posts() ;
        newPost.setPost(postStr);
        newPost.setFrom(displayName);
        newPost.setEmailId(userId);
        Calendar calendar = Calendar.getInstance() ;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy") ;
        newPost.setDate(sdf.format(calendar.getTime()));
        DatabaseReference uniqueRef = postsReference.push() ;
        newPost.setPostId(uniqueRef.getKey());
        uniqueRef.setValue(newPost) ;
        Toast.makeText(this, "Posted!", Toast.LENGTH_SHORT).show();
        post.setText("");
    }
}
