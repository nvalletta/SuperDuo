package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;

/**
 * Created by Nora on 10/16/2015.
 */
@TargetApi(11)
public class FootballRemoteViewsService extends android.widget.RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballViewFactory(
                this.getApplicationContext(),
                getCursorForCurrentFootballData(this.getContentResolver())
        );
    }

    public static Cursor getCursorForCurrentFootballData(ContentResolver contentResolver) {
        Date fragmentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String[] widgetDate = new String[1];
        widgetDate[0] = formatter.format(fragmentDate);
        Cursor cursor = contentResolver.query(
                DatabaseContract.scores_table.buildScoreWithDate(),
                null,
                null,
                widgetDate,
                null
        );
        return cursor;
    }

}
