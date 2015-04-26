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

import example.com.videofly.Friends;
import example.com.videofly.R;


/**
 * Created by madhavchhura on 4/22/15.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    ArrayList<Friends> friendsList;

    public  FriendsAdapter(ArrayList<Friends> list){
        this.friendsList = list;
    }
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        CardView friendsView;
        TextView personName;
        TextView personId;
        ImageView personPhoto;

        FriendsViewHolder(View itemView) {
            super(itemView);
            friendsView = (CardView)itemView.findViewById(R.id.friendsView);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personId = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
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
        holder.personId.setText(friendsList.get(position).getId());
        holder.personPhoto.setImageBitmap(friendsList.get(position).getProfilePic());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }


}
