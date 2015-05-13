package example.com.videofly;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import org.json.JSONObject;

import java.util.ArrayList;

import example.com.videofly.services.ClearNotificationService;


public class VideoCallActivity extends AppCompatActivity implements
        Session.SessionListener,
        Publisher.PublisherListener,
        Subscriber.VideoListener {

    private static final String LOGTAG = "VideoCallActivity";
    private Toolbar mToolbar;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ArrayList<Stream> mStreams;
    private Handler mHandler = new Handler();

    private WindowManager windowManager;
    private RelativeLayout videoFlyHead; //Root Layout
    private ImageButton endCallButton;
    private LinearLayout mViewGroupLayout;

    private RelativeLayout mPublisherViewContainer;
    private RelativeLayout mSubscriberViewContainer;

    // Spinning wheel for loading subscriber view
    private ProgressBar mLoadingSub;

    private boolean resumeHasRun = false;

    private boolean mIsBound = false;
    private NotificationCompat.Builder mNotifyBuilder;
    private NotificationManager mNotificationManager;
    private ServiceConnection mConnection;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mPublisherViewContainer = (RelativeLayout) findViewById(R.id.publisherview);
        mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberview);
        mLoadingSub = (ProgressBar) findViewById(R.id.loadingSpinner);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mStreams = new ArrayList<Stream>();

        Intent intent = getIntent();
        String sessionId = intent.getStringExtra("sessionId");
        String token = intent.getStringExtra("publisherToken");

        JSONObject json = null;

        if(sessionId != null && token != null){
            Log.d("VideoCallActivity", "Created using MainActivity sessionID and token ID");
            sessionConnect(sessionId, token);
        }
        else{
            Log.d("VideoCallActivity", "Created using MainActivity sessionID and token ID");
            sessionConnect(MainActivity.sessionId, MainActivity.token);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //Initialize variables
        endCallButton = new ImageButton(getApplicationContext());
        videoFlyHead = new RelativeLayout(getApplicationContext());
        mStreams = new ArrayList<Stream>();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        videoFlyHead = (RelativeLayout) LayoutInflater.from(this).
                inflate(R.layout.activity_video_fly, null, true);
        //mPublisherViewContainer = (RelativeLayout) videoHead.findViewById(R.id.publisherview);
        mSubscriberViewContainer.removeAllViews();
        mSubscriberViewContainer = (RelativeLayout) videoFlyHead.findViewById(R.id.subscriberview);
        attachSubscriberView(mSubscriber);

        mViewGroupLayout = (LinearLayout) LayoutInflater.from(this).
                inflate(R.layout.activity_video_controls, null);
        videoFlyHead.addView(mViewGroupLayout);

        endCallButton = (ImageButton) mViewGroupLayout.findViewById(R.id.button_end_call);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        videoFlyHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            //Used to make the Head movable.
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(videoFlyHead, params);
                        return true;
                }
                return false;
            }
        });
        windowManager.addView(videoFlyHead, params);

