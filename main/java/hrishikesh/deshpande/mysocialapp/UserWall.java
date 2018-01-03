package hrishikesh.deshpande.mysocialapp;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

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

public class UserWall extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    DatabaseReference postsRef = database.getReference("posts") ;
    List<Posts> postList ;
    String userId ;
    String displayName ;
    boolean isOwnWall ;
    private Context context ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wall);
        context = this ;
        displayName = Utilities.getStringIntentData(getIntent(),"dn") ;
        userId = Utilities.getStringIntentData(getIntent(),"email") ;
        if(displayName == null && userId == null) {
            displayName = AuthenticationUtilities.getDisplayName() ;
            userId = AuthenticationUtilities.getEmailId() ;
            this.isOwnWall = true ;
        }else{
            this.isOwnWall = false ;
        }
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
        Toolbar toolbar = (Toolbar)findViewById(R.id.actionBarUW) ;
        if(this.isOwnWall) {

            TextView tv = (TextView) toolbar.findViewById(R.id.unameLabelUW) ;
            tv.setText(displayName);
            ImageView iv = (ImageView)toolbar.findViewById(R.id.editProfileUW) ;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilities.$si(context,EditProfile.class,null);
                }
            });
            iv = (ImageView)toolbar.findViewById(R.id.friendsUW) ;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilities.$si(context,requestManager.class,null);
                }
            });
            tv = (TextView)findViewById(R.id.postTitleUW) ;
            tv.setText("My Posts");
        }else{
        toolbar.setVisibility(View.GONE);
            toolbar = (Toolbar)findViewById(R.id.actionBarFW) ;
            toolbar.setVisibility(View.VISIBLE);
            TextView tv = (TextView)toolbar.findViewById(R.id.unameLabelFW) ;
            tv.setText(displayName);
            ImageView iv = (ImageView)toolbar.findViewById(R.id.homeFW) ;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilities.$si(context,UserHome.class,null);
                }
            });

        }
        setValueChangedListener(postsRef);
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

    public void setValueChangedListener(DatabaseReference postsReference) {
        postsReference.orderByChild("date")
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            postList = new ArrayList<Posts>() ;
                Posts post ;
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    post = d.getValue(Posts.class) ;
                    Utilities.$l("json",post);
                    if(post.getEmailId().trim().equals(userId)) {
                        //postList.add(post) ;
                        Calendar c = Calendar.getInstance() ;
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy") ;
                        Date date = null ;
                        try {
                            date = sdf.parse(post.getDate());
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE,-2) ;
                        if(date.compareTo(c.getTime()) >= 0) {
                            postList.add(post);
                        }else{
                            Log.d("json","date wrong comparison") ;
                        }
                    }
                }
                Utilities.$l("json",userId);
                Utilities.$l("json",displayName);
                postList = reverseList(postList) ;
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;

    }

    public List<Posts> reverseList(List<Posts> list) {
        List<Posts> reversedList = new ArrayList<Posts>() ;
        for(int i=list.size()-1 ; i>=0 ; i--) {
            reversedList.add(list.get(i)) ;
        }
        return reversedList ;
    }

    public void initRecyclerView() {
        RecyclerView rv = (RecyclerView)findViewById(R.id.userWallRv) ;
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this) ;
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        PostAdapter adapter = new PostAdapter(this,postList) ;
        adapter.isOwnWall = this.isOwnWall ;
        rv.setAdapter(adapter);
    }

}
