package example.com.videofly.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import example.com.videofly.R;
import example.com.videofly.User;

/**
 * Created by madhavchhura on 4/20/15.
 */
public class HomeFragment extends Fragment {

    User userProfile;
    Bitmap bitmap;

    public HomeFragment() {
        // Required empty public constructor
    }


    public HomeFragment(User user) {
        userProfile = user;
    }

    public HomeFragment(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView homeText = (TextView) rootView.findViewById(R.id.label);
        homeText.setText(userProfile.getUserName());
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