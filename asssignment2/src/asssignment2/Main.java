package asssignment2;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnVisualization = new JButton("Visualization");
		btnVisualization.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TrialVisualizationTool tv = new TrialVisualizationTool();
				tv.visualization();
				
				frame.dispose();
				
			}
		});
		btnVisualization.setBounds(67, 117, 117, 40);
		frame.getContentPane().add(btnVisualization);
		
		JButton btnConfiguration = new JButton("Configuration");
		btnConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ConfigurationFrame cf = new ConfigurationFrame();
				cf.configuration();
				
				frame.dispose();
				
			}
		});
		btnConfiguration.setBounds(250, 117, 117, 40);
		frame.getContentPane().add(btnConfiguration);
		
		JLabel lblWelcome = new JLabel("Welcome to Fitts' Law Tester, Please Select");
		lblWelcome.setBounds(81, 55, 272, 16);
		frame.getContentPane().add(lblWelcome);
		
		frame.setTitle("Fitts' Law Tester");
		frame.setResizable(false);
	}

}
