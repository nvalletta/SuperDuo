package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.MyFetchService;

/**
 * Created by Nora on 10/16/2015.
 */
@TargetApi(16)
public class FootballAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scores_list);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent service_start = new Intent(context, MyFetchService.class);
        context.startService(service_start);

        Intent mainActivityIntent = new Intent(context, MainActivity.class);

        for (int i=0; i < appWidgetIds.length; i++) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.fragment_main);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.fragment_main_frame, pendingIntent);
            remoteViews.setEmptyView(R.id.scores_list, R.id.empty_view);

            setAdapterForRemoteViews(context, remoteViews);

            PendingIntent clickPendingIntent = TaskStackBuilder
                    .create(context)
                    .addNextIntentWithParentStack(new Intent(context, MainActivity.class))
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.scores_list, clickPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
    }

    @SuppressWarnings("deprecation")
    private void setAdapterForRemoteViews(Context context, RemoteViews remoteViews) {
        Intent footballRemoteViewsServiceIntent = new Intent(context, FootballRemoteViewsService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            remoteViews.setRemoteAdapter(
                    R.id.scores_list,
                    footballRemoteViewsServiceIntent);
        } else {
            remoteViews.setRemoteAdapter(
                    0,
                    R.id.scores_list,
                    footballRemoteViewsServiceIntent);
        }
    }

}
