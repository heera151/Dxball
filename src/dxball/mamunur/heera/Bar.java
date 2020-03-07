package dxball.mamunur.heera;

public class Bar {
	private float left;
	private float top;
	private float right;
	private float bottom;
	
	public Bar(float left, float top, float right, float bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
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
}
