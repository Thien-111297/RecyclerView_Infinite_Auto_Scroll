package kot.android.recyclerviewinfiniteautoscroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcvBanner;
    List<String> urls;
    Timer timer;
    TimerTask timerTask;
    int position;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getListUrl();
        rcvBanner = findViewById(R.id.rcvBanner);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rcvBanner.setLayoutManager(layoutManager);

        BannerAdapter bannerAdapter = new BannerAdapter(MainActivity.this,urls);
        rcvBanner.setAdapter(bannerAdapter);
        if(urls!=null){
            position=Integer.MAX_VALUE/2;
            rcvBanner.scrollToPosition(Integer.MAX_VALUE/2);

        }
        // Tu dong can ra giua man hinh voi RecycleView
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcvBanner);
        rcvBanner.smoothScrollBy(5,0);

        rcvBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState==1){
                    stopAutoScrollBanner();
                }else if(newState==0){
                    position=layoutManager.findFirstCompletelyVisibleItemPosition();
                    autoRunScrollBanner();
                }
            }
        });
    }
    private void autoRunScrollBanner(){
        if(timer==null&&timerTask==null){
            timer=new Timer();
            timerTask=new TimerTask() {
                @Override
                public void run() {
                    if(position==Integer.MAX_VALUE){
                        position=Integer.MAX_VALUE/2;
                        rcvBanner.smoothScrollBy(5,0);
                    }else {
                        position++;
                        rcvBanner.smoothScrollToPosition(position);
                    }
                }
            };
            timer.schedule(timerTask,4000,4000);
        }

    }
    private void stopAutoScrollBanner(){
        if(timer!=null&&timerTask!=null){
            timerTask.cancel();
            timer.cancel();
            timer=null;
            timerTask=null;
            position=layoutManager.findFirstCompletelyVisibleItemPosition();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoRunScrollBanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoScrollBanner();
    }

    private void getListUrl(){
        urls = new ArrayList<>();
        urls.add("https://znews-photo.zadn.vn/w660/Uploaded/xbhunku/2019_01_25/0418_2.jpg");
        urls.add("https://i.ytimg.com/vi/LaFtAcIrGWA/maxresdefault.jpg");
        urls.add("https://znews-photo.zadn.vn/w1024/Uploaded/mdf_rkxrxd/2019_08_11/Tom_va_jerry_5.jpg");
        urls.add("https://image.thanhnien.vn/768/uploaded/congthang/2017_04_12/tt_ymdj.jpg");

    }
}
