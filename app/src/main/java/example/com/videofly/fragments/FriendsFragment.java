package example.com.videofly.fragments;

import android.annotation.SuppressLint;
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
import example.com.videofly.listeners.ClickListener;
import example.com.videofly.listeners.RecyclerTouchListener;

/**
 * Created by madhavchhura on 4/21/15.
 */
public class FriendsFragment extends Fragment {
    private static ArrayList<Friends> data = null;
    private FriendsAdapter adapter;
    private CallFriendListener callListener;
    RecyclerView recyclerView;

    public FriendsFragment(){
    }

    @SuppressLint("ValidFragment")
    public FriendsFragment(ArrayList<Friends> friends){
        this.data = friends;
    }

    public void setCallFriendListener(CallFriendListener listener) {
        this.callListener = listener;
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
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(position <= data.size()){
                    callListener.onCallFriend(data.get(position).getId());
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface CallFriendListener {
        void onCallFriend(String friendName);
    }


}
