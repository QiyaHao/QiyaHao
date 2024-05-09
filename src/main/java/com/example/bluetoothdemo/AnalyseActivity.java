package com.example.bluetoothdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AnalyseActivity extends AppCompatActivity {
    private  static final int PICK_IMAGE_REQUEST_1 = 1;
    private  static final int PICK_IMAGE_REQUEST_2 = 2;
    private  static final int PICK_IMAGE_REQUEST_3 = 3;
    private  static final int PICK_IMAGE_REQUEST_4 = 4;
    private  static final int PICK_IMAGE_REQUEST_5 = 5;
    private  static final int PICK_IMAGE_REQUEST_6 = 6;
    private  static final int PICK_IMAGE_REQUEST_0 = 0;
    private Button bchoose_1,bchoose_2,bchoose_3,bchoose_4,bchoose_5,bchoose_6;//定义选择照片的六个按钮
    private EditText Edit1,Edit2,Edit3,Edit4,Edit5,Edit6;//定义输入名字的六个按钮
    private ImageView image_1,image_2,image_3,image_4,image_5,image_6;//定义六个照片展示的界面
    //定义6个显示结果的textview
    private TextView Text1,Text2,Text3,Text4,Text5,Text6;
    private TextView cn1,cn2,cn3,cn4,cn5,cn6;
    private TextView R1,R2,R3,R4,R5,R6;



    //private ImageView Image_show;
    private Button banalyse;

    Bitmap imgpath_1 ;
    Bitmap imgpath_2 ;
    Bitmap imgpath_3 ;
    Bitmap imgpath_4 ;
    Bitmap imgpath_5 ;
    Bitmap imgpath_6 ;
    Bitmap imgpath_0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);

        //绑定选择照片的按钮id
        bchoose_1=findViewById(R.id.choose_1);
        bchoose_2=findViewById(R.id.choose_2);
        bchoose_3=findViewById(R.id.choose_3);
        bchoose_4=findViewById(R.id.choose_4);
        bchoose_5=findViewById(R.id.choose_5);
        bchoose_6=findViewById(R.id.choose_6);
        //绑定输入名字的id
        Edit1=findViewById(R.id.e1);
        Edit2=findViewById(R.id.e2);
        Edit3=findViewById(R.id.e3);
        Edit4=findViewById(R.id.e4);
        Edit5=findViewById(R.id.e5);
        Edit6=findViewById(R.id.e6);
        //绑定改变后的名字
        cn1=findViewById(R.id.cname1);
        cn2=findViewById(R.id.cname2);
        cn3=findViewById(R.id.cname3);
        cn4=findViewById(R.id.cname4);
        cn5=findViewById(R.id.cname5);
        cn6=findViewById(R.id.cname6);
        //绑定结果 yes or no
        R1=findViewById(R.id.r1);
        R2=findViewById(R.id.r2);
        R3=findViewById(R.id.r3);
        R4=findViewById(R.id.r4);
        R5=findViewById(R.id.r5);
        R6=findViewById(R.id.r6);



        //绑定分析照片的按钮id
        //banalyse_1=findViewById(R.id.analyse_1);
       // banalyse_2=findViewById(R.id.analyse_2);
        //banalyse_3=findViewById(R.id.analyse_3);
        //banalyse_4=findViewById(R.id.analyse_4);
        //banalyse_5=findViewById(R.id.analyse_5);
        //banalyse_6=findViewById(R.id.analyse_6);
        //绑定展示照片的id
        image_1=findViewById(R.id.image_1);
        image_2=findViewById(R.id.image_2);
        image_3=findViewById(R.id.image_3);
        image_4=findViewById(R.id.image_4);
        image_5=findViewById(R.id.image_5);
        image_6=findViewById(R.id.image_6);

        //绑定TextView组件
        Text1=findViewById(R.id.text_1);
        Text2=findViewById(R.id.text_2);
        Text3=findViewById(R.id.text_3);
        Text4=findViewById(R.id.text_4);
        Text5=findViewById(R.id.text_5);
        Text6=findViewById(R.id.text_6);


        //Image_show=findViewById(R.id.image_show);
        Intent intent=getIntent();
        String imagePath=intent.getStringExtra("imagePath");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //Image_show.setImageBitmap(bitmap);

        //绑定总控的分析按钮

        banalyse=findViewById(R.id.analyse);


        bchoose_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(PICK_IMAGE_REQUEST_1);
            }
        });
        bchoose_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(PICK_IMAGE_REQUEST_2);
            }
        });
        bchoose_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(PICK_IMAGE_REQUEST_3);
            }
        });
        bchoose_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(PICK_IMAGE_REQUEST_4);
            }
        });
        bchoose_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(PICK_IMAGE_REQUEST_5);
            }
        });
        bchoose_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(PICK_IMAGE_REQUEST_6);
            }
        });

        banalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bitmap bitmap_1 =imgpath_1;
                Bitmap bitmap_2 =imgpath_2;
                Bitmap bitmap_3 =imgpath_3;
                Bitmap bitmap_4 =imgpath_4;
                Bitmap bitmap_5 =imgpath_5;
                Bitmap bitmap_6 =imgpath_6;


                int b1=getBright(bitmap_1);
                int b2=getBright(bitmap_2);
                int b3=getBright(bitmap_3);
                int b4=getBright(bitmap_4);
                int b5=getBright(bitmap_5);
                int b6=getBright(bitmap_6);

                String name1=Edit1.getText().toString();
                String name2=Edit2.getText().toString();
                String name3=Edit3.getText().toString();
                String name4=Edit4.getText().toString();
                String name5=Edit5.getText().toString();
                String name6=Edit6.getText().toString();



                int b=b1+b2+b3+b4+b5+b6;

                if(b1>0){
                    String s1=String.valueOf(Analyse_photo(b1));
                    String re1=yorn(Analyse_photo(b1));//获得输出yes还是no
                    R1.setText(re1);
                    cn1.setText(name1);
                    Text1.setText(s1);

                }
                else{
                    Text1.setText("0");
                    R1.setText("NO");
                }
                if(b2>0){
                    String s2=String.valueOf(Analyse_photo(b2));
                    String re2=yorn(Analyse_photo(b2));
                    R2.setText(re2);
                    cn2.setText(name2);
                    Text2.setText(s2);
                }
                else{
                    Text2.setText("0");
                    R2.setText("NO");
                }
                if(b3>0){
                    String s3=String.valueOf(Analyse_photo(b3));
                    String re3=yorn(Analyse_photo(b3));
                    R3.setText(re3);
                    cn3.setText(name3);
                    Text3.setText(s3);
                }
                else{
                    Text3.setText("0");
                    R3.setText("NO");
                }
                if(b4>0){
                    String s4=String.valueOf(Analyse_photo(b4));
                    String re4=yorn(Analyse_photo(b4));
                    R4.setText(re4);
                    cn4.setText(name4);
                    Text4.setText(s4);
                }
                else{
                    Text4.setText("0");
                    R4.setText("NO");
                }
                if(b5>0){
                    String s5=String.valueOf(Analyse_photo(b5));
                    String re5=yorn(Analyse_photo(b5));
                    R5.setText(re5);
                    cn5.setText(name5);
                    Text5.setText(s5);
                }
                else{
                    Text5.setText("0");
                    R5.setText("NO");
                }
                if(b6>0){
                    String s6=String.valueOf(Analyse_photo(b6));
                    String re6=yorn(Analyse_photo(b6));
                    R6.setText(re6);
                    cn6.setText(name6);
                    Text6.setText(s6);
                }
                else{
                    Text6.setText("0");
                    R6.setText("NO");
                }

