package pw.xero.parabot.thieving;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

@SuppressWarnings("serial")
public class GUI extends JFrame
{	
	private JPanel contentPane;
	public JComboBox<String> boxChoice;
	public JCheckBox doBank;
	
	public GUI()
	{
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		boxChoice = new JComboBox<String>();
		boxChoice.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				if(event.getStateChange() == ItemEvent.SELECTED)
				{
					if(boxChoice.getSelectedIndex() == 1 || boxChoice.getSelectedIndex() == 4)
					{
						doBank.setSelected(false);
						doBank.setEnabled(false);
					}
					else
					{
						doBank.setEnabled(true);
					}
						
				}
			}
		});
		boxChoice.setModel(new DefaultComboBoxModel<String>(new String[] {"Edgeville Stalls", "Ardougne Stalls", "Draynor Stalls", "Draynor Master Farmer", "Donator Zone"}));
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
			}
		});
		
		doBank = new JCheckBox("Bank");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(doBank)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(boxChoice, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnStart)))
					.addContainerGap(40, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(boxChoice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStart))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(doBank)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
