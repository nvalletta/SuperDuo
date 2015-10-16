package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.service.MyFetchService;

/**
 * Created by Nora on 10/16/2015.
 */
@TargetApi(11)
public class FootballAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent service_start = new Intent(context, MyFetchService.class);
        context.startService(service_start);

        for (int i=0; i < appWidgetIds.length; i++) {
            Intent intent = new Intent(context, FootballRemoteViewsService.class);

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.fragment_main);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.scores_list, intent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
    }
}