//                String s1=String.valueOf(Analyse_photo(b1));
//                String re1=yorn(Analyse_photo(b1));//获得输出yes还是no
//                String s=String.valueOf(b1);
//                Toast.makeText(AnalyseActivity.this,re1,Toast.LENGTH_SHORT).show();




//                if(b!=-6){
//
//
//                    String s1=String.valueOf(Analyse_photo(b1));
//                    String re1=yorn(Analyse_photo(b1));
//                    R1.setText(re1);
//                    cn1.setText(name1);
//                    Text1.setText(s1);
//
//                    String s2=String.valueOf(Analyse_photo(b2));
//                    String re2=yorn(Analyse_photo(b2));
//                    R2.setText(re2);
//                    cn2.setText(name2);
//                    Text2.setText(s2);
//
//                    String s3=String.valueOf(Analyse_photo(b3));
//                    String re3=yorn(Analyse_photo(b3));
//                    R3.setText(re3);
//                    cn3.setText(name3);
//                    Text3.setText(s3);
//
//                    String s4=String.valueOf(Analyse_photo(b4));
//                    String re4=yorn(Analyse_photo(b4));
//                    R4.setText(re4);
//                    cn4.setText(name4);
//                    Text4.setText(s4);
//
//                    String s5=String.valueOf(Analyse_photo(b5));
//                    String re5=yorn(Analyse_photo(b5));
//                    R5.setText(re5);
//                    cn5.setText(name5);
//                    Text5.setText(s5);
//
//                    String s6=String.valueOf(Analyse_photo(b6));
//                    String re6=yorn(Analyse_photo(b6));
//                    R6.setText(re6);
//                    cn6.setText(name6);
//                    Text6.setText(s6);
//
//                    Toast.makeText(AnalyseActivity.this,s1,Toast.LENGTH_SHORT).show();
//
//                } else{
//                    Toast.makeText(AnalyseActivity.this,"please choose photoes",Toast.LENGTH_SHORT).show();
//                }


            }
        });

    }
    private void openGallery(int id){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, id);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri_1 = data.getData();
            try {
                // 从URI获取Bitmap
                Bitmap bitmap_1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri_1);
                // 将Bitmap显示在ImageView中
                image_1.setImageBitmap(bitmap_1);
                imgpath_1=bitmap_1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_2 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri_2 = data.getData();
            try {
                // 从URI获取Bitmap
                Bitmap bitmap_2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri_2);
                // 将Bitmap显示在ImageView中
                image_2.setImageBitmap(bitmap_2);
                imgpath_2=bitmap_2;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (requestCode == PICK_IMAGE_REQUEST_3 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri_3 = data.getData();
            try {
                // 从URI获取Bitmap
                Bitmap bitmap_3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri_3);
                // 将Bitmap显示在ImageView中
                image_3.setImageBitmap(bitmap_3);
                imgpath_3=bitmap_3;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (requestCode == PICK_IMAGE_REQUEST_4 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri_4 = data.getData();
            try {
                // 从URI获取Bitmap
                Bitmap bitmap_4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri_4);
                // 将Bitmap显示在ImageView中
                image_4.setImageBitmap(bitmap_4);
                imgpath_4=bitmap_4;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (requestCode == PICK_IMAGE_REQUEST_5 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri_5 = data.getData();
            try {
                // 从URI获取Bitmap
                Bitmap bitmap_5 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri_5);
                // 将Bitmap显示在ImageView中
                image_5.setImageBitmap(bitmap_5);
                imgpath_5=bitmap_5;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (requestCode == PICK_IMAGE_REQUEST_6 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri_6 = data.getData();
            try {
                // 从URI获取Bitmap
                Bitmap bitmap_6 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri_6);
                // 将Bitmap显示在ImageView中
                image_6.setImageBitmap(bitmap_6);
                imgpath_6=bitmap_6;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (requestCode == PICK_IMAGE_REQUEST_0 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri_0 = data.getData();
            try {
                // 从URI获取Bitmap
                Bitmap bitmap_0 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri_0);
                // 将Bitmap显示在ImageView中
                image_6.setImageBitmap(bitmap_0);
                imgpath_0=bitmap_0;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
    //分析图片像素值
    public int Analyse_photo(int i){

        int m=0;
        if(i<2){
            m=0;
        } else if (i<5&&i>=2) {
            m=(int)(i*0.834+0.02534);
        }else if (i<25&&i>=5) {
            m=(int)(i*0.867-0.0065);
        }
        else if (i>=25&&i<32) {
            m=(int)(i*0.767+2.497);
        }
        else if (i>=32&&i<39) {
            m=(int)(i*0.729+3.704);
        }
        else if (i>=39&&i<56) {
            m=(int)(i*0.687+5.361);
        }
        else if (i>=56&&i<58) {
            m=(int)(i*0.786-0.214);
        }
        else if (i>=58&&i<74) {
            m=(int)(i*0.098+38.128);
        }
        else if (i>=74&&i<79) {
            m=(int)(i*3.222-193.05);
        }
        else if (i>=79&&i<80) {
            m=(int)(i*0.459+25.25);
        }
        else if (i>=80) {
            m=(int)(i*0.819-3.562);
        }else{
            m=0;
        }
        


        return m;
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
    //输出yes or no
    public String yorn(int n){
        if(n<15)
            return "NO";
        else
            return "YES";

    }


}













