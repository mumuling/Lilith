package com.youloft.lilith.info;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.youloft.lilith.AppConfig;
import com.youloft.lilith.common.utils.Toaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gyh on 2017/7/11.
 */

public class DownloadService extends Service {
    RandomAccessFile raf = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppConfig.DOWNLOAD_STATUS = true;
        String url = intent.getStringExtra("url");
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toaster.showShort("网络错误,下载失败");
                AppConfig.DOWNLOAD_STATUS = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                File dataDirectory;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    dataDirectory = Environment.getExternalStorageDirectory();
                } else {
                    dataDirectory = getFilesDir();
                }
                File file = new File(dataDirectory, "lilith.apk");
                try {
                    raf = new RandomAccessFile(file, "rwd");
                    byte[] b = new byte[4096];
                    int len;
                    while ((len = inputStream.read(b)) != -1) {
                        raf.write(b, 0, len);
                    }
                    AppConfig.DOWNLOAD_STATUS = false;
                    installApk(file);
                } catch (FileNotFoundException e) {
                    AppConfig.DOWNLOAD_STATUS = false;
                    e.printStackTrace();
                    Toaster.showShort("文件存储错误");
                } catch (IOException e) {
                    AppConfig.DOWNLOAD_STATUS = false;
                    e.printStackTrace();
                    Toaster.showShort("文件存储错误");
                } finally {
                    AppConfig.DOWNLOAD_STATUS = false;
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        stopSelf();
    }
}
