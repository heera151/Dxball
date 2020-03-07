package dxball.mamunur.heera;

public class Ball {
	private float x;
	private float y;
	private float r;
	private float dx;
	private float dy;
	private int type;
	
	public Ball(float x, float y, float r, float dx, float dy, int type) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.dx = dx;
		this.dy = dy;
		this.type = type;
	}
	
	public void setX (float x)
	{
		this.x = x;
	}
	
	public void setY (float y)
	{
		this.y = y;
	}
	
	public void setR (float r)
	{
		this.r = r;
	}
	
	public void setDX (float dx)
	{
		this.dx = dx;
	}
	
	public void setDY (float dy)
	{
		this.dy = dy;
	}
	
	public void setType (int type)
	{
		this.type = type;
	}
	
	public float getX ()
	{
		return x;
	}
	
	public float getY ()
	{
		return y;
	}
	
	public float getR ()
	{
		return r;
	}
	
	public float getDX ()
	{
		return dx;
	}
	
	public float getDY ()
	{
		return dy;
	}
	
	public int getType ()
	{
		return type;
	}
}