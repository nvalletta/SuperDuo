package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;

/**
 * Created by Nora on 10/16/2015.
 */
@TargetApi(11)
public class FootballRemoteViewsService extends android.widget.RemoteViewsService {

    //TODO: In here, load all football stuffs? http://developer.android.com/guide/topics/providers/content-provider-basics.html

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballViewFactory(this.getApplicationContext(), intent);
    }

}
