package asssignment2;

import java.awt.Point;

public class Trajectory {
	
	public int id;
	public int trialNum;
	public int amplitude;
	public int width;
	public Point startPos;
	public Point targetPos;
	public long time;
	public int success;
	public Point cursorPos;
	
	public Trajectory(int id, int trialNum, int amplitude, int width, Point startPos, Point targetPos,
			long time, int success, Point cursorPos){
		
		this.id = id;
		this.trialNum = trialNum;
		this.amplitude = amplitude;
		this.width = width;
		this.startPos = startPos;
		this.targetPos = targetPos;
		this.time = time;
		this.success = success;
		this.cursorPos = cursorPos;
		
	}
	
	public String toString(){
		
		String string = id + "\t" + trialNum + "\t" + amplitude + "\t" + 
	width + "\t" + "(" + (int) startPos.getX() + "," + (int) startPos.getY() + ")" + 
	"\t" + "(" + (int) targetPos.getX() + "," + (int) targetPos.getY() + ")" + "\t" + 
	time + "\t" + success + "\t" + "(" + (int) cursorPos.getX() + "," + 
	(int) cursorPos.getY() + ")" + "\t";
		
		return string;
	}
	
	
}
