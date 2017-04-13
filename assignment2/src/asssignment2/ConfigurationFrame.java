package asssignment2;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.Color;

public class ConfigurationFrame implements ActionListener, ChangeListener{

	private JFrame frame;
	private JLabel lblIndices, lblTotalTrails;
	private JSpinner spinner_id, spinner_trials, spinner_amp, spinner_width;
	private JButton btn_add_amp, btn_add_width, btn_rem_amp, btn_rem_width;
	private JButton btnOk, btnCancel;
	private JList amplitude, width, indices;
	
	private DefaultListModel amp_model, width_model, index_model;
	private ListModel amp_list_model, width_list_model;
	
	private DecimalFormat format = new DecimalFormat("#0.00");

	/**
	 * Launch the application.
	 */
	public void configuration() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigurationFrame window = new ConfigurationFrame();
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
	public ConfigurationFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Configuration Frame");
		frame.getContentPane().setEnabled(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setSize(440,420);
		
		JLabel lblSubjectId = new JLabel("Subject ID:");
		lblSubjectId.setBounds(16, 29, 70, 16);
		frame.getContentPane().add(lblSubjectId);
		
		spinner_id = new JSpinner();
		spinner_id.setBounds(89, 24, 70, 26);
		SpinnerModel sm0 = new SpinnerNumberModel(1, 1, 1000, 1);
		spinner_id.setModel(sm0);
		frame.getContentPane().add(spinner_id);
		
		JLabel lblTrialsPer = new JLabel("Trials per condition:");
		lblTrialsPer.setBounds(16, 62, 143, 16);
		frame.getContentPane().add(lblTrialsPer);
		
		spinner_trials = new JSpinner();
		spinner_trials.setBounds(152, 57, 70, 26);
		SpinnerModel sm1 = new SpinnerNumberModel(1, 1, 500, 1);
		spinner_trials.setModel(sm1);
		frame.getContentPane().add(spinner_trials);
		spinner_trials.addChangeListener(this);
		
		JLabel lblAmplitude = new JLabel("Amplitudes: (pixels)");
		lblAmplitude.setBounds(16, 96, 143, 16);
		frame.getContentPane().add(lblAmplitude);
		
		spinner_amp = new JSpinner();
		spinner_amp.setBounds(16, 116, 70, 26);
		SpinnerModel sm2 = new SpinnerNumberModel(1, 1, 1200, 1);
		spinner_amp.setModel(sm2);
		frame.getContentPane().add(spinner_amp);
		
		JLabel lblWidthspixels = new JLabel("Widths: (pixels)");
		lblWidthspixels.setBounds(159, 96, 97, 16);
		frame.getContentPane().add(lblWidthspixels);
		
		spinner_width = new JSpinner();
		spinner_width.setBounds(152, 116, 70, 26);
		SpinnerModel sm3 = new SpinnerNumberModel(1, 1, 1200, 1);
		spinner_width.setModel(sm3);
		frame.getContentPane().add(spinner_width);
		
		JLabel lblIndexsOfDifficult = new JLabel("Indices of Difficulty");
		lblIndexsOfDifficult.setBounds(284, 96, 134, 16);
		frame.getContentPane().add(lblIndexsOfDifficult);
		
		lblIndices = new JLabel("(0 indices): ");
		lblIndices.setBounds(294, 121, 84, 16);
		frame.getContentPane().add(lblIndices);
		
		btnOk = new JButton("OK");
		btnOk.setForeground(Color.DARK_GRAY);
		btnOk.setBackground(Color.LIGHT_GRAY);
		btnOk.setBounds(192, 348, 90, 40);
		frame.getContentPane().add(btnOk);
		btnOk.addActionListener(this);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setForeground(Color.DARK_GRAY);
		btnCancel.setBackground(Color.LIGHT_GRAY);
		btnCancel.setBounds(294, 348, 90, 40);
		frame.getContentPane().add(btnCancel);
		btnCancel.addActionListener(this);
		
		lblTotalTrails = new JLabel("Total trials: 0");
		lblTotalTrails.setBounds(20, 359, 117, 16);
		frame.getContentPane().add(lblTotalTrails);
		
		btn_add_amp = new JButton("+");
		btn_add_amp.setBackground(Color.WHITE);
		btn_add_amp.setBounds(90, 118, 20, 22);
		frame.getContentPane().add(btn_add_amp);
		btn_add_amp.addActionListener(this);
		
		btn_add_width = new JButton("+");
		btn_add_width.setBackground(Color.WHITE);
		btn_add_width.setBounds(227, 119, 20, 22);
		frame.getContentPane().add(btn_add_width);
		btn_add_width.addActionListener(this);
		
		btn_rem_amp = new JButton("-");
		btn_rem_amp.setBackground(Color.WHITE);
		btn_rem_amp.setBounds(117, 118, 20, 22);
		frame.getContentPane().add(btn_rem_amp);
		btn_rem_amp.addActionListener(this);
		
		btn_rem_width = new JButton("-");
		btn_rem_width.setBackground(Color.WHITE);
		btn_rem_width.setBounds(254, 119, 20, 22);
		frame.getContentPane().add(btn_rem_width);
		btn_rem_width.addActionListener(this);
		
		amplitude = new JList();
		amp_model = new DefaultListModel();
		amplitude.setModel(amp_model);
		amp_model.addListDataListener(new MyListDataListener());
		amp_model.removeListDataListener(new MyListDataListener());
		
		width = new JList();
		width_model = new DefaultListModel();
		width_model.addListDataListener(new MyListDataListener());
		width_model.removeListDataListener(new MyListDataListener());
		width.setModel(width_model);
		
		indices = new JList();
		index_model = new DefaultListModel();
		indices.setModel(index_model);
		
		JScrollPane scrollPane = new JScrollPane(amplitude, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(14, 148, 128, 195);
		frame.getContentPane().add(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane(width, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1.setBounds(152, 148, 128, 195);
		frame.getContentPane().add(scrollPane_1);
		
		JScrollPane scrollPane_2 = new JScrollPane(indices, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_2.setBounds(288, 149, 128, 195);
		frame.getContentPane().add(scrollPane_2);
		
		frame.setResizable(false);
	}
	
	class MyListDataListener implements ListDataListener{
		
		public void contentsChanged(ListDataEvent e){
						
		}
		
		public void intervalAdded(ListDataEvent e){
			
			indexUpdate();
			
		}
		
		public void intervalRemoved(ListDataEvent e){
			
			indexUpdate();
			
		}
		
	};
	
	private void indexUpdate(){
		
		index_model.removeAllElements();
		
		HashSet set = new HashSet();
		
		amp_list_model = amplitude.getModel();
		width_list_model = width.getModel();
		
		int amp_len = amp_list_model.getSize();
		int width_len = width_list_model.getSize();
		
		for(int i = 0; i < amp_len; i++){
			for(int j = 0; j < width_len; j++){
				int a = (int) amp_list_model.getElementAt(i);
				int w = (int) width_list_model.getElementAt(j);
				
				double d = Math.log(2*a/w)/Math.log(2);
				
				if(!set.contains(format.format(d)))
					set.add(format.format(d));
				
			}
		}
		
		lblIndices.setText("(" + set.size() + " unique):");
		lblTotalTrails.setText("Total trials: " + (int) spinner_trials.getValue() * amp_len * width_len);
		
		double[] array = new double[set.size()];
		Iterator iterator = set.iterator();
		int count = 0;
		
		while(iterator.hasNext()){
			array[count] = Double.parseDouble((String) iterator.next());
			count++;
		}
		
		Arrays.sort(array);
		
		for(int i = 0; i < array.length; i++){
			index_model.addElement(array[i]);
		}
	}
	
	public void actionPerformed(ActionEvent e){
		
		int selectedIndex;
		
		if(e.getSource() == btn_add_amp){
			
			if((int) spinner_amp.getValue() > 600)
				JOptionPane.showMessageDialog(frame, "amplitude is too large, please set a number smaller than 600");
			else
				amp_model.addElement(spinner_amp.getValue());
			
		}
		
		if(e.getSource() == btn_add_width){
			
			if((int) spinner_width.getValue() > 300)
				JOptionPane.showMessageDialog(frame, "width is too large, please set a number smaller than 300");
			else
				width_model.addElement(spinner_width.getValue());
		}
		
		if(e.getSource() == btn_rem_amp){
			selectedIndex = amplitude.getSelectedIndex();
			
			if(selectedIndex != -1){
				amp_model.remove(selectedIndex);
			}
		}
		
		if(e.getSource() == btn_rem_width){
			selectedIndex = width.getSelectedIndex();
			
			if(selectedIndex != -1){
				width_model.remove(selectedIndex);
			}
		}
		
		if(e.getSource() == btnOk){
			
			int id = (int) spinner_id.getValue();
			int trialsPerCond = (int) spinner_trials.getValue();
			
			amp_list_model = amplitude.getModel();
			width_list_model = width.getModel();
			
			int[] amps = new int[amp_list_model.getSize()];
			int[] widths = new int[width_list_model.getSize()];
			
			for(int i = 0; i < amp_list_model.getSize(); i++)
				amps[i] = (int) amp_list_model.getElementAt(i);
			
			for(int i = 0; i < width_list_model.getSize(); i++)
				widths[i] = (int) width_list_model.getElementAt(i);
						
			MainStudyInterface nw = new MainStudyInterface();
			nw.studyInterface(id, trialsPerCond, amps, widths);
			
			frame.dispose();
			
		}
		
		if(e.getSource() == btnCancel){
			
			frame.dispose();
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
		amp_list_model = amplitude.getModel();
		width_list_model = width.getModel();
		
		lblTotalTrails.setText("Total trials: " + 
				(int) spinner_trials.getValue() * amp_list_model.getSize() * width_list_model.getSize());
		
	}
	
}
