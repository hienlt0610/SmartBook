package smartbook.hutech.edu.smartbook.ui.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import butterknife.BindView;
import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.adapter.TabPageAdapter;
import smartbook.hutech.edu.smartbook.common.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private TabPageAdapter mTabPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * Init activity
     */
    private void init() {
        initTabLayoutAndPager();
    }

    private void initTabLayoutAndPager() {
        Toast.makeText(this, "sdsadas", Toast.LENGTH_SHORT).show();
        mTabPageAdapter = new TabPageAdapter(getSupportFragmentManager());
        mTabPageAdapter.addPage("test", new HomeFragment());
        mTabPageAdapter.addPage("test", new HomeFragment());
        mTabPageAdapter.addPage("test", new HomeFragment());
        mViewPager.setAdapter(mTabPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Notification!") // title for notification
                .setContentText("Hello word") // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

}
