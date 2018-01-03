package hrishikesh.deshpande.mysocialapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link friendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link friendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class friendsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    friendListAdapter adapter ;
    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    public static List<friendInfo> friendListofUser=new ArrayList<friendInfo>();

    DatabaseReference mrrotUser = database.getReference("user") ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View mainView ;
    private OnFragmentInteractionListener mListener;
    List<userInfo> userList ;

    public friendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment friendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static friendsFragment newInstance(String param1, String param2) {
        friendsFragment fragment = new friendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_friends, container, false);
        mainView = view ;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference() ;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot == null) {
                    // Toast.makeText(requestManager.this, "No Pending Request!", Toast.LENGTH_SHORT).show();
                    //return;
                }else {
                    List<friendInfo>friendListofUser1=new ArrayList<friendInfo>();
                    for (DataSnapshot child : dataSnapshot.child("user").getChildren()) {
                        //Log.d("json", "check " + child.getValue(requestInfo.class));
                        userInfo fr = child.getValue(userInfo.class);
                        boolean flag =false;
                        for(int i=0;i<pendingRequest.friendList.size();i++){
                            if(fr.getEmail().trim().equalsIgnoreCase(pendingRequest.friendList.get(i).getEmail().trim())){
                                flag=true;
                                break;
                            }
                        }
                        if(!flag){
                            if(!(fr.getEmail().trim().equalsIgnoreCase(MainActivity.currentUserEmail.trim()))) {
                                friendInfo friendInfo = new friendInfo();
                                friendInfo.setEmail(fr.getEmail());
                                friendInfo.setFirstName(fr.getFirstName());
                                friendListofUser1.add(friendInfo);
                            }
                        }
                    }
                    List<String> temp = new ArrayList<String>() ;
                    for(DataSnapshot dchild : dataSnapshot.child("request").getChildren()) {

                        requestInfo r = dchild.getValue(requestInfo.class) ;
                        if(r.getTo().trim().equalsIgnoreCase(MainActivity.currentUserEmail.trim())) {
                            temp.add(r.getFrom()) ;

                        }else if(r.getFrom().trim().equalsIgnoreCase(MainActivity.currentUserEmail.trim())) {
                            temp.add(r.getTo()) ;
                        }
                    }
                    List<friendInfo> temp2 = new ArrayList<friendInfo>() ;
                    for(int i=0 ; i<friendListofUser1.size() ; i++ ) {
                        for (String s : temp) {
                            if (s.equalsIgnoreCase(friendListofUser1.get(i).getEmail())) {
                                temp2.add(friendListofUser1.get(i));
                            }
                        }
                    }

                    for(friendInfo frr : temp2) {
                        friendListofUser1.remove(frr) ;
                    }

                    friendListofUser=friendListofUser1;
                    Utilities.$l("json","addnewf" + friendListofUser);
                    initRecyclerView(mainView,friendListofUser);
                    //adapter.friendlist=friendListofUser;
                    //adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;
    }

    public void initRecyclerView(View view , List<friendInfo> fl) {
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.friendlistRV) ;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) ;
        recyclerView.setLayoutManager(llm);
        adapter = new friendListAdapter(getActivity()) ;
        adapter.friendlist = fl ;
        recyclerView.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
