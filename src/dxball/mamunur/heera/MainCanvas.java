package dxball.mamunur.heera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainCanvas extends View {
	public final float positiveValue = (float) 0.70710678;
	public final float negativeValue = (float) -0.70710678;
	
	Paint paint;
	Thread mainBallThread;
	Handler mainBallHandler;
	Thread powerBallThread;
	Handler powerBallHandler;
	Thread loadBricksThread;
	Handler loadBricksHandler;
	
	Ball mainBall;
	Ball powerBall;
	Ball weakBall;
	Ball biggerBarBall;
	Bar bar;
	Brick brick;
	ArrayList<Brick> bricks = new ArrayList<Brick>();
	
	Boolean newLevel;
	Boolean gameOver;
	Boolean newLife;
	Boolean powerBallTaken;
	Boolean weakBallTaken;
	Boolean biggerBarBallTaken;
	Boolean isAllBricksLoaded;
	int score;
	int life;
	float canvasWidth;
	float canvasHeight;
	float touchX, touchY;
	long powerUpTime;
	long weakeenUpTime;
	long biggerBarUpTime;
	
	float barPartition;
	float mainBallX;
	float mainBallY;
	float mainBallR;
	float rightTopX;
	float rightTopY;
	float rightBottomX;
	float rightBottomY;
	float leftBottomX;
	float leftBottomY;
	float leftTopX;
	float leftTopY;
	
	
	
	public MainCanvas(Context context) {
		super(context);
		paint = new Paint();
		newLevel = true;
		newLife = true;
		gameOver = false;
		life = 3;
		score = 0;
		powerBallTaken = false;
		weakBallTaken = false;
		biggerBarBallTaken = false;
		isAllBricksLoaded = false;
		powerBall = new Ball(0, 0, 0, 0, 0, 1);
		weakBall = new Ball(0, 0, 0, 0, 0, 1);
		biggerBarBall = new Ball(0, 0, 0, 0, 0, 1);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (newLevel)
		{
			canvasWidth = (float) canvas.getWidth();
			canvasHeight = (float) canvas.getHeight();
			
			loadBricksThread = new Thread (new LoadBricksThread());
	        loadBricksThread.start();
	        loadBricksHandler = new Handler();
			newLevel = false;
		}
		
		if (newLife)
		{
			LoadBallAndBar ();
			newLife = false;
		}
		
		if (bricks.size() == 0 && isAllBricksLoaded)
		{
			newLevel = true;
			newLife = true;
			life = 3;
			if (MainActivity.levelSelect == 3)
				MainActivity.levelSelect = 1;
			else
				MainActivity.levelSelect += 1;
			
			if (MainActivity.clearedLevel < 3 )
				MainActivity.clearedLevel = MainActivity.levelSelect;
			
			isAllBricksLoaded = false;
		}
		
		// Backgrount to White
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
		
		// Ball Draw
		paint.setColor(Color.DKGRAY);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(mainBall.getX(), mainBall.getY(), mainBall.getR(), paint);
		
		// Text
		paint.setTextSize(30);
        paint.setFakeBoldText(true);
        canvas.drawText("Score: "+score, 20, 40, paint);
        
        paint.setTextSize(30);
        paint.setFakeBoldText(true);
        canvas.drawText("Life: "+life, canvas.getWidth()-110, 40, paint);

        paint.setTextSize(30);
        paint.setFakeBoldText(true);
        canvas.drawText("LEVEL "+MainActivity.levelSelect, canvas.getWidth()/2-60, 40, paint);
        
        // Bar
        paint.setColor(Color.GRAY);
        canvas.drawRect(bar.getLeft(), bar.getTop(), bar.getRight(), bar.getBottom(), paint);
        
        // Brick
        for (int i = 0; i < bricks.size(); i++)
        {
        	if (bricks.get(i).getColor() == 1)
        		paint.setColor(Color.BLUE);
        	else if (bricks.get(i).getColor() == 2)
        		paint.setColor(Color.GREEN);
        	else if (bricks.get(i).getColor() == 3)
        		paint.setColor(Color.MAGENTA);
        	else paint.setColor(Color.CYAN);
        	brick = bricks.get(i);
        	canvas.drawRect(brick.getLeft(), brick.getTop(), brick.getRight(), brick.getBottom(), paint);
        }
        
        // Check Game Over
        if(life == 0){
        	paint.setColor(Color.WHITE);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);   
            
            paint.setColor(Color.RED);
            paint.setTextSize(50);
            paint.setFakeBoldText(true);
            canvas.drawText("GAME OVER", canvasWidth / 2 - 150, canvasHeight / 2, paint);
            canvas.drawText("FINAL SCORE: "+score, canvasWidth / 2 - 195, canvasHeight / 2 + 60, paint);
            try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	((GameCanvas)getContext()).finish();
        }
        
        if (powerBall.getType() == 2)
        {
        	paint.setColor(Color.GREEN);
    		paint.setStyle(Paint.Style.FILL);
    		canvas.drawCircle(powerBall.getX(), powerBall.getY(), powerBall.getR(), paint);
        }
        
        if (weakBall.getType() == 3)
        {
        	paint.setColor(Color.RED);
    		paint.setStyle(Paint.Style.FILL);
    		canvas.drawCircle(weakBall.getX(), weakBall.getY(), weakBall.getR(), paint);
        }
        
        if (biggerBarBall.getType() == 4)
        {
        	paint.setColor(Color.GRAY);
    		paint.setStyle(Paint.Style.FILL);
    		canvas.drawCircle(biggerBarBall.getX(), biggerBarBall.getY(), biggerBarBall.getR(), paint);
        }
        
        mainBallThread = new Thread (new MainBallThread());
        mainBallThread.start();
        mainBallHandler = new Handler();
        
        powerBallThread = new Thread (new PowerBallThread());
        powerBallThread.start();
        powerBallHandler = new Handler();
        
        invalidate();
	}
	
	public void LoadBallAndBar ()
	{
		if (!biggerBarBallTaken)
			bar = new Bar((canvasWidth / 8) * 3, canvasHeight - 35, (canvasWidth / 8) * 5, canvasHeight - 5);
		else
			bar = new Bar(((canvasWidth / 8) * 3) - 50, canvasHeight - 35, ((canvasWidth / 8) * 5) + 50, canvasHeight - 5);
		
		if ((!powerBallTaken && !weakBallTaken) || (powerBallTaken && weakBallTaken))
			mainBall = new Ball(canvasWidth/2, canvasHeight - 50, 25, 4, 4, 1);
		else if (powerBallTaken)
			mainBall = new Ball(canvasWidth/2, canvasHeight - 70, 45, 4, 4, 1);
		else mainBall = new Ball(canvasWidth/2, canvasHeight - 40, 15, 4, 4, 1);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchX = event.getX();
		touchY = event.getY();
		
		if (touchX >= bar.getRight())
		{
			bar.setRight(bar.getRight() + 10);
			bar.setLeft(bar.getLeft() + 10);
			return true;
		}
		else if (touchX <= bar.getLeft())
		{
			bar.setRight(bar.getRight() - 10);
			bar.setLeft(bar.getLeft() - 10);
			return true;
		}
		else return true;
	}
	
	void CheckPowerHiddenInBrick (int i)
	{
		MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.collussion);
		mediaPlayer.start();
		
		if (bricks.get(i).getBrickLife() == 1)
		{
			bricks.get(i).setBrickLife(0);
			bricks.get(i).setColor(4);
		}
		else
		{
			if (bricks.get(i).getPower() != 1)
			{
				switch (bricks.get(i).getPower()) {
				case 2:
					powerBall.setX(bricks.get(i).getLeft() + 45);
					powerBall.setY(bricks.get(i).getBottom() - 25);
					powerBall.setR(25);
					powerBall.setDY(5);
					powerBall.setType(2);
					break;
				case 3:
					weakBall.setX(bricks.get(i).getLeft() + 45);
					weakBall.setY(bricks.get(i).getBottom() - 25);
					weakBall.setR(20);
					weakBall.setDY(5);
					weakBall.setType(3);
					break;
				default:
					biggerBarBall.setX(bricks.get(i).getLeft() + 45);
					biggerBarBall.setY(bricks.get(i).getBottom() - 25);
					biggerBarBall.setR(25);
					biggerBarBall.setDY(5);
					biggerBarBall.setType(4);
					break;
				}
			}
			bricks.remove(i);
		};
		score++;
	}
	
	class MainBallThread implements Runnable
    {
    	public void run() {
    		mainBallHandler.post(new Runnable() {
				
				public void run() {					
					// Ball Don't Hit Bar
					
					if((mainBall.getY() - mainBall.getR()) >= canvasHeight){
			            life--;
			            newLife = true;
			            MediaPlayer mediaPlayer_2 = MediaPlayer.create(getContext(), R.raw.die);
			    		mediaPlayer_2.start();
			            return;
			        }
			        
					// Ball Boundary Check
					
			        if((mainBall.getX() + mainBall.getR()) >= canvasWidth || (mainBall.getX() - mainBall.getR()) <= 0){
			        	mainBall.setDX(-mainBall.getDX());
			        }
			        if( (mainBall.getY() - mainBall.getR()) <= 0){
			        	mainBall.setDY(-mainBall.getDY());
			        }
			        
			        // Ball And Bricks Collision
			        
			        rightTopX = mainBall.getX() + (mainBall.getR() * positiveValue);
			        rightTopY = mainBall.getY() + (mainBall.getR() * positiveValue);
			        
			        leftTopX = mainBall.getX() + (mainBall.getR() * negativeValue);
			        leftTopY = mainBall.getY() + (mainBall.getR() * positiveValue);
			        
			        leftBottomX = mainBall.getX() + (mainBall.getR() * negativeValue);
			        leftBottomY = mainBall.getY() + (mainBall.getR() * negativeValue);
			        
			        rightBottomX = mainBall.getX() + (mainBall.getR() * positiveValue);
			        rightBottomY = mainBall.getY() + (mainBall.getR() * negativeValue);
			        
			        for (int i = 0; i < bricks.size(); i++)
					{
			        	if (rightTopX >= bricks.get(i).getLeft() && rightTopX <= bricks.get(i).getRight() && rightTopY >= bricks.get(i).getTop() && rightTopY <= bricks.get(i).getBottom())
			        	{
			        		CheckPowerHiddenInBrick (i);
			        		mainBall.setDX(-5);
			        		mainBall.setDY(5);
			        	}
			        	else if (leftTopX >= bricks.get(i).getLeft() && leftTopX <= bricks.get(i).getRight() && leftTopY >= bricks.get(i).getTop() && leftTopY <= bricks.get(i).getBottom())
			        	{
			        		CheckPowerHiddenInBrick (i);
			        		mainBall.setDX(5);
			        		mainBall.setDY(5);
			        	}
			        	else if (leftBottomX >= bricks.get(i).getLeft() && leftBottomX <= bricks.get(i).getRight() && leftBottomY >= bricks.get(i).getTop() && leftBottomY <= bricks.get(i).getBottom())
			        	{
			        		CheckPowerHiddenInBrick (i);
			        		mainBall.setDX(5);
			        		mainBall.setDY(-5);
			        	}
			        	else if (rightBottomX >= bricks.get(i).getLeft() && rightBottomX <= bricks.get(i).getRight() && rightBottomY >= bricks.get(i).getTop() && rightBottomY <= bricks.get(i).getBottom())
			        	{
			        		CheckPowerHiddenInBrick (i);
			        		mainBall.setDX(-5);
			        		mainBall.setDY(-5);
			        	}
			        	else if (mainBall.getY() - mainBall.getR() <= bricks.get(i).getBottom() && mainBall.getY() + mainBall.getR() >=bricks.get(i).getTop() && mainBall.getX() >= bricks.get(i).getLeft() && mainBall.getX() <= bricks.get(i).getRight())
						{
							CheckPowerHiddenInBrick (i);
							mainBall.setDY(-mainBall.getDY());
						}
						else if (mainBall.getX() - mainBall.getR() <= bricks.get(i).getRight() && mainBall.getX() + mainBall.getR() >= bricks.get(i).getLeft() && mainBall.getY() >= bricks.get(i).getTop() && mainBall.getY() <= bricks.get(i).getBottom())
						{
							CheckPowerHiddenInBrick (i);
							mainBall.setDX(-mainBall.getDX());
						}
						else {
							
						}
					}
			        
			        // Ball And Bar Collision Detect
			        
			        barPartition = (bar.getRight() - bar.getLeft()) / 5;
			        mainBallX = mainBall.getX();
			        mainBallY = mainBall.getY();
			        mainBallR = mainBall.getR();
			        
			        if ((mainBallX >= bar.getLeft() + (barPartition * 2)) && (mainBallX <= bar.getLeft() + (barPartition * 3)) && mainBallY + mainBallR >= bar.getTop())
			        {
			        	mainBall.setDX(0);
			        	mainBall.setDY(-5);
			        } 
			        else if ((mainBallX >= bar.getLeft() + (barPartition * 3)) && (mainBallX <= bar.getLeft() + (barPartition * 4)) && mainBallY + mainBallR >= bar.getTop())
			        {
			        	
			        	mainBall.setDX(5);
			        	mainBall.setDY(-5);
			        }
			        else if ((mainBallX >= bar.getLeft() + (barPartition * 4)) && (mainBallX <= bar.getLeft() + (barPartition * 5)) && mainBallY + mainBallR >= bar.getTop())
			        {
			        	
			        	mainBall.setDX(7);
			        	mainBall.setDY(-5);
			        }
			        else if ((mainBallX >= bar.getLeft() + (barPartition * 0)) && (mainBallX <= bar.getLeft() + (barPartition * 1)) && mainBallY + mainBallR >= bar.getTop())
			        {
			        	
			        	mainBall.setDX(-7);
			        	mainBall.setDY(-5);
			        }
			        else if ((mainBallX >= bar.getLeft() + (barPartition * 1)) && (mainBallX <= bar.getLeft() + (barPartition * 2)) && mainBallY + mainBallR >= bar.getTop())
			        {
			        	
			        	mainBall.setDX(-5);
			        	mainBall.setDY(-5);
			        }
			        else if (leftBottomX >= bar.getLeft() && leftBottomX <= bar.getRight() && leftBottomY >= bar.getTop() && leftBottomY <= bar.getBottom())
		        	{
		        		mainBall.setDX(9);
		        		mainBall.setDY(-5);
		        	}
		        	else if (rightBottomX >= bar.getLeft() && rightBottomX <= bar.getRight() && rightBottomY >= bar.getTop() && rightBottomY <= bar.getBottom())
		        	{
		        		mainBall.setDX(-9);
		        		mainBall.setDY(-5);
		        	}
			        else
			        {
			        	
					}

			        // Ball Movement
			        
			        mainBall.setX(mainBall.getX() + mainBall.getDX());
					mainBall.setY(mainBall.getY() + mainBall.getDY()); 
				}
			});
    	}
    }
	
	class PowerBallThread implements Runnable
    {
    	public void run() {
    		powerBallHandler.post(new Runnable() {
				
				public void run() {
					
					if (powerBallTaken && System.currentTimeMillis() >= powerUpTime + 15000)
					{
						mainBall.setR(25);
						powerBallTaken = false;
					}
					
					if (weakBallTaken && System.currentTimeMillis() >= weakeenUpTime + 15000)
					{
						mainBall.setR(25);
						weakBallTaken = false;
					}
					
					if (biggerBarBallTaken && System.currentTimeMillis() >= biggerBarUpTime + 15000)
					{
						bar.setLeft(bar.getLeft()+ 50);
						bar.setRight(bar.getRight() - 50);
						biggerBarBallTaken = false;
					}
					
					if (powerBall.getType() == 2)
					{
						if (!powerBallTaken && powerBall.getY() - powerBall.getR() <= bar.getBottom() && powerBall.getY() + powerBall.getR() >= bar.getTop() && powerBall.getX() >= bar.getLeft() && powerBall.getX() <= bar.getRight())
						{
							powerBallTaken = true;
							powerUpTime = System.currentTimeMillis();
							powerBall.setType(1);
							mainBall.setR(45);
						}
						
						powerBall.setY(powerBall.getY() + powerBall.getDY());
					}

					if (weakBall.getType() == 3)
					{
						if (!weakBallTaken && weakBall.getY() - weakBall.getR() <= bar.getBottom() && weakBall.getY() + weakBall.getR() >= bar.getTop() && weakBall.getX() >= bar.getLeft() && weakBall.getX() <= bar.getRight())
						{
							weakBallTaken = true;
							weakeenUpTime = System.currentTimeMillis();
							weakBall.setType(1);
							mainBall.setR(15);
						}
						
						weakBall.setY(weakBall.getY() + weakBall.getDY());
					}

					if (biggerBarBall.getType() == 4)
					{
						if (!biggerBarBallTaken && biggerBarBall.getY() - biggerBarBall.getR() <= bar.getBottom() && biggerBarBall.getY() + biggerBarBall.getR() >= bar.getTop() && biggerBarBall.getX() >= bar.getLeft() && biggerBarBall.getX() <= bar.getRight())
						{
							biggerBarBallTaken = true;
							biggerBarUpTime = System.currentTimeMillis();
							biggerBarBall.setType(1);
							bar.setLeft(bar.getLeft()- 50);
							bar.setRight(bar.getRight() + 50);
						}
						
						biggerBarBall.setY(biggerBarBall.getY() + biggerBarBall.getDY());
					}
				}
			});
    	}
    }
	
	class LoadBricksThread implements Runnable
    {
    	public void run() {
    		loadBricksHandler.post(new Runnable() {
				
				public void run() {					
					
					float left = 0, top = 0, right = 0, bottom = 0;
					int color = 0, brickLife = 0;
					int [] power = new int [4];
					int [] powerAssign = new int [4];
					Random rand = new Random();
					for (int i = 0; i < 4; i++)
					{
						power[i] = rand.nextInt(3) + 2;
						
						if (MainActivity.levelSelect == 1)
							powerAssign[i] = rand.nextInt(23) + 1;
						else if (MainActivity.levelSelect == 2)
							powerAssign[i] = rand.nextInt(42) + 1;
						else powerAssign[i] = rand.nextInt(58) + 1;
					}
					
					BufferedReader reader = null;
					AssetManager assetManager = getResources().getAssets();
					
					try {
						if (MainActivity.levelSelect == 1)
							reader = new BufferedReader (new InputStreamReader(assetManager.open("Level1.txt")));
						else if (MainActivity.levelSelect == 2)
							reader = new BufferedReader (new InputStreamReader(assetManager.open("Level2.txt")));
						else reader = new BufferedReader (new InputStreamReader(assetManager.open("Level3.txt")));
						
					    String mLine;
					    int i = 0, j = 0, k = 0, l = 0;
					    while ((mLine = reader.readLine()) != null)
					    {
					       switch (i)
					       {
						       case 0: left = Float.parseFloat(mLine); break;
						       case 1: top = Float.parseFloat(mLine); break;
						       case 2: right = Float.parseFloat(mLine); break;
						       case 3: bottom = Float.parseFloat(mLine); break;
						       case 4: color = Integer.parseInt(mLine); break;
						       case 5: brickLife = Integer.parseInt(mLine); break;
						       default: break;
					       }

					       i++;
					       if (i==6)
					       {
						       j++;
					    	   if (brickLife == 1)
					    	   {
					    		   color = 3;
					    	   }
					    	   if (powerAssign[0] == j)
					    	   {
					    		   bricks.add(new Brick(left, top, right, bottom, color, brickLife, power[k++]));
					    	   }
					    	   else if (powerAssign[1] == j)
					    	   {
					    		   bricks.add(new Brick(left, top, right, bottom, color, brickLife, power[k++]));
					    	   }
					    	   else if (powerAssign[2] == j)
					    	   {
					    		   bricks.add(new Brick(left, top, right, bottom, color, brickLife, power[k++]));
					    	   }
					    	   else if (powerAssign[3] == j)
					    	   {
					    		   bricks.add(new Brick(left, top, right, bottom, color, brickLife, power[k++]));
					    	   }
					    	   else
					    	   {
					    		   bricks.add(new Brick(left, top, right, bottom, color, brickLife, 1));
					    	   }
					    	   
					    	   i = 0;
					    	   l++;
					       }  
					    }
					    isAllBricksLoaded = true;
					}
					catch (IOException e) {
						
					}
					finally {
					    if (reader != null) {
					         try {
					             reader.close();
					         }
					         catch (IOException e) {

					         }
					    }
					}
				}
			});
    	}
    }
}