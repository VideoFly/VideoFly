package example.com.videofly;

import android.view.View;

/**
 * Created by madhavchhura on 5/11/15.
 */

public interface ClickListener {
    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}