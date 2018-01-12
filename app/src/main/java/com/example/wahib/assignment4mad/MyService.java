package com.example.wahib.assignment4mad;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;


public class MyService extends Service {
    private static final String TAG = "MyService";
    private boolean isRunning = false;
    private Looper looper;
    private MyServiceHandler myServiceHandler;

    @Override
    public void onCreate() {
        HandlerThread handlerthread = new HandlerThread("MyThread");
        handlerthread.start();
        looper = handlerthread.getLooper();
        myServiceHandler = new MyServiceHandler(looper);
        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = myServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.arg2 = intent.getIntExtra("limit", 0);
        myServiceHandler.sendMessage(msg);
        Toast.makeText(this, "MyService Started.", Toast.LENGTH_SHORT).show();
        //If service is killed while starting, it restarts.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }

    private final class MyServiceHandler extends Handler {
        public MyServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
                int limit = msg.arg2;
                for (int i = 0; i <= limit; i++) {

                    int percentage = (i * 100) / limit;

                    try {
                        Thread.sleep(1000);
                        Event event = new Event(percentage);
                        EventBus.getDefault().post(event);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!isRunning) {
                        break;
                    }
                }
            }
            //stops the service for the start id.
            stopSelfResult(msg.arg1);
        }
    }
}
