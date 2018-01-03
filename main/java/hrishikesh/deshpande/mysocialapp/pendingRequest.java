package hrishikesh.deshpande.mysocialapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * {@link pendingRequest.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class pendingRequest extends Fragment {
    pendingRequestAdapter adapter ;
    DatabaseReference friendListref = FirebaseDatabase.getInstance().getReference().child("friendList") ;
    public static List<friendInfo>friendList=new ArrayList<friendInfo>();
    private OnFragmentInteractionListener mListener;
    View mainView ;
    public pendingRequest() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pending_request, container, false);
        mainView = view ;
        friendListref.child(MainActivity.currentUserEmail.replace('.', '1')).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    //    Toast.makeText(requestManager.this, "No Pending Request!", Toast.LENGTH_SHORT).show();
                    //  return;
                } else {
                    List<friendInfo>friendList1=new ArrayList<friendInfo>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d("json","check " + child.getValue(requestInfo.class)) ;
                        Utilities.$l("json","frpa"+child.getValue(friendInfo.class));
                        friendInfo fr = child.getValue(friendInfo.class);
                        if(fr!=null && !fr.getEmail().trim().equalsIgnoreCase(MainActivity.currentUserEmail.trim()))
                        {
                            friendList1.add(fr);
                        }
                    }
                    friendList=friendList1;
                    initRecyclerView(mainView,friendList);
                    //adapter.list=friendList;
                    //adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    public void initRecyclerView(View view,List<friendInfo> fl) {
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.PendingrequestlistRV) ;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) ;
        recyclerView.setLayoutManager(llm);
        adapter = new pendingRequestAdapter(getActivity()) ;
        adapter.list = fl ;
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
