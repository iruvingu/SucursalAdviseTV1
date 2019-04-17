package com.example.sucursaladvisetv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.OpentokError;
import android.support.annotation.NonNull;
import android.Manifest;
import android.widget.FrameLayout;

public class VideoChatActivity extends AppCompatActivity implements  Session.SessionListener, PublisherKit.PublisherListener {

    private static String API_KEY = "46310892";
    private static String SESSION_ID = "1_MX40NjMxMDg5Mn5-MTU1NTQzOTAxMTI2MX5nRXVVQVdoTnFQYVdJaTlBaE1udXV2VEJ-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjMxMDg5MiZzaWc9NGI3MmE3MTgzOWNhMTI5ODdkMzM2Y2Y0MjgxMzM1NTM4NDE3MTNiMzpzZXNzaW9uX2lkPTFfTVg0ME5qTXhNRGc1TW41LU1UVTFOVFF6T1RBeE1USTJNWDVuUlhWVlFWZG9UbkZRWVZkSmFUbEJhRTF1ZFhWMlZFSi1mZyZjcmVhdGVfdGltZT0xNTU1NDM5MzAzJm5vbmNlPTAuMDY4NDUyMDAyNTQwMjA5Nzgmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU1NjA0NDEwMiZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static final String LOG_TAG = VideoChatActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session session;
    private FrameLayout PublisherContainer;
    private FrameLayout SubscriberContainer;
    private Publisher publisher;
    private Subscriber subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);

        PublisherContainer = (FrameLayout) findViewById(R.id.publisher_container);
        SubscriberContainer = (FrameLayout) findViewById(R.id.subscriber_container);

        //Se inicia la sesion si tiene los permisos
        session = new Session.Builder(this, API_KEY, SESSION_ID).build();
        session.setSessionListener(this);
        session.connect(TOKEN);
    }

    @Override
    public void onConnected(Session session) {
        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        PublisherContainer.addView(publisher.getView());
        session.publish(publisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (subscriber == null){
            subscriber = new Subscriber.Builder(this, stream).build();
            session.subscribe(subscriber);
            SubscriberContainer.addView(subscriber.getView());
        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if (subscriber != null){
            subscriber = null;
            SubscriberContainer.removeAllViews();
        }

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
