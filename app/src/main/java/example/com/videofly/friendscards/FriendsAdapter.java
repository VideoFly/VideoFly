package example.com.videofly.friendscards;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import example.com.videofly.Friends;
import example.com.videofly.R;


/**
 * Created by madhavchhura on 4/22/15.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    ArrayList<Friends> friendsList;

    public  FriendsAdapter(ArrayList<Friends> list){
        Collections.sort(list, Friends.NameComparator);
        this.friendsList = list;
    }
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        CardView friendsView;
        TextView personName;
        //TextView personId;
        ImageView personPhoto;
        ImageView callButton;

        FriendsViewHolder(View itemView) {
            super(itemView);
            friendsView = (CardView)itemView.findViewById(R.id.friendsView);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            callButton = (ImageView) itemView.findViewById(R.id.callButton);

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("FriendsFragment", "Call Button Clicked");
                }
            });
        }
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_friends, parent, false);

        FriendsViewHolder fvh = new FriendsViewHolder(v);

        return fvh;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position){
        Log.d("Friends View Holder", "Assigning Friends values");

        holder.personName.setText(friendsList.get(position).getName());
        holder.personPhoto.setImageBitmap(friendsList.get(position).getFriendImage());

        //Picasso.with(holder.personPhoto.getContext()).load(friendsList.get(position).getFriendImage().getUrl()).into(holder.personPhoto);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
