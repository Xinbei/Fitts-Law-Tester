package asssignment2;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;

public class MainStudyInterface implements MouseListener, ActionListener{

	//frame components
	private JFrame frame;
	private JLabel leftTrials, lblStart, lblEnd, lblThankYou, lblTime;
	public JButton btnExit, btnView;
	
	//target's positions and sizes
	private Point[] endPoints;
	private Dimension[] endDimension;
	
	private boolean isStart;
	private boolean firstSuccess = false;
	
	public int count = 0;
	private int totalTrials;
	
	//timer
	private long startTime;
	private long endTime;
	private long moveTime;
	
	public int success;
	private int failure = -1;
	
	private File trialRecord;
	private File trajectoryRecord;
	private BufferedWriter bw1;
	
	private int id;
	private int trialsPerCond;
	private int[] amps_extend;
	private int[] widths_extend;
	
	private Trajectory trajectory;
	private List<Point> cursorPosition = new ArrayList<Point>();
	private List<Long> moveTimes = new ArrayList<Long>();

	/**
	 * Launch the application.
	 */
	public void studyInterface(int id, int trialsPerCond, int[] amps, int[] widths) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainStudyInterface window = new MainStudyInterface(id, trialsPerCond, amps, widths);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void initializeFile(){
		
        trialRecord = new File("trial_records.txt");
        
        String header;
		
		if(!trialRecord.exists()){
			
			header = "SubjectID" + "\t" + "TrialNum" + "\t" + "Amplitude" + "\t"
					+ "Width" + "\t" + "StartPos" + "\t" + "TargetPos" + "\t" + "Time" + "\t" + "Success";
			
			try{
				
				bw1 = new BufferedWriter(new FileWriter(trialRecord, true));
				
				bw1.write(header);
				bw1.newLine();
				bw1.flush();
				
			}catch (Exception e){}
			
		}
		
        trajectoryRecord = new File("trajectory_records.txt");
        
        header = "SubjectID" + "\t" + "TrialNum" + "\t" + "Amplitude" + "\t"
				+ "Width" + "\t" + "StartPos" + "\t" + "TargetPos" + "\t" + "Time" + "\t" + 
				"Success" + "\t" + "CursorPos" + "\t";
		
		if(trajectoryRecord.exists()){
			
           try{
				
				PrintWriter pw = new PrintWriter("trajectory_records.txt");
				pw.close();
				
			}catch (Exception e){}
			
		}
		
		try{
			
			bw1 = new BufferedWriter(new FileWriter(trajectoryRecord, true));
			
			bw1.write(header);
			bw1.newLine();
			bw1.flush();
			
		}catch (Exception e){}
	}
	
	public MainStudyInterface(){}
	
	/**
	 * Create the application.
	 */
	
