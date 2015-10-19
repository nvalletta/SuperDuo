package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.service.MyFetchService;
import static barqsoft.footballscores.ScoresAdapter.*;

/**
 * Created by Nora on 10/16/2015.
 */
@TargetApi(11)
public class FootballViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;

    public FootballViewFactory(Context context, Cursor cursor){
        this.mContext = context;
        mCursor = cursor;
    }

    @Override
    public void onCreate() {
        Intent service_start = new Intent(mContext, MyFetchService.class);
        mContext.startService(service_start);
    }

    @Override
    public void onDataSetChanged() {
        if (null != mCursor) {
            mCursor.close();
        }

        final long idToken = Binder.clearCallingIdentity();

        mCursor = FootballRemoteViewsService.getCursorForCurrentFootballData(
                mContext.getContentResolver()
        );

        Binder.restoreCallingIdentity(idToken);
    }

    @Override
    public void onDestroy() {
        if (null != mCursor) {
            mCursor.close();
            mCursor = null;
        }
    }

    @Override
    public int getCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (null == mCursor || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews remoteViewsRow = new RemoteViews(
                mContext.getPackageName(),
                R.layout.scores_list_item
        );

        populateRemoteViewsRowData(remoteViewsRow);
        Intent mainActivityIntent = new Intent(mContext, MainActivity.class);
        remoteViewsRow.setOnClickFillInIntent(R.id.scores_item_container, mainActivityIntent);

        return remoteViewsRow;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.scores_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void populateRemoteViewsRowData(RemoteViews remoteViewsRow) {
        String homeGoals = Utilities.getScores(
                mCursor.getInt(COL_HOME_GOALS),
                mCursor.getInt(COL_AWAY_GOALS)
        );

        String homeName = mCursor.getString(COL_HOME);
        String awayName = mCursor.getString(COL_AWAY);
        String date = mCursor.getString(COL_DATE);

        int homeTeamCrestId = Utilities.getTeamCrestByTeamName(mCursor.getString(COL_HOME));
        int awayTeamCrestId = Utilities.getTeamCrestByTeamName(mCursor.getString(COL_AWAY));

        remoteViewsRow.setTextViewText(R.id.score_textview, homeGoals);
        remoteViewsRow.setTextViewText(R.id.home_name, homeName);
        remoteViewsRow.setTextViewText(R.id.away_name, awayName);
        remoteViewsRow.setTextViewText(R.id.data_textview, date);
        remoteViewsRow.setImageViewResource(R.id.home_crest, homeTeamCrestId);
        remoteViewsRow.setImageViewResource(R.id.away_crest, awayTeamCrestId);
    }

}
