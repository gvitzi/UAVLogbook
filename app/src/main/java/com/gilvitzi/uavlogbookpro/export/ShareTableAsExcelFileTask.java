package com.gilvitzi.uavlogbookpro.export;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.gilvitzi.uavlogbookpro.R;
import com.gilvitzi.uavlogbookpro.database.LogbookDataSource;

import java.io.File;

/**
 * Created by Gil on 28/01/2016.
 */
public class ShareTableAsExcelFileTask {
    private static final String MIME_APPLICATION_XSL = "application/xls";
    private static final String FILE_EXTENTION_XLS = ".xls";
    private static final String FILE_NAME_PREFIX = "UAV Logbook - ";
    private static final String FILE_PROVIDER_DOMAIN = "com.gilvitzi.fileprovider";
    private static final String TEMP_CACHE_FOLDER = "excel_files";
    private static final String FILE_PATH_PREFIX = "file://";

    private ExportTableToExcelTask mExportTask;
    private Context mContext;
    private Activity mActivity;
    private String mFileName;
    private String mTableTitle;

    public ShareTableAsExcelFileTask(Activity activity, LogbookDataSource datasource, String query, String tableTitle) {
        this.mActivity = activity;
        this.mContext = activity;
        this.mTableTitle = tableTitle;
        this.mFileName = FILE_NAME_PREFIX + tableTitle;
        initExportTask(activity, datasource, query);
    }

    private void initExportTask(Activity activity, LogbookDataSource datasource, String query) {
        File dir = mContext.getCacheDir();
        final String filePath = dir.getPath() + "/" + mFileName + FILE_EXTENTION_XLS;
        mExportTask = new ExportTableToExcelTask(activity, datasource, mFileName, dir.getPath(), query);

        //When Finished:
        mExportTask.addListnener(new ExportTableToExcelTask.Listener() {
            @Override
            public void onTaskCompleted() {
                showShareFileDialog(filePath);
            }
        });
    }

    public void execute() {
        mExportTask.execute();
    }

    private void showShareFileDialog(String filePath) {

        File fileWithinMyDir = new File(filePath);
        String sharingFileString = mContext.getResources().getString(R.string.sharing_file);
        String sharingFileSubject = mContext.getResources().getString(R.string.sharing_file_subject);
        String shareFileString = mContext.getResources().getString(R.string.share_file);

        Uri contentUri = FileProvider.getUriForFile(mContext, FILE_PROVIDER_DOMAIN, new File(filePath));

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType(MIME_APPLICATION_XSL);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, contentUri);

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, sharingFileSubject);
            intentShareFile.putExtra(Intent.EXTRA_TEXT, sharingFileString);

            mContext.startActivity(Intent.createChooser(intentShareFile, shareFileString));
        }
    }
}