	/**
	 * @wbp.parser.constructor
	 */
	public MainStudyInterface(int id, int trialsPerCond, int[] amps, int[] widths) {
		
		this.id = id;
		this.trialsPerCond = trialsPerCond;
		
		initializeFile();
		
		totalTrials = trialsPerCond * amps.length * widths.length;
		
		initializeFrame(totalTrials);
		
		preparation(amps, widths);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame(int trials) {
		frame = new JFrame("Main Study Interface");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setSize(1000, 700);
		
		lblStart = new JLabel(" Start");
		lblStart.setOpaque(true);
		lblStart.setBackground(Color.GRAY);
		lblStart.setSize(38, 38);
		lblStart.setLocation((frame.getWidth() - lblStart.getWidth())/2, (frame.getHeight() - lblStart.getHeight())/2);
		frame.getContentPane().add(lblStart);
		lblStart.addMouseListener(this);
		
		leftTrials = new JLabel(trials + " trials left");
		leftTrials.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		leftTrials.setSize(136, 30);
		leftTrials.setLocation(28, 19);
		frame.getContentPane().add(leftTrials);

		lblEnd = new JLabel();
		lblEnd.setOpaque(true);
		lblEnd.setBackground(Color.GREEN);
		frame.getContentPane().add(lblEnd);
		lblEnd.addMouseListener(this);
		
		lblThankYou = new JLabel("Thank You For Your Participant!");
		lblThankYou.setFont(new Font("Lucida Grande", Font.PLAIN, 36));
		lblThankYou.setBounds(250, 138, 562, 230);
		lblThankYou.setVisible(false);
		frame.getContentPane().add(lblThankYou);
		
		lblTime = new JLabel();
		lblTime.setSize(100, 30);
		lblTime.setVisible(false);
		frame.getContentPane().add(lblTime);
		
		btnExit = new JButton("Exit");
		btnExit.setBounds(641, 483, 117, 40);
		btnExit.setVisible(false);
		frame.getContentPane().add(btnExit);
		btnExit.addActionListener(this);
		
		btnView = new JButton("View Your Trajectory");
		btnView.setBounds(278, 483, 154, 40);
		btnView.setVisible(false);
		frame.getContentPane().add(btnView);
		btnView.addActionListener(this);
		
		frame.setResizable(false);
		frame.addMouseMotionListener(new MyMouseMotionListener());
		frame.addMouseListener(this);
		
		isStart = false;
		
	}
	
	private void preparation(int[] amps, int[] widths){
		
		Point point_start = lblStart.getLocation();
		
		int x;
		int y;
		Dimension dimension;
		double randomAngle;
		
		endPoints = new Point[totalTrials];
		endDimension = new Dimension[totalTrials];
		
		amps_extend = new int[totalTrials];
		widths_extend = new int[totalTrials];
		
		Random random = new Random();
		
		int local_count = 0;
		
		for(int i = 0; i < amps.length; i++){
			for(int j = 0; j < widths.length; j++){
				
				int iterate = 0;
				
				while(iterate < trialsPerCond){
					
					amps_extend[local_count] = amps[i];
					widths_extend[local_count] = widths[i];
					
					randomAngle = Math.toRadians(random.nextInt(360));
					
					x = (int) (point_start.getX() + amps[i] * Math.cos(randomAngle) + widths[j] / 2);
					y = (int) (point_start.getY() + amps[i] * Math.sin(randomAngle) + widths[j] / 2);
					
					while(x > 1000 - widths[j] || y > 700 - widths[j] || x < widths[j] || y < widths[j]){
						
						randomAngle = Math.toRadians(random.nextInt(360));
						
						x = (int) (point_start.getX() + amps[i] * Math.cos(randomAngle) + widths[j] / 2);
						y = (int) (point_start.getY() + amps[i] * Math.sin(randomAngle) + widths[j] / 2);
						
					}
					
					dimension = new Dimension(widths[j], widths[j]);
					
					endPoints[local_count] = new Point(x - widths[j], y - widths[j]);
					endDimension[local_count] = dimension;
										
					iterate++;
					local_count++;
				}
			}
		}
		
		randomPermutation();
		
		lblEnd.setLocation(endPoints[count]);
		lblEnd.setSize(endDimension[count]);
		
	}
	
	private void randomPermutation(){
		
		Random random = new Random();
		int ran, temp;
		Point point_temp;
		Dimension dim_temp;
		
		for(int i = 0; i < endPoints.length; i++){
			
			ran = i + random.nextInt(endPoints.length - i);
			
			point_temp = endPoints[i];
			endPoints[i] = endPoints[ran];
			endPoints[ran] = point_temp;
						
			dim_temp = endDimension[i];
			endDimension[i] = endDimension[ran];
			endDimension[ran] = dim_temp;
			
			temp = amps_extend[i];
			amps_extend[i] = amps_extend[ran];
			amps_extend[ran] = temp;
			
			temp = widths_extend[i];
			widths_extend[i] = widths_extend[ran];
			widths_extend[ran] = temp;
			
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == lblStart && !isStart){
			
			startTime = System.currentTimeMillis();
			
			//change start button color and play sound
			lblStart.setBackground(Color.WHITE);
			lblStart.setForeground(Color.BLACK);
						
			isStart = true;
		}
		
		if(e.getSource() == lblEnd && isStart){
			
			if(failure < 1){
				failure = 0;
				success = 1;
			}
			
			firstSuccess = true;
									
            endTime = System.currentTimeMillis();
            
			writeToTrialRecordFile();
			
            try{
				
				bw1 = new BufferedWriter(new FileWriter(trajectoryRecord, true));
				
				for(int i = 0; i < cursorPosition.size(); i++){
					
					trajectory = new Trajectory(id, count + 1, amps_extend[count], widths_extend[count], lblStart.getLocation(),
							lblEnd.getLocation(), moveTimes.get(i), success, cursorPosition.get(i));
					
					bw1.write(trajectory.toString());
					bw1.newLine();
					bw1.flush();
					
				}
				
				trajectory = new Trajectory(id, count + 1, amps_extend[count], widths_extend[count], lblStart.getLocation(),
						lblEnd.getLocation(), endTime - startTime, success, lblEnd.getLocation());
				
				bw1.write(trajectory.toString());
				bw1.newLine();
				bw1.flush();
				
			}catch (Exception exception){}
            
            cursorPosition.clear();
			
			failure = 0;
			
			lblTime.setLocation((int) lblEnd.getLocation().getX() + lblEnd.getWidth(), 
					(int) lblEnd.getLocation().getY() + lblEnd.getWidth() / 2);
			lblTime.setText((endTime - startTime) + "ms");
			lblTime.setVisible(true);
			
			lblEnd.setBackground(Color.MAGENTA);
			
			new DisplayTime(500);
						
		}
		
		if(e.getSource() == frame && e.getSource() != lblEnd && isStart){
			
			if(firstSuccess){
				firstSuccess = false;
				success = 0;
			}
			
			failure++;			
		}
				
	}
	
	private void writeToTrialRecordFile(){
		
		String column = id + "\t" + (failure + 1) + "\t" + 
				amps_extend[count] + "\t" + widths_extend[count] + "\t" + "(" +
						lblStart.getLocation().getX() + ", " + lblStart.getLocation().getY() + ")" + "\t" +
				        "(" + lblEnd.getLocation().getX() + ", " + lblEnd.getLocation().getY() + ")" + "\t" +
						(endTime - startTime) + "\t" + success + "\t";

				
				try{
					
					bw1 = new BufferedWriter(new FileWriter(trialRecord, true));
					bw1.write(column);
					bw1.newLine();
					bw1.flush();
					
				}catch (Exception e){}

		
	}
	
	public class DisplayTime{
		Timer timer;
		
		public DisplayTime(int ms){
			
			timer = new Timer();
			timer.schedule(new DisplayTimeTask(), ms);
		}
		
		class DisplayTimeTask extends TimerTask{
			
			public void run(){
				
				if(totalTrials == 1){
					
					lblStart.setVisible(false);
					lblEnd.setVisible(false);
					
					leftTrials.setVisible(false);
					
					lblThankYou.setVisible(true);
					
					btnExit.setVisible(true);
					btnView.setVisible(true);
									
				}
				
				lblTime.setVisible(false);
				
				totalTrials--;
				
				leftTrials.setText(totalTrials + " trials left");
				
				lblStart.setBackground(Color.GRAY);
				lblStart.setForeground(Color.WHITE);
				
				isStart = false;
							
				count++;
				
				if(count < endPoints.length){
				
					lblEnd.setLocation(endPoints[count]);
					lblEnd.setSize(endDimension[count]);
					lblEnd.setBackground(Color.GREEN);
					
				}
								
				timer.cancel();
			}
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnExit)
			frame.dispose();
		
		if(e.getSource() == btnView){
			
			TrialVisualizationTool tv = new TrialVisualizationTool();
			tv.visualization();
			
			frame.dispose();
			
		}
		
	}
	
	private class MyMouseMotionListener implements MouseMotionListener{
		
		public void mouseDragged(MouseEvent e){
						
		}
		
		public void mouseMoved(MouseEvent e){
			
			if(isStart){
				moveTime = System.currentTimeMillis();
				
				moveTimes.add(moveTime - startTime);
				cursorPosition.add(e.getPoint());
			}			
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
