package example.com.videofly.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import example.com.videofly.R;
import example.com.videofly.User;

/**
 * Created by madhavchhura on 4/20/15.
 */
public class HomeFragment extends Fragment {

    User userProfile;

    public HomeFragment() {
        // Required empty public constructor
    }


    public HomeFragment(User user) {
        userProfile = user;
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
        ImageView imageView = (ImageView) rootView.findViewById(R.id.pic);

        //For Test Purposes.
        imageView.setImageBitmap(userProfile.getUserProfilePicture());


        Log.d("In OnCreateView", ""+userProfile.getUserName()+"");
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