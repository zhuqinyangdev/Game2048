package com.example.game2048;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQY on 2017/8/7.
 */

public class GameView extends LinearLayout{

    private SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
    private SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getContext());

    private LinearLayout scorelayout=new LinearLayout(getContext());
    private TextView scoreView=new TextView(getContext());
    private TextView maxScoreView=new TextView(getContext());
    private Button start_Game=new Button(getContext());
    private int score=0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

     public GameView(Context context) {
         super(context);

         initView();
     }
    private void initView(){
        setBackgroundResource(R.drawable.ic_gamebg);
        if(sharedPreferences.getInt("MaxScore",-1)==-1){
            editor.putInt("MaxScore",0);
            editor.apply();
        }
        scorelayout.setOrientation(HORIZONTAL);
        setOrientation(VERTICAL);
        /*RelativeLayout.LayoutParams Params=new LayoutParams(getLayoutParams());
        Params.setMargins(10,30,400,20);*/
        scoreView.setText("得分:0");
        scoreView.setTextSize(18);
        maxScoreView.setTextSize(18);
        maxScoreView.setText("最高分:"+sharedPreferences.getInt("MaxScore",-1));
        scorelayout.setBackgroundColor(0xffeee4da);
        scorelayout.addView(scoreView,300,ViewGroup.LayoutParams.WRAP_CONTENT);
        scorelayout.addView(maxScoreView,350,ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(scorelayout,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        GameMain gameMain=new GameMain(getContext());
        addView(gameMain,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        start_Game.setText("New Game");
        start_Game.setAllCaps(false);
        start_Game.setBackgroundColor(0xffeee4da);
        scorelayout.addView(start_Game,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public class GameMain extends GridLayout{

        private static final int N=4;

        private static final  String TAG="GameView";

        public List<Card> emptyList;
        private  List<Card> cardsList;

        private int mWidth;

        public GameMain(Context context, AttributeSet attrs) {
            super(context, attrs);
            initGameView();
        }
        public GameMain(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initGameView();
        }
        public GameMain(Context context) {
            super(context);
            initGameView();
        }
        private void initGameView(){
            start_Game.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startGame();
                }
            });
            setColumnCount(4);
            setBackgroundColor(0xffbbada0);
            cardsList=new ArrayList<Card>();
            emptyList=new ArrayList<Card>();
            DisplayMetrics dm2 = getResources().getDisplayMetrics();
            mWidth=(dm2.widthPixels)/4;
            addCards();
            if (sharedPreferences.getString("record",null)!=null){
                read();
            }
            this.setOnTouchListener(new OnTouchListener() {
                private float xStart,yStart,xEnd,yEnd;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            xStart=motionEvent.getX();
                            yStart=motionEvent.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            xEnd=motionEvent.getX();
                            yEnd=motionEvent.getY();
                            float xOffset,yOffset;
                            xOffset=xStart-xEnd;
                            yOffset=yStart-yEnd;
                            if(Math.abs(xOffset)>Math.abs(yOffset)){
                                if(xOffset>5){       //向左移
                                    moveLeft();
                                }else if (xOffset<-5){      //向右移
                                    moveRight();
                                }
                            }else{
                                if(yOffset>5){       //向上移
                                    moveUp();
                                }else if (yOffset<-5){      //向下移
                                    moveDown();
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }

            });
        }
        private void zero(int []num){
            for (int i=0;i<N;i++){
                num[i]=0;
            }
        }
        private void moveLeft(){
            int []num=new int[N];
            int n,size;   //n为num当前可储存的位置，size为数字的大小
            boolean move=false;   //判断是否移动过
            String beforemove=null,afterremove=null;
            for (int i=0;i<N;i++){
                n=0;
                for (int j=0;j<N;j++){
                    if ((size=cardsList.get(i*N+j).getNum())!=0){
                        num[n]=size;
                        emptyList.add(cardsList.get(i*N+j));
                        n++;
                    }
                    beforemove+=""+size;
                    cardsList.get(i*N+j).setNum(0);
                }
                for (int x=0;x<N;x++){
                    if (num[x]!=0&&x<3){
                        if (num[x]==num[x+1]){
                            num[x]=2*num[x+1];
                            score+=2*num[x+1];
                            scoreView.setText("得分:"+score);
                            maxScore();
                            num[x+1]=0;
                            x++;
                        }
                    }else{
                        break;
                    }
                }
                int f=0;//进行写操作
                for (int nn:num){
                    if (nn!=0){
                        cardsList.get(i*N+f).setNum(nn);
                        emptyList.remove(cardsList.get(i*N+f));
                        f++;
                    }
                }
                for (int x=0;x<N;x++){
                    afterremove+=""+cardsList.get(i*N+x).getNum();
                }
                zero(num);
                if (!afterremove.equals(beforemove)){
                    move=true;
                }
            }
            if (move){
                addNum();
                Log();
            }
            finishcheck();
            save();
        }

        private void moveRight(){
            int []num=new int[N];
            int n,size;   //n为num当前可储存的位置，size为数字的大小
            boolean move=false;   //判断是否移动过
            String beforemove=null,afterremove=null;
            for (int i=0;i<N;i++){
                n=0;
                for (int j=0;j<N;j++){
                    if ((size=cardsList.get(i*N+j).getNum())!=0){
                        num[n]=size;
                        emptyList.add(cardsList.get(i*N+j));
                        n++;
                    }
                    beforemove+=""+size;
                    cardsList.get(i*N+j).setNum(0);
                }
                for (int x=N-1;x>=0;x--){
                    if (num[x]!=0&&x>0){
                        if (num[x]==num[x-1]){
                            num[x]=2*num[x-1];
                            score+=2*num[x-1];
                            scoreView.setText("得分:"+score);
                            maxScore();
                            num[x-1]=0;
                            x--;
                        }
                    }
                }
                int f=N-1;//进行写操作
                for (int x=N-1;x>=0;x--){
                    int nn=num[x];
                    if (nn!=0){
                        cardsList.get(i*N+f).setNum(nn);
                        emptyList.remove(cardsList.get(i*N+f));
                        f--;
                    }
                }
                for (int x=0;x<N;x++){
                    afterremove+=""+cardsList.get(i*N+x).getNum();
                }
                zero(num);
                if (!afterremove.equals(beforemove)){
                    move=true;
                }
            }
            if (move){
                addNum();
                Log();
            }
            finishcheck();
            save();
        }
        private void moveUp(){
            int []num=new int[N];
            int n,size;   //n为num当前可储存的位置，size为数字的大小
            boolean move=false;   //判断是否移动过
            String beforemove=null,afterremove=null;
            for (int j=0;j<N;j++){
                n=0;
                for (int i=0;i<N;i++){
                    if ((size=cardsList.get(i*N+j).getNum())!=0){
                        num[n]=size;
                        emptyList.add(cardsList.get(i*N+j));
                        n++;
                    }
                    beforemove+=""+size;
                    cardsList.get(i*N+j).setNum(0);
                }
                for (int x=0;x<N;x++){
                    if (num[x]!=0&&x<3){
                        if (num[x]==num[x+1]){
                            num[x]=2*num[x+1];
                            score+=2*num[x+1];
                            scoreView.setText("得分:"+score);
                            maxScore();
                            num[x+1]=0;
                            x++;
                        }
                    }else{
                        break;
                    }
                }
                int f=0;//进行写操作
                for (int nn:num){
                    if (nn!=0){
                        cardsList.get(f*N+j).setNum(nn);
                        emptyList.remove(cardsList.get(f*N+j));
                        f++;
                    }
                }
                for (int x=0;x<N;x++){
                    afterremove+=""+cardsList.get(x*N+j).getNum();
                }
                zero(num);
                if (!afterremove.equals(beforemove)){
                    move=true;
                }
            }
            if (move){
                addNum();
                Log();
            }
            finishcheck();
            save();
        }
        private void moveDown(){
            int []num=new int[N];
            int n,size;   //n为num当前可储存的位置，size为数字的大小
            boolean move=false;   //判断是否移动过
            String beforemove=null,afterremove=null;
            for (int j=0;j<N;j++){
                n=0;
                for (int i=0;i<N;i++){
                    if ((size=cardsList.get(i*N+j).getNum())!=0){
                        num[n]=size;
                        emptyList.add(cardsList.get(i*N+j));
                        n++;
                    }
                    beforemove+=""+size;
                    cardsList.get(i*N+j).setNum(0);
                }
                for (int x=N-1;x>=0;x--){
                    if (num[x]!=0&&x>0){
                        if (num[x]==num[x-1]){
                            num[x]=2*num[x-1];
                            score+=2*num[x-1];
                            scoreView.setText("得分:"+score);
                            maxScore();
                            num[x-1]=0;
                            x--;
                        }
                    }
                }
                int f=N-1;//进行写操作
                for (int x=N-1;x>=0;x--){
                    int nn=num[x];
                    if (nn!=0){
                        cardsList.get(f*N+j).setNum(nn);
                        emptyList.remove(cardsList.get(f*N+j));
                        f--;
                    }
                }
                for (int x=0;x<N;x++){
                    afterremove+=""+cardsList.get(x*N+j).getNum();
                }
                zero(num);
                if (!afterremove.equals(beforemove)){
                    move=true;
                }
            }
            if (move){
                addNum();
                Log();
            }
            finishcheck();
            save();
        }
        private void addNum(){
            if (emptyList.size()!=0) {
                int index = (int) (Math.random() * emptyList.size());
                if (Math.random() > 0.1) {
                    emptyList.get(index).setNum(2);
                } else {
                    emptyList.get(index).setNum(4);
                }
                emptyList.remove(index);
            }
        }

        /**
         * 添加卡片
         */
        private void addCards(){

            for(int i=0;i<4;i++){
                for (int j=0;j<4;j++){
                    Card card=new Card(getContext());
                    card.setNum(0);
                    card.setCardId(i*4+j+1);
                    this.addView(card,mWidth,mWidth);
                    emptyList.add(card);
                    cardsList.add(card);
                }
            }
        }
        /**
         * 开始新游戏游戏方法
         */
        public void startGame(){
            emptyList.clear();
            for (Card card: cardsList){
                if(card.getNum()!=0){
                    card.setNum(0);
                }
            }
            emptyList.addAll(cardsList);
            addNum();addNum();
            score=0;
            scoreView.setText("0");
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            addNum();
            addNum();
        }

        /**
         * 检查游戏是否结束
         */
        private boolean y=false;
        public void  digui(int i,int j,int num1){
            if(y)return;
            if(i==4||j==4)return;
            int num2=cardsList.get(i*4+j).getNum();
            if((i!=0||j!=0)&&num2==num1){y=true;return;}
            digui(i,j+1,num2);
            digui(i+1,j,num2);
            //i--;
        }
        private void finishcheck(){
            if(emptyList.size()==0){
                digui(0,0,cardsList.get(0).getNum());
                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext())
                        .setTitle("提示:")
                        .setMessage("你已经输了，去找你的男朋友吧！")
                        .setCancelable(true)
                        .setPositiveButton("重新开始不去找", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startGame();
                            }
                        })
                        .setNegativeButton("不玩了去找他了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.activityList.get(0).finish();
                            }
                        });
                if (!y){
                    dialog.show();
                }

            }
            y=false;
        }
        private void Log(){
            Log.d(TAG,""+emptyList.size());
        }
        private void read(){
            String text=sharedPreferences.getString("record",null);
            if (text!=null){
                String []num=text.split(" ");
                for (int i=0;i<N*N;i++){
                    cardsList.get(i).setNum(Integer.valueOf(num[i]));
                    if (cardsList.get(i).getNum()==0){
                        emptyList.add(cardsList.get(i));
                    }
                }
            }
        }
        private void save(){
            String num="";
            for (Card card:cardsList){
                num+=card.getNum()+" ";
            }
            editor.putString("record",num);
            editor.apply();
        }
        private void maxScore(){
            int maxScore=sharedPreferences.getInt("MaxScore",-1);
            Log.d(TAG,"maxScore:"+maxScore);
            if (score>maxScore&&maxScore!=-1){
                editor.putInt("MaxScore",score);
                editor.apply();
                maxScoreView.setText("最高分:"+score);
            }

        }
    }


}
