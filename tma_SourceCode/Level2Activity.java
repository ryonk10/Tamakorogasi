package com.example.tmakorogasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.EventListener;

public class Level2Activity extends AppCompatActivity {

    private BallSurfaceViewLevel2 bsvLevel2;
    private Handler hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);
        SurfaceView svLevel2 =findViewById(R.id.surfaceView);
        bsvLevel2 = new BallSurfaceViewLevel2(svLevel2);
        hd=new Handler(new CheckConditionHandler());

    }

    class CheckConditionHandler implements Handler.Callback{

        @Override
        public boolean handleMessage(@NonNull Message message) {
            String condition=(String)message.obj;
            if(condition=="GameOver"){
                DialogFragment gameOverFragment = new GameOverWindowFragment(2);
                gameOverFragment.showNow(getSupportFragmentManager(), "GameOverWindowFragment");
            }else if(condition=="GameClear"){
                DialogFragment clearFragment = new ClearWindowFragment();
                clearFragment.showNow(getSupportFragmentManager(), "ClearWindowFragment");
            }
            return true;
        }
    }

    public void onClick_BackButton(View view) {
        Intent intentBack = new Intent(this, SettingActivity.class);
        startActivity(intentBack);
    }

    public void onClick_PauseButton(View view) {
        bsvLevel2.dx = 0;
        bsvLevel2.dy = 0;
        DialogFragment dialogFragment = new PauseWindowFragment();
        dialogFragment.showNow(getSupportFragmentManager(), "PauseWindowsFragment");
    }


    class BallSurfaceViewLevel2 implements View.OnTouchListener, SurfaceHolder.Callback, Runnable {
        private float x, y, speed = 8, masatu = 15, ballSize = 50;
        public double dx = 0, dy = 0, downX, downY, upX, upY;
        int screen_width, screen_height, screen_Top = 400;
        boolean isUpDirection, isLeftDirection;
        private Thread thread;
        private SurfaceHolder holder;
        private Paint paintBall = new Paint();
        private Paint paintWallBlack = new Paint();
        private Paint paintWallWhite = new Paint();


        BallSurfaceViewLevel2(SurfaceView svLevel2) {
            holder = svLevel2.getHolder();
            holder.addCallback(this);
            svLevel2.setOnTouchListener(this);

        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    upY = event.getY();
                    dx = upX - downX;
                    dy = upY - downY;
                    if (dx < 0) {
                        isLeftDirection = true;
                    } else {
                        isLeftDirection = false;
                    }
                    if (dy < 0) {
                        isUpDirection = true;
                    } else {
                        isUpDirection = false;
                    }
                    dx = Math.abs(dx);
                    dy = Math.abs(dy);
                    break;
            }
            return true;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            thread = new Thread(this);
            thread.start();
            paintBall.setColor(Color.BLUE);
            paintWallBlack.setColor(Color.DKGRAY);
            paintWallWhite.setColor(Color.LTGRAY);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            screen_width = width;
            screen_height = height;
            x = width / 2;
            y = height - 300;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            thread = null;
        }


        @Override
        public void run() {
            int blackSize = 300;
            int wallWidth = 50;
            int blackY = screen_Top;
            int boxHeight = 100;
            boolean isCollision = false;
            int[][] boxWall = {
                    //{LeftX,TopY,RightX,BottomY}
                    {screen_width/2, screen_height / 2, screen_width, screen_height / 2 + boxHeight},
                    {0, 0, screen_width*3/4, boxHeight},
                    {200, -screen_height / 2, screen_width, -200},
                    {0, -screen_height/2-boxHeight-150, screen_width-200, -screen_height/2-150},
                    {0, -screen_height*3/2, screen_width/2-150, -screen_height},
                    {screen_width / 2+150, -screen_height *3/2, screen_width, -screen_height},
                    {screen_width/4,-screen_height*2,screen_width*3/4,-screen_height *3/2-350},
                    //ゴール
                    {0, -screen_height * 2, screen_width, -screen_height * 2 + 200}};

            while (thread != null) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.WHITE);
                    canvas.drawCircle(x, y, ballSize, paintBall);

                    //壁作成
                    canvas.drawRect(0, 0, wallWidth, screen_height, paintWallWhite);
                    canvas.drawRect(screen_width - wallWidth, 0, screen_width, screen_height, paintWallWhite);

                    canvas.drawRect(0, blackY, wallWidth, blackY + blackSize, paintWallBlack);
                    canvas.drawRect(screen_width - wallWidth, blackY, screen_height, blackY + blackSize, paintWallBlack);
                    for (int i = 0; i < boxWall.length-1; i++) {
                        canvas.drawRect(boxWall[i][0], boxWall[i][1], boxWall[i][2], boxWall[i][3], paintWallBlack);
                    }
                    //ゴール
                    canvas.drawRect(boxWall[boxWall.length-1][0], boxWall[boxWall.length-1][1]-screen_height, boxWall[boxWall.length-1][2], boxWall[boxWall.length-1][3], paintWallWhite);

                    holder.unlockCanvasAndPost(canvas);

                    if (0 < dx) {
                        dx -= masatu;
                    } else {
                        dx = 0;
                    }
                    if (0 < dy) {
                        dy -= masatu;
                    } else {
                        dy = 0;
                    }
                    if (isLeftDirection) {
                        x -= dx / speed;
                    } else {
                        x += dx / speed;
                    }

                    //壁当たり判定
                    if (x - ballSize < wallWidth) {
                        dx = 0;
                        x = ballSize + wallWidth;
                    } else if (screen_width - wallWidth < x + ballSize) {
                        dx = 0;
                        x = screen_width - wallWidth - ballSize;
                    }

                    //Box当たり判定
                    for (int i = 0; i < boxWall.length; i++) {
                        if (boxWall[i][0] < x + ballSize && x - ballSize < boxWall[i][2] && boxWall[i][1] < y + ballSize && y - ballSize < boxWall[i][3]) {
                            isCollision = true;
                            dx = 0;
                            dy = 0;
                            //GameClear!!
                            if (i == boxWall.length-1) {
                                Message msg = Message.obtain();
                                msg.obj = "GameClear";
                                hd.sendMessage(msg);
                                y=-100;
                            }else {

                                if (isLeftDirection) {
                                    x += ballSize;
                                } else {
                                    x -= ballSize;
                                }
                                if (isUpDirection) {
                                    y += ballSize;
                                } else {
                                    y -= ballSize;
                                }
                            }
                            break;
                        }
                    }


                    //壁移動
                    if (screen_height < blackY) {

                        blackY = screen_Top - blackSize;
                    } else {
                        if (!isCollision) {
                            if (isUpDirection) {
                                for (int i = 0; i < boxWall.length; i++) {
                                    boxWall[i][1] += dy / speed;
                                    boxWall[i][3] += dy / speed;
                                }
                                blackY += dy / speed;
                            } else {
                                for (int i = 0; i < boxWall.length; i++) {
                                    boxWall[i][1] -= dy / speed;
                                    boxWall[i][3] -= dy / speed;
                                }
                                blackY -= dy / speed;
                            }
                        }
                    }
                    isCollision = false;
                    //GameOver...
                    if (screen_height<y) {
                        Message msg = Message.obtain();
                        msg.obj = "GameOver";
                        hd.sendMessage(msg);
                        y=-100;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
