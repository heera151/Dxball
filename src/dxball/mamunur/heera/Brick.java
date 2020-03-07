package dxball.mamunur.heera;

public class Brick {
	private float left;
	private float top;
	private float right;
	private float bottom;
	private int color;
	private int brickLife;
	private int power;
	
	public Brick(float left, float top, float right, float bottom, int color, int brickLife, int power) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.color = color;
		this.brickLife = brickLife;
		this.power = power;
	}
	
	public void setLeft (float left)
	{
		this.left = left;
	}
	
	public void setTop (float top)
	{
		this.top = top;
	}
	
	public void setRight (float right)
	{
		this.right = right;
	}
	
	public void setBottom (float bottom)
	{
		this.bottom = bottom;
	}
	
	public void setColor (int color)
	{
		this.color = color;
	}
	
	public void setBrickLife (int brickLife)
	{
		this.brickLife = brickLife;
	}
	
	public void setPower (int power)
	{
		this.power = power;
	}
	
	public float getLeft()
	{
		return left;
	}
	
	public float getTop ()
	{
		return top;
	}
	
	public float getRight ()
	{
		return right;
	}
	
	public float getBottom ()
	{
		return bottom;
	}
	
	public int getColor ()
	{
		return color;
	}
	
	public int getBrickLife ()
	{
		return brickLife;
	}
	
	public int getPower ()
	{
		return power;
	}
}
