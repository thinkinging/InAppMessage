package com.wps.ovs.inappmessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingClickListener;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks;
import com.google.firebase.inappmessaging.display.internal.FirebaseInAppMessagingDisplayImpl;
import com.google.firebase.inappmessaging.display.internal.injection.components.AppComponent;
import com.google.firebase.inappmessaging.display.internal.injection.components.DaggerAppComponent;
import com.google.firebase.inappmessaging.display.internal.injection.components.DaggerUniversalComponent;
import com.google.firebase.inappmessaging.display.internal.injection.components.UniversalComponent;
import com.google.firebase.inappmessaging.display.internal.injection.modules.ApplicationModule;
import com.google.firebase.inappmessaging.display.internal.injection.modules.HeadlessInAppMessagingModule;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.CampaignMetadata;
import com.google.firebase.inappmessaging.model.InAppMessage;

public class MainActivity extends AppCompatActivity {

    private final static String Tag = "InAppMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseInAppMessaging.getInstance().setAutomaticDataCollectionEnabled(true);

        MyClickListener listener = new MyClickListener();
        FirebaseInAppMessaging.getInstance().addClickListener(listener);

        setContentView(R.layout.activity_main);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        Log.v(Tag, task.getResult().getToken());
                        Log.v(Tag, task.getResult().getId());
                    } else {
                        Log.v(Tag, "error 1");
                    }
                } else {
                    Log.v(Tag, "error 2");
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(Tag, "onStart");
        FirebaseInAppMessaging.getInstance().setMessageDisplayComponent(new FirebaseInAppMessagingDisplay() {
            @Override
            public void displayMessage(InAppMessage inAppMessage, FirebaseInAppMessagingDisplayCallbacks firebaseInAppMessagingDisplayCallbacks) {
                Log.v(Tag, inAppMessage.getAction().getActionUrl() + "");
                Toast.makeText(MainActivity.this, inAppMessage.getAction().getActionUrl() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class MyClickListener implements FirebaseInAppMessagingClickListener {

        @Override
        public void messageClicked(InAppMessage inAppMessage, Action action) {
            // Determine which URL the user clicked
            String url = action.getActionUrl();
            Log.v(Tag, url);
        }

    }
}
