package asssignment2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TrialVisualizationTool implements ActionListener {

	private JFrame frame;
	private JLabel lblStart, lblEnd, lblTrial, lblTime;
	private JButton btnPreviousTrial, btnNextTrial;
	
	private final String FILENAME = "trajectory_records.txt";
	private BufferedReader br;
	private File file;
	private int trials;
	private int count = 1;
	
	private HashMap<Integer, List<Point>> hm_tra = new HashMap<Integer, List<Point>>();
	private HashMap<Integer, Point> hm_end = new HashMap<Integer, Point>();
	private HashMap<Integer, Integer> hm_time = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> hm_width = new HashMap<Integer, Integer>();


	/**
	 * Launch the application.
	 */
	public void visualization() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrialVisualizationTool window = new TrialVisualizationTool();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrialVisualizationTool() {
		
		initialize();
		
		readFile();
		
		visualizeTrajectory();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setSize(1000, 770);
		
		btnPreviousTrial = new JButton("Previous Trial");
		btnPreviousTrial.setBounds(274, 680, 117, 50);
		frame.getContentPane().add(btnPreviousTrial);
		btnPreviousTrial.addActionListener(this);
		
		btnNextTrial = new JButton("Next Trial");
		btnNextTrial.setBounds(687, 680, 117, 50);
		frame.getContentPane().add(btnNextTrial);
		btnNextTrial.addActionListener(this);
		
		lblStart = new JLabel(" Start");
		lblStart.setBounds(481, 366, 38, 38);
		lblStart.setOpaque(true);
		lblStart.setBackground(Color.GRAY);
		
		lblTrial = new JLabel();
		lblTrial.setBounds(452, 29, 107, 16);
		
		lblTime = new JLabel();
		lblTime.setBounds(452, 57, 107, 16);
		
		lblEnd = new JLabel();
		
	}
	
	private void readFile(){
		
		file = new File(FILENAME);
		
		if(!file.exists()){
			JOptionPane.showMessageDialog(frame, "your trajectory file does not exist, please do test first!");
		}else{

			try{
				
				br = new BufferedReader(new FileReader(FILENAME));
				String currLine = "";
				
				while((currLine = br.readLine()) != null){
		        	
		        	String[] strings = currLine.split("\t");
		        	
		        	if(!strings[0].equals("SubjectID")){
		        		
		        		int trialNum = Integer.valueOf(strings[1]);
		        		
		        		if(!hm_tra.containsKey(trialNum)){
		        			
		        			hm_tra.put(trialNum, new ArrayList<Point>());
		        			
		        		}else{
		        			
		        			List<Point> list = hm_tra.get(trialNum);
		        			list.add(stringToPoint(strings[8]));
		        			
		        			hm_tra.put(trialNum, list);
		        			
		        		}
		        		
		        		if(!hm_end.containsKey(trialNum))
		        			hm_end.put(trialNum, stringToPoint(strings[5]));
		        		
		        		hm_time.put(trialNum, Integer.valueOf(strings[6]));
		        				        		
		        		hm_width.put(trialNum, Integer.valueOf(strings[3]));
		        		
		        	}
		        	
		        }
				
			}catch (Exception e){}
			
			trials = hm_tra.size();
			
		}
		
	}
	
	private Point stringToPoint(String string){
		
		String[] split = string.split(",");
		
		int x = Integer.valueOf(split[0].substring(1));
		int y = Integer.valueOf(split[1].substring(0, split[1].length() - 1));
		
		Point point = new Point(x, y);
		
		return point;
		
	}
	
	private void visualizeTrajectory(){
				
		if(count == 1)
			btnPreviousTrial.setEnabled(false);
		else
			btnPreviousTrial.setEnabled(true);
		
		if(count == trials - 1)
			btnNextTrial.setEnabled(false);
		else
			btnNextTrial.setEnabled(true);
		
		List<Point> trajectories = hm_tra.get(count);
		Point end = hm_end.get(count);
		int time = hm_time.get(count);
		int width = hm_width.get(count);
				
		lblTime.setText("Time: " + time);
		lblTrial.setText("Trial #: " + count);
		lblEnd.setBounds((int) end.getX(), (int) end.getY(), width, width);
		lblEnd.setOpaque(true);
		lblEnd.setBackground(Color.GREEN);
		lblEnd.setVisible(true);
				
		JPanel panel = new JPanel();
		
		JLayeredPane layeredPane = new JLayeredPane();
		
		layeredPane.setPreferredSize(new Dimension(1000, 680));
		
		panel.setBounds(0,0,1000,680);
		
		for(int i = 0; i < trajectories.size(); i++){
						
			JLabel label = new JLabel();
			label.setBounds((int) trajectories.get(i).getX(), (int) trajectories.get(i).getY(), 5, 5);
			label.setOpaque(true);
			label.setBackground(Color.RED);
			label.setVisible(true);
					
			layeredPane.add(label);
		}
				
		layeredPane.add(lblTime);
		layeredPane.add(lblTrial);
		layeredPane.add(lblStart);
		layeredPane.add(lblEnd);
		
		panel.add(layeredPane);
		frame.getContentPane().add(panel);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == btnPreviousTrial){
			
			count--;
						
			visualizeTrajectory();
			
		}
		
		if(e.getSource() == btnNextTrial){
			
			count++;
						
			visualizeTrajectory();
			
		}
		
	}
	
	

}
