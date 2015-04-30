package example.com.videofly.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.com.videofly.Friends;
import example.com.videofly.R;
import example.com.videofly.friendscards.FriendsAdapter;

/**
 * Created by madhavchhura on 4/21/15.
 */
public class FriendsFragment extends Fragment {
    private static ArrayList<Friends> data = null;
    private FriendsAdapter adapter;
    RecyclerView recyclerView;

    public FriendsFragment(){

    }

    public FriendsFragment(ArrayList<Friends> friends){
        this.data = friends;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_layout, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        adapter = new FriendsAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
