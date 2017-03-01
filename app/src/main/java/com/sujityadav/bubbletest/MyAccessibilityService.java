package com.sujityadav.bubbletest;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;



/**
 * Created by sujit yadav on 2/22/2017.
 */

public class MyAccessibilityService extends AccessibilityService {
    private final IBinder iBinder = new MyAccessibilityServiceBinder();
    private AccessibilityServiceInfo info;
    private WindowManager mWindowManager;
    private View mFloatingView;
    int i=0;
    private String date, subject, merchant;

    public class MyAccessibilityServiceBinder extends Binder {
        MyAccessibilityService getService() {
            return MyAccessibilityService.this;
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            AccessibilityNodeInfo source = event.getSource();
            if (source == null) {
                return;
            }
            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = null;
            findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.google.android.gm:id/sender_name");
            if (findAccessibilityNodeInfosByViewId.size() > 0) {
                i++;
                AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                // You can also traverse the list if required data is deep in view hierarchy.
                merchant = parent.getText().toString();
            }
            findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.google.android.gm:id/subject_and_folder_view");
            if (findAccessibilityNodeInfosByViewId.size() > 0) {
                i++;
                AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                // You can also traverse the list if required data is deep in view hierarchy.
                subject = parent.getText().toString();
            }
            findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.google.android.gm:id/send_date");
            if (findAccessibilityNodeInfosByViewId.size() > 0) {
                i++;
                AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                // You can also traverse the list if required data is deep in view hierarchy.
                date = parent.getText().toString();
                date = date.split(" ")[0];
//                Toast.makeText(this, date+"  :"+String.valueOf(i), Toast.LENGTH_SHORT).show();
//                Log.i("Required Text", date + i);
                Intent service = new Intent(MyAccessibilityService.this, FloatingViewService.class);
                service.putExtra("date", date);
                service.putExtra("subject", subject);
                service.putExtra("merchant", merchant);
                startService(service);
            }
        }

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        info= new AccessibilityServiceInfo();
        info.eventTypes =AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.packageNames = new String[] {"com.google.android.gm"};
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS; info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY; info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        info.notificationTimeout = 0;
        this.setServiceInfo(info);

    }
}
