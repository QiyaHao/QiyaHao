package com.example.bluetoothdemo;





import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView tv_blueToothStatus;
    Button btn_linkBlueTooth;
    Button btn_send;
    private BluetoothAdapter bluetoothAdapter;
    BluetoothChatUtil mBlthChatUtil;
    boolean readOver = true;
    String strGet = "";
    String imgpath = "";
    private Switch swcwhitelight;
    private Switch swcyinglight;
    private  EditText emo_v_1,emo_v_2;//获取电机速度
    private  EditText emo_t_1,emo_t_2;//获取电机时间分钟
    private EditText emo_t_1_s,emo_t_2_s;//获取电机时间秒

    private  Button bturnonmo_1,bpausemo_1,bturnoffmo_1;//控制电机1开，暂停，关
    private Chronometer record_mo_1;

    private  Button bturnonmo_2,bpausemo_2,bturnoffmo_2;//控制电机2，暂停,开,关
    private EditText etemp,etemp_time,etemp_time_s;//获取温度与控温时间
    private int startTime_mo_1,startTime_mo_2,startTime_temp;//开始时间
    private int Temperature;
    private long recordingTime_mo_1=0,recordingTime_mo_2=0,recordingTime_temp=0;
    private  Button bturn_temp,bpause_tem,bturnoff_temp;//控制温度开，关，暂停

    private EditText Ttemp_num;//获取实时温度

    private final String TAG = getClass().getSimpleName();
    private Button mTakePhoto, mChooseFromAlbum;//拍照和选取照片按钮
    private Button mAnalyse;//分析照片按钮

    private ImageView mPicture;//展示照片
    private static final int REQUEST_PERMISSION_CODE = 267;
    private static final int TAKE_PHOTO = 189;
    private static final int CHOOSE_PHOTO = 385;
    private static final String FILE_PROVIDER_AUTHORITY = "cn.fonxnickel.officialcamerademo.fileprovider";
    private Uri mImageUri, mImageUriFromFile;
    private File imageFile;

    private Timer timer1 = null,timer2=null;
    private TimerTask timerTask1 = null,timerTask2 = null;
    private Button bturn_all,bturnoff_all,bpause_all;
    private boolean isnull(String str) {
        if ((str.equals("")) ||( str == null)) {
            return  true;
        }else {
            return false;
        }
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//s设置屏幕方向

        /*申请读取存储的权限*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        tv_blueToothStatus = (TextView) findViewById(R.id.tv_blueToothStatus);
        btn_linkBlueTooth = (Button) findViewById(R.id.btn_linkBlueTooth);
        swcwhitelight=findViewById(R.id.swc_whitelight);
        swcyinglight=findViewById(R.id.swc_yinglight);
        emo_v_1=findViewById(R.id.mo_v_1);//获取电机1的速度
        emo_v_2=findViewById(R.id.mo_v_2);//获取电机2的速度


        emo_t_1=findViewById(R.id.mo_t_1);//获取电机1的时间
        emo_t_1_s=findViewById(R.id.mo_t_1_s);

        emo_t_2=findViewById(R.id.mo_t_2);//获取电机2的时间
        emo_t_2_s=findViewById(R.id.mo_t_2_s);

        bturnonmo_1=findViewById(R.id.turnon_mo_1);//获取控制电机1开的按钮
        bpausemo_1=findViewById(R.id.stop_mo_1);
        bturnoffmo_1=findViewById(R.id.turnoff_mo_1);//控制电机1关的按钮

        bturnonmo_2=findViewById(R.id.turnon_mo_2);//获取控制电机2开的按钮
        bpausemo_2=findViewById(R.id.stop_mo_2);
        bturnoffmo_2=findViewById(R.id.turnoff_mo_2);//控制电机2关的按钮

        etemp=findViewById(R.id.temp);
        etemp_time=findViewById(R.id.temp_time);
        etemp_time_s=findViewById(R.id.temp_time_s);

        bturn_temp=findViewById(R.id.turnon_temp);
        Ttemp_num=findViewById(R.id.temp_num);
        bturnoff_temp=findViewById(R.id.turnoff_temp);
        bpause_tem=findViewById(R.id.stop_temp);


        bturn_all=findViewById(R.id.turnon_all);
        bturnoff_all=findViewById(R.id.stop_all);
        bpause_all=findViewById(R.id.pause_all);


        mPicture = (ImageView) findViewById(R.id.iv_picture);
        mTakePhoto = (Button) findViewById(R.id.bt_take_photo);
        mChooseFromAlbum = (Button) findViewById(R.id.bt_choose_from_album);
        mAnalyse=(Button) findViewById(R.id.bt_analyse);

        final  Chronometer chronometer_mo_1=findViewById(R.id.chronometer_mo_1);
        //chronometer_mo_1.isCountDown();
        final  Chronometer chronometer_mo_2=findViewById(R.id.chronometer_mo_2);
        final  Chronometer chronometer_temp=findViewById(R.id.chronometer_temp);
        int hour_1 = (int) ((SystemClock.elapsedRealtime() - chronometer_mo_1.getBase()) / 1000 / 60);
        chronometer_mo_1.setFormat("0" + String.valueOf(hour_1) + ":%s");
        int hour_2 = (int) ((SystemClock.elapsedRealtime() - chronometer_mo_2.getBase()) / 1000 / 60);
        chronometer_mo_2.setFormat("0" + String.valueOf(hour_2) + ":%s");
        int hour_3 = (int) ((SystemClock.elapsedRealtime() - chronometer_temp.getBase()) / 1000 / 60);
        chronometer_temp.setFormat("0" + String.valueOf(hour_3) + ":%s");

        /*
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask(){
            public void run(){
                try {
                    String msg=new String("T");
                    byte[] buffer2= String.valueOf(msg).toString().getBytes();
                    mBlthChatUtil.write(buffer2);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        Timer timer2=new Timer();
        TimerTask timerTask2=new TimerTask(){
            public void run(){
                try {
                    String temp=Ttemp_num.getText().toString();
                    int j=Integer.parseInt(temp);
                    if(j<=Temperature){
                        //加热
                        String msg=new String("S1");
                        byte[] buffer2= String.valueOf(msg).toString().getBytes();
                        mBlthChatUtil.write(buffer2);

                    }else{
                        //停止加热
                        String msg=new String("S0");
                        byte[] buffer2= String.valueOf(msg).toString().getBytes();
                        mBlthChatUtil.write(buffer2);
                    }
                    System.out.println(j);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
*/
        //分析按钮，跳转到分析界面
        mAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AnalyseActivity.class);
                startActivity(intent);
            }
        });


        //分析照片按钮
        /*
        mAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgpath==""){
                    showToast("请选择一张照片");
                }
                Bitmap bitmap = BitmapFactory.decodeFile(imgpath);
                int b=getBright(bitmap);
                String s=String.valueOf(b);


                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();






            }
        });
        */



        //控制白光通信
        swcwhitelight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //白光开
                    String msg=new String("b1");
                    byte[] buffer2= String.valueOf(msg).toString().getBytes();
                    mBlthChatUtil.write(buffer2);
                    Toast.makeText(MainActivity.this,"白光亮",Toast.LENGTH_SHORT).show();

                }else{
                    String msg=new String("b0");
                    byte[] buffer2= String.valueOf(msg).toString().getBytes();
                    mBlthChatUtil.write(buffer2);
                    Toast.makeText(MainActivity.this,"白光灭",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //控制荧光通信
        swcyinglight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //荧光开
                    String msg=new String("y1");
                    byte[] buffer2= String.valueOf(msg).toString().getBytes();
                    mBlthChatUtil.write(buffer2);
                    Toast.makeText(MainActivity.this,"荧光亮",Toast.LENGTH_SHORT).show();

                }else{
                    String msg=new String("y0");
                    byte[] buffer2= String.valueOf(msg).toString().getBytes();
                    mBlthChatUtil.write(buffer2);
                    Toast.makeText(MainActivity.this,"荧光灭",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //


        //控制电机1开
        bturnonmo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1,获取速度
                String v_1=emo_v_1.getText().toString();

                //String v_moto_1= String.valueOf(Integer.parseInt(emo_v_1.getText().toString())*5+345);
                //2,获取时间
                String time_mo_1=emo_t_1.getText().toString();
                String time_mo_1_s=emo_t_1_s.getText().toString();

                if(isnull(v_1)||isnull(time_mo_1)||isnull(time_mo_1_s)){
                    Toast.makeText(MainActivity.this,"请确保电机1数据输入完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    startTime_mo_1 =(Integer.parseInt(emo_t_1.getText()
                            .toString())) *60+(Integer.parseInt(emo_t_1_s.getText().toString()));
                    //v_1=String.valueOf(Integer.parseInt(emo_v_1.getText().toString())*4+302);
                    //v_1=String.valueOf(Integer.parseInt(Double.parseDouble(emo_v_1.getText().toString()))*2.38+313.11));
                    Double d=Double.parseDouble(emo_v_1.getText().toString())*2.88+313.54;
                    int i=d.intValue();
                    v_1=String.valueOf(i);
                    Toast.makeText(MainActivity.this,v_1,Toast.LENGTH_SHORT).show();
                }


                /*
                String time=new String();
                time=time_mo_1+time_mo_1_s;

                if (!(time.equals("") && time != null)) {
                    startTime_mo_1 =(Integer.parseInt(emo_t_1.getText()
                            .toString())) *60+(Integer.parseInt(emo_t_1_s.getText().toString()));

                }

                 */
                // 跳过已经记录了的时间，起到继续计时的作用
                chronometer_mo_1.setBase(SystemClock.elapsedRealtime()-recordingTime_mo_1);
                // 开始记时
                chronometer_mo_1.start();
                //p3av p5d0

                String msg1=new String("e");
                //String msg2=new String("p5d0");
                String msg=new String();
                msg=msg1+v_1;



                //String msg1=new String("e");
                //String msg=new String();
                //msg=msg1+v_1;

                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                mBlthChatUtil.write(buffer2);
            }
        });
        //控制电机1暂停
        bpausemo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer_mo_1.stop();
                //保存这次记录的时间
                recordingTime_mo_1=SystemClock.elapsedRealtime()-chronometer_mo_1.getBase();//getBase():返回时间
               // String msg=new String("p3d0p5d0");
                String msg=new String("f");
                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                mBlthChatUtil.write(buffer2);
                Toast.makeText(MainActivity.this,"电机1暂停运转",Toast.LENGTH_SHORT).show();
            }
        });
        //控制电机1关
        bturnoffmo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //p3d0 p5d0
                recordingTime_mo_1=0;
                chronometer_mo_1.stop();
                chronometer_mo_1.setBase(SystemClock.elapsedRealtime());
                emo_t_1.setText(null);
                emo_v_1.setText(null);
                emo_t_1_s.setText(null);
                String msg=new String("f");
                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                mBlthChatUtil.write(buffer2);
                Toast.makeText(MainActivity.this,"电机1终止运转",Toast.LENGTH_SHORT).show();

            }
        });

        chronometer_mo_1
                .setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        // 如果开始计时到现在超过了startime秒,也就是说到了时间
                        if (SystemClock.elapsedRealtime()
                                - chronometer.getBase() > startTime_mo_1 * 1000) {
                            chronometer_mo_1.stop();
                            // 给用户提示
                            chronometer_mo_2.setBase(SystemClock.elapsedRealtime());
                            String msg=new String("f");
                            byte[] buffer2= String.valueOf(msg).toString().getBytes();
                            mBlthChatUtil.write(buffer2);
                            emo_t_1.setText("0");
                            emo_v_1.setText("0");
                            emo_t_1_s.setText("0");


                        }
                    }
                });
        //控制电机2开
        bturnonmo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String v_2=emo_v_2.getText().toString();
                //2,获取时间
                String time_mo_2=emo_t_2.getText().toString();
                String time_mo_2_s=emo_t_2_s.getText().toString();
                if(isnull(v_2)||isnull(time_mo_2)||isnull(time_mo_2_s)){
                    Toast.makeText(MainActivity.this,"请确保电机2的数据输入完整",Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    startTime_mo_2 =(Integer.parseInt(emo_t_2.getText()
                            .toString())) *60+Integer.parseInt(emo_t_2_s.getText().toString());
                    //v_2=String.valueOf(Integer.parseInt(emo_v_2.getText().toString())*4+304);

                    Double d=Double.parseDouble(emo_v_2.getText().toString())*2.88+313.54;
                    int i=d.intValue();
                    v_2=String.valueOf(i);
                    Toast.makeText(MainActivity.this,v_2,Toast.LENGTH_SHORT).show();


                }



                // 跳过已经记录了的时间，起到继续计时的作用
                chronometer_mo_2.setBase(SystemClock.elapsedRealtime()-recordingTime_mo_2);
                // 开始记时
                chronometer_mo_2.start();

                String msg1=new String("g");
                //String msg2=new String("p9d0");
                String msg=new String();
                msg=msg1+v_2;
                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                mBlthChatUtil.write(buffer2);

            }
        });
        //控制电机2暂停
        bpausemo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer_mo_2.stop();
                //保存这次记录的时间
                recordingTime_mo_2=SystemClock.elapsedRealtime()-chronometer_mo_2.getBase();//getBase():返回时间
                String msg=new String("h");
                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                mBlthChatUtil.write(buffer2);
                Toast.makeText(MainActivity.this,"电机2暂停运转",Toast.LENGTH_SHORT).show();

            }
        });
        //控制电机2关
        bturnoffmo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //p3d0 p5d0
                recordingTime_mo_2=0;
                chronometer_mo_2.stop();
                chronometer_mo_2.setBase(SystemClock.elapsedRealtime());
                emo_t_2.setText(null);
                emo_v_2.setText(null);
                emo_t_2_s.setText(null);
                String msg=new String("h");
                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                mBlthChatUtil.write(buffer2);
                Toast.makeText(MainActivity.this,"电机2终止运转",Toast.LENGTH_SHORT).show();

            }
        });
        chronometer_mo_2
                .setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        // 如果开始计时到现在超过了startime秒,也就是说到了时间
                        if (SystemClock.elapsedRealtime()
                                - chronometer.getBase() > startTime_mo_2 * 1000) {
                            chronometer_mo_2.stop();

                            // 给用户提示
                            chronometer_mo_2.setBase(SystemClock.elapsedRealtime());
                            String msg=new String("h");
                            byte[] buffer2= String.valueOf(msg).toString().getBytes();
                            mBlthChatUtil.write(buffer2);
                            emo_t_2.setText("0");
                            emo_v_2.setText("0");
                            emo_t_2_s.setText("0");


                        }
                    }
                });
        //控制温度加热
        bturn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取一下用户输入的时间和温度
                //1,获取温度
                String T1=etemp.getText().toString();
                //2,获取时间
                String ss = etemp_time.getText().toString();
                String ss2=etemp_time_s.getText().toString();
                if (isnull(ss)||isnull(ss2)||isnull(T1)) {
                    Toast.makeText(MainActivity.this,"请确保温度控制的数据输入完整",Toast.LENGTH_SHORT).show();
                    return;

                }else{
                    startTime_temp =(Integer.parseInt(etemp_time.getText()
                            .toString())) *60+Integer.parseInt(etemp_time_s.getText().toString());
                    Temperature=Integer.parseInt(etemp.getText().toString());
                }
                int time=(Integer.parseInt(etemp_time.getText().toString()))*1000;


                if (timer1 == null) {
                    timer1 = new Timer();
                }
                if (timerTask1 == null){
                     timerTask1=new TimerTask(){
                        public void run(){
                            try {
                                String msg=new String("T");
                                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                                mBlthChatUtil.write(buffer2);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    };
                }


                if (timer2 == null) {
                    timer2 = new Timer();
                }
                if (timerTask2 == null){
                  timerTask2=new TimerTask(){
                        public void run(){
                            try {
                                String temp=Ttemp_num.getText().toString();
                                int j=Integer.parseInt(temp);
                                if(j<=Temperature){
                                    //加热
                                    String msg=new String("S1");
                                    byte[] buffer2= String.valueOf(msg).toString().getBytes();
                                    mBlthChatUtil.write(buffer2);

                                }else{
                                    //停止加热
                                    String msg=new String("S0");
                                    byte[] buffer2= String.valueOf(msg).toString().getBytes();
                                    mBlthChatUtil.write(buffer2);
                                }
                                System.out.println(j);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    };
                }


                if(timer1 != null && timerTask1 != null ){
                    timer1.schedule(timerTask1,0,1000);
                }
                if(timer2 != null && timerTask2 != null ){
                    timer2.schedule(timerTask2,2000,2000);
                }



                // 跳过已经记录了的时间，起到继续计时的作用
                chronometer_temp.setBase(SystemClock.elapsedRealtime()-recordingTime_temp);
                // 开始记时
                chronometer_temp.start();



            }
        });
        //暂停加热
        bpause_tem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer_temp.stop();
                // 保存这次记录了的时间
                //SystemClock.elapsedRealtime()是系统启动到现在的毫秒数
                recordingTime_temp=SystemClock.elapsedRealtime()-chronometer_temp.getBase();//getBase():返回时间
                // 给用户提示
                String msg=new String("S0");
                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                mBlthChatUtil.write(buffer2);
                if (timer1 != null) {
                    timer1.cancel();
                    timer1 = null;
                }
                if (timerTask1 != null) {
                    timerTask1.cancel();
                    timerTask1 = null;
                }
                if (timer2 != null) {
                    timer2.cancel();
                    timer2 = null;
                }
                if (timerTask2 != null) {
                    timerTask2.cancel();
                    timerTask2 = null;
                }



            }
        });
        //停止加热
        bturnoff_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送停止加热
                String msg=new String("S0");

                byte[] buffer2= String.valueOf(msg).toString().getBytes();
                mBlthChatUtil.write(buffer2);
                chronometer_temp.stop();
                chronometer_temp.setBase(SystemClock.elapsedRealtime());// setBase(long base):设置计时器的起始时间
                etemp.setText(null);//输入框清空
                etemp_time.setText(null);
                Ttemp_num.setText("未测量");
                etemp_time_s.setText(null);
                if (timer1 != null) {
                    timer1.cancel();
                    timer1 = null;
                }
                if (timerTask1 != null) {
                    timerTask1.cancel();
                    timerTask1 = null;
                }
                if (timer2 != null) {
                    timer2.cancel();
                    timer2 = null;
                }
                if (timerTask2 != null) {
                    timerTask2.cancel();
                    timerTask2 = null;
                }
            }
        });
        chronometer_temp
                .setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        // 如果开始计时到现在超过了startime秒,也就是说到了时间
                        if (SystemClock.elapsedRealtime()
                                - chronometer.getBase() > startTime_temp * 1000) {
                            chronometer_temp.stop();
                            etemp.setText("0");//输入框清空
                            etemp_time.setText("0");
                            etemp_time_s.setText("0");
                            Ttemp_num.setText("24");
                            // 给用户提示
                            String msg=new String("S0");
                            byte[] buffer2= String.valueOf(msg).toString().getBytes();
                            mBlthChatUtil.write(buffer2);
                            if (timer1 != null) {
                                timer1.cancel();
                                timer1 = null;
                            }
                            if (timerTask1 != null) {
                                timerTask1.cancel();
                                timerTask1 = null;
                            }
                            if (timer2 != null) {
                                timer2.cancel();
                                timer2 = null;
                            }
                            if (timerTask2 != null) {
                                timerTask2.cancel();
                                timerTask2 = null;
                            }


                        }
                    }
                });


        bturn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取电机1数据
                String v_1=emo_v_1.getText().toString();

                String time_mo_1=emo_t_1.getText().toString();
                String time_mo_1_s=emo_t_1_s.getText().toString();
                //获取电机2数据`
                String v_2=emo_v_2.getText().toString();
                String time_mo_2=emo_t_2.getText().toString();
                String time_mo_2_s=emo_t_2_s.getText().toString();
                //获取测温数据
                String T=etemp.getText().toString();
                String time_temp = etemp_time.getText().toString();
                String time_temp_s=etemp_time_s.getText().toString();




                if(isnull(v_1)||isnull(time_mo_1)||isnull(time_mo_1_s)||isnull(v_2)||isnull(time_mo_1)||isnull(time_mo_2_s)||isnull(time_temp)||isnull(time_temp_s)||isnull(T)){
                    Toast.makeText(MainActivity.this,"请确保数据输入完整",Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    startTime_mo_1 =(Integer.parseInt(emo_t_1.getText()
                            .toString())) *60+(Integer.parseInt(emo_t_1_s.getText().toString()));
                    startTime_mo_2 =(Integer.parseInt(emo_t_2.getText()
                            .toString())) *60+(Integer.parseInt(emo_t_2_s.getText().toString()));
                    startTime_temp =(Integer.parseInt(etemp_time.getText()
                            .toString())) *60+(Integer.parseInt(etemp_time_s.getText().toString()));
                    //v_1=String.valueOf(Integer.parseInt(emo_v_1.getText().toString())*4+302);
                    //v_2=String.valueOf(Integer.parseInt(emo_v_2.getText().toString())*4+302);


                    Double d_1=Double.parseDouble(emo_v_1.getText().toString())*2.88+313.54;
                    int i_1=d_1.intValue();
                    v_1=String.valueOf(i_1);

                    Double d_2=Double.parseDouble(emo_v_2.getText().toString())*2.88+313.54;
                    int i_2=d_2.intValue();
                    v_2=String.valueOf(i_2);

                    Temperature=Integer.parseInt(etemp.getText().toString())-2;



                }
                chronometer_mo_1.setBase(SystemClock.elapsedRealtime()-recordingTime_mo_1);
                chronometer_mo_1.start();
                String msg_a_1=new String("e");
                //String msg_a_2=new String("p5d0");
                String msg_a=new String();
                msg_a=msg_a_1+v_1;
                byte[] buffer1= String.valueOf(msg_a).toString().getBytes();
                mBlthChatUtil.write(buffer1);

                chronometer_mo_2.setBase(SystemClock.elapsedRealtime()-recordingTime_mo_2);
                chronometer_mo_2.start();
                String msg_b_1=new String("g");
                String msg_b=new String();
                msg_b=msg_b_1+v_2;
                byte[] buffer2= String.valueOf(msg_b).toString().getBytes();
                //mBlthChatUtil.write(buffer2);

                Timer timer=new Timer(true);
                TimerTask timerTask=new TimerTask(){
                    public void run(){
                        try {
                            mBlthChatUtil.write(buffer2);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                timer.schedule(timerTask,1000);



                if (timer1 == null) {
                    timer1 = new Timer();
                }
                //测温
                if (timerTask1 == null){
                    timerTask1=new TimerTask(){
                        public void run(){
                            try {
                                String msg=new String("T");
                                byte[] buffer3= String.valueOf(msg).toString().getBytes();
                                mBlthChatUtil.write(buffer3);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    };
                }


                if (timer2 == null) {
                    timer2 = new Timer();
                }
                if (timerTask2 == null){
                    timerTask2=new TimerTask(){
                        public void run(){
                            try {
                                String temp=Ttemp_num.getText().toString();
                                int j=Integer.parseInt(temp);
                                if(j<Temperature){
                                    //加热
                                    String msg=new String("S1");
                                    byte[] buffer4= String.valueOf(msg).toString().getBytes();
                                    mBlthChatUtil.write(buffer4);

                                }else{
                                    //停止加热
                                    String msg=new String("S0");
                                    byte[] buffer5= String.valueOf(msg).toString().getBytes();
                                    mBlthChatUtil.write(buffer5);
                                }
                                //System.out.println(j);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    };
                }


                if(timer1 != null && timerTask1 != null ){
                    timer1.schedule(timerTask1,0,2000);
                }
                if(timer2 != null && timerTask2 != null ){
                    timer2.schedule(timerTask2,5000,2000);
                }
                // 跳过已经记录了的时间，起到继续计时的作用
                chronometer_temp.setBase(SystemClock.elapsedRealtime()-recordingTime_temp);
                // 开始记时
                chronometer_temp.start();






                //控制电机2开
                /*
                String v_2=emo_v_2.getText().toString();
                String time_mo_2=emo_t_2.getText().toString();
                String time_mo_2_s=emo_t_2_s.getText().toString();
                String time2=new String();
                time2=time_mo_2+time_mo_2_s;
                if (!(time2.equals("") && time2 != null)) {
                    startTime_mo_2 =(Integer.parseInt(emo_t_2.getText()
                            .toString())) *60+(Integer.parseInt(emo_t_2_s.getText().toString()));

                }
                chronometer_mo_2.setBase(SystemClock.elapsedRealtime()-recordingTime_mo_2);
                chronometer_mo_2.start();
                String msg_b_1=new String("f");

                String msg_b=new String();
                msg_b=msg_b_1+v_2;
                byte[] buffer2= String.valueOf(msg_b).toString().getBytes();



                Timer timer=new Timer(true);
                TimerTask timerTask=new TimerTask(){
                    public void run(){
                        try {
                            mBlthChatUtil.write(buffer2);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                timer.schedule(timerTask,1000);

                //byte[] buffer2= String.valueOf(msg_b).toString().getBytes();
                //mBlthChatUtil.write(buffer2);


                //控制加热
                //获取一下用户输入的时间和温度
                /*
                 */



            }
        });
        bpause_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer_mo_1.stop();
                recordingTime_mo_1=SystemClock.elapsedRealtime()-chronometer_mo_1.getBase();//getBase():返回时间
                String msg_a=new String("f");
                byte[] buffer1= String.valueOf(msg_a).toString().getBytes();
                mBlthChatUtil.write(buffer1);

                chronometer_mo_2.stop();
                recordingTime_mo_2=SystemClock.elapsedRealtime()-chronometer_mo_2.getBase();//getBase():返回时间
                String msg_b=new String("h");
                byte[] buffer2= String.valueOf(msg_b).toString().getBytes();
                mBlthChatUtil.write(buffer2);

                chronometer_temp.stop();
                // 保存这次记录了的时间
                //SystemClock.elapsedRealtime()是系统启动到现在的毫秒数
                recordingTime_temp=SystemClock.elapsedRealtime()-chronometer_temp.getBase();//getBase():返回时间
                // 给用户提示
                String msg_c=new String("S0");
                byte[] buffer3= String.valueOf(msg_c).toString().getBytes();
                mBlthChatUtil.write(buffer3);
                if (timer1 != null) {
                    timer1.cancel();
                    timer1 = null;
                }
                if (timerTask1 != null) {
                    timerTask1.cancel();
                    timerTask1 = null;
                }
                if (timer2 != null) {
                    timer2.cancel();
                    timer2 = null;
                }
                if (timerTask2 != null) {
                    timerTask2.cancel();
                    timerTask2 = null;
                }



            }
        });
        bturnoff_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //电机1关
                recordingTime_mo_1=0;
                chronometer_mo_1.stop();
                chronometer_mo_1.setBase(SystemClock.elapsedRealtime());

                emo_t_1.setText(null);
                emo_v_1.setText(null);
                emo_t_1_s.setText(null);

                String msg_a=new String("f");
                byte[] buffer1= String.valueOf(msg_a).toString().getBytes();
                mBlthChatUtil.write(buffer1);
               //电机2关
                recordingTime_mo_2=0;
                chronometer_mo_2.stop();
                chronometer_mo_2.setBase(SystemClock.elapsedRealtime());

                emo_t_2.setText(null);
                emo_t_2_s.setText(null);
                emo_v_2.setText(null);

                String msg_b=new String("h");
                byte[] buffer2= String.valueOf(msg_b).toString().getBytes();
                //mBlthChatUtil.write(buffer2);
                Timer timer=new Timer(true);
                TimerTask timerTask=new TimerTask(){
                    public void run(){
                        try {
                            mBlthChatUtil.write(buffer2);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                timer.schedule(timerTask,1000);
                //发送停止加热
                String msg_c=new String("S0");
                byte[] buffer3= String.valueOf(msg_c).toString().getBytes();
                mBlthChatUtil.write(buffer3);
                chronometer_temp.stop();
                chronometer_temp.setBase(SystemClock.elapsedRealtime());// setBase(long base):设置计时器的起始时间

                etemp.setText(null);//输入框清空
                Ttemp_num.setText("unmeasured");
                etemp_time.setText(null);
                etemp_time_s.setText(null);

                if (timer1 != null) {
                    timer1.cancel();
                    timer1 = null;
                }
                if (timerTask1 != null) {
                    timerTask1.cancel();
                    timerTask1 = null;
                }
                if (timer2 != null) {
                    timer2.cancel();
                    timer2 = null;
                }
                if (timerTask2 != null) {
                    timerTask2.cancel();
                    timerTask2 = null;
                }




            }
        });





        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        mChooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });









        if (isSupported()) {
            Toast.makeText(this, "the device supports bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "the device does not support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (bluetoothAdapter.isEnabled()) {
            //已经打开蓝牙，判断Android版本是否需要添加权限，解决：无法发现蓝牙设备的问题
            Toast.makeText(this, "bluetooth on", Toast.LENGTH_SHORT).show();
            getPermission();
        } else {
            //开启蓝牙
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothAdapter.enable();
            //关闭蓝牙：bluetoothAdapter.disable();
        }
    }

    /**
     * 连接蓝牙
     * @param view
     */
    public void linkBlueTooth(View view) {
       // etxt_receiveMessage.setText("");
        mBlthChatUtil = BluetoothChatUtil.getInstance(this);
        mBlthChatUtil.registerHandler(mHandler);
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            //如果有配对的设备
            for(BluetoothDevice device : pairedDevices){
                //通过array adapter在列表中添加设备名称和地址
                if(device.getName().equals("HC-06")){
                    if (bluetoothAdapter.isDiscovering()) {
                        //取消搜索
                        bluetoothAdapter.cancelDiscovery();
                    }
                    if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED) {
                        showToast("BT has already connected");
                    }else {
                        mBlthChatUtil.connect(device);
                        if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED) {
                            showToast("connected");
                        }

                    }
                    break;
                }
            }
        }else{
            showToast("there are no paired devices yet");
        }
    }

    /**
     * 发送
     * @param view
     */
    public void send(View view) {
        //byte[] buffer2= String.valueOf(etxt_sendMessage.getText()).toString().getBytes();
       // mBlthChatUtil.write(buffer2);
    }

    /**
     * 判断是否设备是否支持蓝牙
     * @return 是否支持
     */
    private boolean isSupported() {
        //初始化
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
        } else {
            return true;
        }
    }

    /*
      获取权限
     */
    @SuppressLint("WrongConstant")
    private void getPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = 0;
            permissionCheck = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionCheck += this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                int ACCESS_LOCATION = 1;// 自定义常量,任意整型
                //未获得权限
                this.requestPermissions( // 请求授权
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        ACCESS_LOCATION);
            }
        }
    }

    /**
     * 显示消息
     * @param str
     */
    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 消息句柄
     */
    private Handler mHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.R)
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
            switch(msg.what){
                case BluetoothChatUtil.STATE_CONNECTED:
                    showToast("The connection is successful");
                    tv_blueToothStatus.setText("Bluetooth connected");
                    break;
                case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
                    showToast("The connection failed");
                    break;
                case BluetoothChatUtil.STATE_CHANGE:
                    showToast("Connecting devices..");
                    break;
                case BluetoothChatUtil.MESSAGE_DISCONNECTED:
                    showToast("Disconnect from the device");
                    tv_blueToothStatus.setText("BT state：unlinked");

                    break;
                //读到另一方传送的消息
                case BluetoothChatUtil.MESSAGE_READ:{
//                    showToast("接收消息成功");
                    byte[] buf;
                    String str;
                    buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
                    str = new String(buf,0,buf.length);

                    //根据HC-05传来的消息进行相应的处理后显示
                    if(readOver){
                        strGet=str.trim();
                        if(strGet.length()<9){
                            readOver=false;
                            return;
                        }
                    }
                    else{
                        strGet+=str.trim();
                        readOver=true;
                    }
                    
                    //etxt_receiveMessage.setText(etxt_receiveMessage.getText()+"\n"+strGet);


                    Ttemp_num.setText(str.trim());

                    break;
                }

                case BluetoothChatUtil.MESSAGE_WRITE:{
//                    showToast("发送消息成功");
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);//打开相册
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            imageFile = createImageFile();//创建用来保存照片的文件
            mImageUriFromFile = Uri.fromFile(imageFile);
            Log.i(TAG, "takePhoto: uriFromFile " + mImageUriFromFile);
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
            }
        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /*申请权限的回调*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: permission granted");
        } else {
            Log.i(TAG, "onRequestPermissionsResult: permission denied");
            //Toast.makeText(this, "You Denied Permission", Toast.LENGTH_SHORT).show();
        }
    }

    /*相机或者相册返回来的数据*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        Log.i(TAG, "onActivityResult: imageUri " + mImageUri);
                        galleryAddPic(mImageUriFromFile);
                        mPicture.setImageBitmap(bitmap);//显示到ImageView上
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (data == null) {//如果没有选取照片，则直接返回
                    return;
                }
                Log.i(TAG, "onActivityResult: ImageUriFromAlbum: " + data.getData());
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        handleImageOnKitKat(data);//4.4之后图片解析
                    } else {
                        handleImageBeforeKitKat(data);//4.4之前图片解析
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 4.4版本以下对返回的图片Uri的处理：
     * 就是从返回的Intent中取出图片Uri，直接显示就好
     * @param data 调用系统相册之后返回的Uri
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4.4版本以上对返回的图片Uri的处理：
     * 返回的Uri是经过封装的，要进行处理才能得到真实路径
     * @param data 调用系统相册之后返回的Uri
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则提供document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则进行普通处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }


//获取图片像素值
    public int getBright(Bitmap bm){
        if(bm==null) return -1;
        int width=bm.getWidth();
        int height=bm.getHeight();
        int r,g,b;
        int count=0;
        int bright=0;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                count++;
                int localTemp=bm.getPixel(i,j);
                r=(localTemp|0xff00ffff)>>16 & 0x00ff;
                g=(localTemp|0xffff00ff)>>8 &0x0000ff;
                b=(localTemp|0xffffff00)& 0x0000ff;
                bright=(int)(bright+0.299*r+0.587*g+0.114*b);
            }
        }

        return bright/count;
    }








    /**
     * 将imagePath指定的图片显示到ImageView上
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mPicture.setImageBitmap(bitmap);
            imgpath=imagePath;




        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将Uri转化为路径
     * @param uri 要转化的Uri
     * @param selection 4.4之后需要解析Uri，因此需要该参数
     * @return 转化之后的路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {


                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 将拍的照片添加到相册
     *
     * @param uri 拍的照片的Uri
     */
    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }

}
