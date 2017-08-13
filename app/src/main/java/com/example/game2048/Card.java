package com.example.game2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * Created by ZQY on 2017/8/7.
 */

public class Card extends RelativeLayout {

    private TextView numView;


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        switch (num){
            case 0:
                numView.setBackgroundColor(0xffeee4da);
                break;
            case 2:
                numView.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                numView.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                numView.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                numView.setBackgroundColor(0xfff59563);
                break;
            case 32:
                numView.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                numView.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                numView.setBackgroundColor(0xffedcf72);
                break;
        }
        if (num>0) {
            numView.setText(num + "");
        }else if (num<=0){
            numView.setText("");
        }
    }

    private int num;


    public Card(Context context) {
        super(context);
        initCard();
    }
    private void initCard(){
        numView=new TextView(getContext());
        numView.setTextSize(50);
        numView.setGravity(Gravity.CENTER);
        LayoutParams P=new LayoutParams(-1,-1);
        P.setMargins(10,10,0,0);
        addView(numView,P);
    }

}
