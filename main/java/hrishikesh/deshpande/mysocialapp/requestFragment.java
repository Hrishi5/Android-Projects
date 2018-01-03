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
 * {@link requestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class requestFragment extends Fragment {
    requestAdapter adapter ;
    public static List<requestInfo>pendingRequestList=new ArrayList<requestInfo>();
    private OnFragmentInteractionListener mListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    DatabaseReference mrrot = database.getReference("request") ;
    View mainView ;
    public requestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        mainView =view ;
        mrrot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot == null) {
                    // Toast.makeText(requestManager.this, "No Pending Request!", Toast.LENGTH_SHORT).show();
                    //return;
                }else {
                    List<requestInfo> pendingRequestList1=new ArrayList<requestInfo>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d("json", "pending " + child.getValue(requestInfo.class));
                        requestInfo fr = child.getValue(requestInfo.class);
                        if (MainActivity.currentUserEmail.equalsIgnoreCase(fr.getTo()) || MainActivity.currentUserEmail.equalsIgnoreCase(fr.getFrom())) {
                            pendingRequestList1.add(fr);
                        }
                    }
                    pendingRequestList=pendingRequestList1;
                    initRecyclerView(mainView,pendingRequestList);
                    //adapter.list=pendingRequestList;
                    //adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;

    }

    public void initRecyclerView(View view,List<requestInfo> list) {
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.requestlistRV) ;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) ;
        recyclerView.setLayoutManager(llm);
        adapter = new requestAdapter(getActivity()) ;
        adapter.list = list ;
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
