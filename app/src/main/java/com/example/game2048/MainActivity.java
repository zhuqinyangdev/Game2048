package com.example.game2048;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static List<AppCompatActivity> activityList=new ArrayList<>();
    private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityList.add(this);


    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder dialog=new AlertDialog.Builder(this)
                .setTitle("提示:")
                .setMessage("你将要退出游戏！")
                .setCancelable(true)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.activityList.get(0).finish();
                    }
                })
                .setNegativeButton("按错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog.show();
    }

}
