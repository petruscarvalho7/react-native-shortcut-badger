package com.botbie.badge;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.content.Context;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by dry on 9/23/16.
 */

public class BadgeNumberModule extends ReactContextBaseJavaModule {

    private ResolveInfo resolveInfo;
    private NotificationManager mNotificationManager;
    private static int mNotificationId = 1001;

    public BadgeNumberModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "BadgeNumberAndroid";
    }

    @ReactMethod
    public void setNumber(int number) {
        if (number > 0) {
            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                mNotificationManager = (NotificationManager) this.getReactApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                if (resolveInfo == null) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    resolveInfo = this.getReactApplicationContext().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                }

                mNotificationManager.cancel(mNotificationId);
                // mNotificationId++;

                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("1001", "NAME", importance);
                mNotificationManager.createNotificationChannel(channel);

                Notification.Builder builder = new Notification.Builder(this.getReactApplicationContext(), "1001")
                        .setContentTitle("Sippy")
                        .setContentText("You still have some news.")
                        .setNumber(number)
                        .setPriority(5)
                        .setSmallIcon(resolveInfo.getIconResource());
                Notification notification = builder.build();
                ShortcutBadger.applyNotification(this.getReactApplicationContext(), notification, number);
                ShortcutBadger.applyCount(this.getReactApplicationContext(), number);
                mNotificationManager.notify(mNotificationId, notification);
            } else {
                ShortcutBadger.applyCount(this.getReactApplicationContext(), number);
            }
        }
    }
}
