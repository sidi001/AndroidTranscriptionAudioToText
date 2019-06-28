package com.example.siditoure.androidtranscription;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;

public class ExplorerReceiver extends BroadcastReceiver {
    private ExplorateurActivity mActivity = null;

    public ExplorerReceiver(ExplorateurActivity mActivity) {
        super();
        this.mActivity = mActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED))
            mActivity.setEmpty();
        else if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED))
            mActivity.updateDirectory(new File(intent.getData().toString()));
    }
}