//        if (mSession != null) {
//            mSession.onPause();
//
//            if (mSubscriber != null) {
//                mSubscriberViewContainer.removeView(mSubscriber.getView());
//            }
//        }
//
//        mNotifyBuilder = new NotificationCompat.Builder(this)
//                .setContentTitle(this.getTitle())
//                .setContentText(getResources().getString(R.string.notification))
//                .setSmallIcon(R.drawable.appicon).setOngoing(true);
//
//        Intent notificationIntent = new Intent(this, VideoCallActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//
//        mNotifyBuilder.setContentIntent(intent);
//        if (mConnection == null) {
//            mConnection = new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName className, IBinder binder) {
//                    ((ClearNotificationService.ClearBinder) binder).service.startService(new Intent(VideoCallActivity.this, ClearNotificationService.class));
//                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                    mNotificationManager.notify(ClearNotificationService.NOTIFICATION_ID, mNotifyBuilder.build());
//                }
//
//                @Override
//                public void onServiceDisconnected(ComponentName className) {
//                    mConnection = null;
//                }
//
//            };
//        }
//
//        if (!mIsBound) {
//            bindService(new Intent(VideoCallActivity.this,
//                            ClearNotificationService.class), mConnection,
//                    Context.BIND_AUTO_CREATE);
//            mIsBound = true;
//            startService(notificationIntent);
//        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }

        if (!resumeHasRun) {
            resumeHasRun = true;
            return;
        } else {
            if (mSession != null) {
                mSession.onResume();
            }
        }
        mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);

        reloadInterface();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }

        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        if (isFinishing()) {
            mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
            if (mSession != null) {
                mSession.disconnect();
            }
        }
    }

    @Override
    public void onDestroy() {
        mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }

        if (mSession != null) {
            mSession.disconnect();
        }

        if (videoFlyHead != null) windowManager.removeView(videoFlyHead);

        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mSession != null) {
            mSession.disconnect();
        }

        super.onBackPressed();
    }


    //Session Methods
    private void sessionConnect(String sessionId,String token) {
        Log.d(LOGTAG, "in sessionConnect");
        if (mSession == null) {
            mSession = new Session(VideoCallActivity.this,
                    getResources().getString(R.string.opentok_api_key),
                    sessionId);
            mSession.setSessionListener(this);
            mSession.connect(token);

            Log.d(LOGTAG, "leaving sessionConnect if loop");
        }
        Log.d(LOGTAG, "leaving sessionConnect");
    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOGTAG, "Connected to the session.");
        if (mPublisher == null) {
            mPublisher = new Publisher(VideoCallActivity.this, "publisher");
            mPublisher.setPublisherListener(this);
            attachPublisherView(mPublisher);
            mSession.publish(mPublisher);
        }

        else if (mPublisher != null) {
            mSession.publish(mPublisher);
        }
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOGTAG, "Disconnected from the session.");
        if (mPublisher != null) {
            mPublisherViewContainer.removeView(mPublisher.getView());
        }

        if (mSubscriber != null) {
            mSubscriberViewContainer.removeView(mSubscriber.getView());
        }

        mPublisher = null;
        mSubscriber = null;
        mStreams.clear();
        mSession = null;
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        mStreams.add(stream);
        if (mSubscriber == null) {
            subscribeToStream(stream);
        }
        else{
            mSession.subscribe(mSubscriber);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if ((mSubscriber != null)) {
            unsubscribeFromStream(stream);
        }

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOGTAG, "Publisher exception: " + opentokError.getMessage());
    }

    //Publisher.PublisherListener
    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.i(LOGTAG, "Publisher exception: " + opentokError.getMessage());
    }


    //Subscriber.VideoListener
    @Override
    public void onVideoDataReceived(SubscriberKit subscriberKit) {
        Log.i(LOGTAG, "First frame received");

        // stop loading spinning
        mLoadingSub.setVisibility(View.GONE);
        attachSubscriberView(mSubscriber);
    }

    @Override
    public void onVideoDisabled(SubscriberKit subscriberKit, String s) {
        Log.i(LOGTAG, "Video disabled:" + s);
    }

    @Override
    public void onVideoEnabled(SubscriberKit subscriber, String reason) {
        Log.i(LOGTAG, "Video enabled:" + reason);
    }

    @Override
    public void onVideoDisableWarning(SubscriberKit subscriber) {
        Log.i(LOGTAG, "Video may be disabled soon due to network quality degradation. Add UI handling here.");
    }

    @Override
    public void onVideoDisableWarningLifted(SubscriberKit subscriber) {
        Log.i(LOGTAG, "Video may no longer be disabled as stream quality improved. Add UI handling here.");
    }


    private void subscribeToStream(Stream stream) {
        Log.d(LOGTAG, "subscribing to stream");
        mSubscriber = new Subscriber(this, stream);
        mSubscriber.setVideoListener(this);
        mSubscriber.setSubscribeToAudio(true);
        mSubscriber.setSubscribeToVideo(true);
        mSession.subscribe(mSubscriber);

        if (mSubscriber.getSubscribeToVideo()) {
            // start loading spinning
            mLoadingSub.setVisibility(View.VISIBLE);
        }

        Log.d(LOGTAG, "leaving subscribing to stream");

    }

    private void unsubscribeFromStream(Stream stream) {
        mStreams.remove(stream);
        if (mSubscriber.getStream().getStreamId().equals(stream.getStreamId())) {
            mSubscriberViewContainer.removeView(mSubscriber.getView());
            mSubscriber = null;
            if (!mStreams.isEmpty()) {
                subscribeToStream(mStreams.get(0));
            }
        }
    }
    private void attachSubscriberView(Subscriber subscriber) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels, getResources()
                .getDisplayMetrics().heightPixels);
        mSubscriberViewContainer.removeView(mSubscriber.getView());
        mSubscriberViewContainer.addView(mSubscriber.getView(), layoutParams);
        subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
    }

    private void attachPublisherView(Publisher publisher) {
        mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                320, 240);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                RelativeLayout.TRUE);
        layoutParams.bottomMargin = dpToPx(8);
        layoutParams.rightMargin = dpToPx(8);
        mPublisherViewContainer.addView(mPublisher.getView(), layoutParams);
    }

    public void reloadInterface() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSubscriber != null) {
                    attachSubscriberView(mSubscriber);
                }
            }
        }, 500);
    }

    public void endCallButtonClicked(View view){
        mStreams.clear();
        mSession.disconnect();
        this.mSubscriber = null;
        stopService(new Intent(getApplicationContext(), VideoCallActivity.class));
    }

    /**
     * Converts dp to real pixels, according to the screen density.
     *
     * @param dp A number of density-independent pixels.
     * @return The equivalent number of real pixels.
     */
    private int dpToPx(int dp) {
        double screenDensity = this.getResources().getDisplayMetrics().density;
        return (int) (screenDensity * (double) dp);
    }

}
