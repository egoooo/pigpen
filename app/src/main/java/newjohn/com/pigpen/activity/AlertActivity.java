package newjohn.com.pigpen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import newjohn.com.pigpen.MyApplication;
import newjohn.com.pigpen.R;
import newjohn.com.pigpen.bean.AlertData;
import newjohn.com.pigpen.bean.AlertDataDao;
import newjohn.com.pigpen.listviewscroll.AutoListView;
import newjohn.com.pigpen.listviewscroll.CHScrollView;
import newjohn.com.pigpen.listviewscroll.ListViewScrollAdapter;


public class AlertActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener, AdapterView.OnItemClickListener{
    private static final String TAG ="AlertActivity" ;
    private Intent mIntent;
    private MsgReceiver msgReceiver;
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();//用于存放报警信息的集合，AutoListView使用该数据
    AlertDataDao alertDataDao;

    @BindView(R.id.toolbar_a)
    Toolbar toolbar;

    CHScrollView headerScroll;

    AutoListView lstv;
    public ListViewScrollAdapter adapter; //表格的适配器
    int page=0;
//    private Handler handler = new Handler() {
//        @SuppressWarnings("unchecked")
//        public void handleMessage(Message msg) {
//            List<Map<String, String>> result = (List<Map<String, String>>) msg.obj;
//            switch (msg.what) {
//                case AutoListView.REFRESH:
//                    lstv.onRefreshComplete();
//                    list.clear();
//                    list.addAll(result);
//                    break;
//                case AutoListView.LOAD:
//                    lstv.onLoadComplete();
//                    list.addAll(result);
//                    break;
//            }
//            lstv.setResultSize(result.size());
//            adapter.notifyDataSetChanged();
//        };
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        alertDataDao= MyApplication.getMyApplication().getDaoSession().getAlertDataDao();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.communication.RECEIVER");
        registerReceiver(msgReceiver, intentFilter);
//        //启动服务
//        mIntent = new Intent(this, PersistentConnectionService.class);
//        startService(mIntent);

        headerScroll = (CHScrollView) findViewById(R.id.item_scroll_title_a);
        CHScrollView.CHScrollViewHelper.mHScrollViews.clear();

        CHScrollView.CHScrollViewHelper.mHScrollViews.clear();
        CHScrollView.CHScrollViewHelper.mHScrollViews.add(headerScroll);
        lstv = (AutoListView) findViewById(R.id.scroll_list_a);
        adapter = new ListViewScrollAdapter(this, list, R.layout.alert_list_item,
                new String[] { "area", "deviceNum", "value","info", "dateTime"},
                new int[] { R.id.item_title_a, R.id.num_a, R.id.value_a, R.id.info_a,R.id.date_a,},
                R.id.item_scroll_a, lstv);
        lstv.setOnRefreshListener(this);
        lstv.setOnItemClickListener(this);
        lstv.setOnLoadListener(this);

        lstv.setAdapter(adapter);
//        lstv.setOnRefreshListener(new AutoListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getRefreshData();
//
//            }
//        });
//        lstv.setOnLoadListener(new AutoListView.OnLoadListener() {
//            @Override
//            public void onLoad() {
//
//                page++;
//                Log.i(TAG, "onLoad: page"+page);
//                getLoadMoreData(page);
//
//            }
//        });
//        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });

        getRefreshData();




    }


    @Override
    protected void onDestroy() {
//        //停止服务
//        stopService(mIntent);
        //注销广播
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getRefreshData();
            }
        }).start();


    }

    @Override
    public void onLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                page++;
                Log.i(TAG, "onLoad: page"+page);
                getLoadMoreData(page);
            }
        }).start();



    }


    /**
     * 广播接收器
     * @author len
     *
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String progress = intent.getStringExtra("progress");
            Toast.makeText(AlertActivity.this,progress,Toast.LENGTH_SHORT).show();
        }


    }


    public void getRefreshData(){
        List<AlertData> datas=alertDataDao.queryBuilder().offset(0).limit(20).list();
        final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> data = null;
        for (int i = 0; i < datas.size(); i++) {
            data = new HashMap<String, String>();
            data.put("area", datas.get(i).getArea());
            data.put("deviceNum",datas.get(i).getDeviceNum());
            data.put("value" , datas.get(i).getValue());
            data.put("info",datas.get(i).getInfo());
            data.put("dateTime" ,datas.get(i).getDateTime() );

            result.add(data);
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lstv.onRefreshComplete();
                list.clear();
                list.addAll(result);
                lstv.setResultSize(result.size());
                adapter.notifyDataSetChanged();
            }
        });




//        Message msg = handler.obtainMessage();
//        msg.what =1;
//        msg.obj =result;
//        handler.sendMessage(msg);
        

    }
    public void getLoadMoreData(int page){
        List<AlertData> datas=alertDataDao.queryBuilder().offset(page*20).limit(20).list();
        final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> data = null;
        Log.i(TAG, "getLoadMoreData: count"+datas.size()+"page"+page);
        for (int i = 0; i < datas.size(); i++) {
            data = new HashMap<String, String>();
            data.put("area", datas.get(i).getArea());
            data.put("deviceNum",datas.get(i).getDeviceNum());
            data.put("value" , datas.get(i).getValue());
            data.put("info",datas.get(i).getInfo());
            data.put("dateTime" ,datas.get(i).getDateTime() );

            result.add(data);
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lstv.onLoadComplete();

                list.addAll(result);
                lstv.setResultSize(result.size());
                adapter.notifyDataSetChanged();
            }
        });

//        Message msg = handler.obtainMessage();
//        msg.what =1;
//        msg.obj =result;
//        handler.sendMessage(msg);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG,"onCreateOptionsMenu");
        //创建Menu菜单
        getMenuInflater().inflate(R.menu.alert_menu,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.cleanAlert:
                alertDataDao.deleteAll();
                lstv.onRefreshComplete();
                list.clear();
                lstv.setResultSize(0);
                adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}
