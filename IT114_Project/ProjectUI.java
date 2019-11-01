package IT114_Project;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class ProjectUI {
	public static Point testPoint = new Point(0,0);
	public static HashMap<Point, JButton> lazyGrid;
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Tic Tac Toe");
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(500,500));
		frame.setMinimumSize(new Dimension(500,500));
		
		int rows = 3;
		int cols = 3;
		Dimension gridDimensions = new Dimension(500,500);
		JPanel grid1 = new JPanel();
		//set gridlayout pass in rows and cols
		grid1.setLayout(new GridLayout(rows,cols));
		grid1.setSize(gridDimensions);
		
		JTextField textField = new JTextField();
		//grid layout creation (full layout control)
		for(int i = 0; i < (rows*cols); i++) {
			JButton button = new JButton();
			//convert to x coordinate
			int x = i % rows;
			//convert to y coordinate
			int y = i/cols;

			button.setText("");
			if(x % 2 == 0 && y % 2 == 0)
			{
				button.setBackground(Color.white);
			}
			else
			{
				button.setBackground(Color.GRAY);
			}
			if(x == 1 && y == 1)
			{
				button.setBackground(Color.white);
			}

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textField.setText(((JButton)e.getSource()).getText());
					
				}
				
			});
			grid1.add(button);
			
		}
		
		frame.add(grid1, BorderLayout.CENTER);

		frame.add(textField, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}


}
