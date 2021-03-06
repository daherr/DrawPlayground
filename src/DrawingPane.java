/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author woytek
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.*;

public class DrawingPane extends JPanel implements ActionListener, MouseMotionListener, MouseListener, Serializable {

	public DrawingObject obj = null;
	public ArrayList<DrawingObject> objList = new ArrayList<DrawingObject>();

	public DrawingPane() {
		super(); // always call super() in an extended/derived class!
		// this.setSize( 500, 500 );
		// setSize( getPreferredSize() );
		// size is handled by parent pane placement in JFrame
		// make a border
		setBorder(BorderFactory.createLineBorder(Color.RED));
		setVisible(true);

		// we need both a mouse listener (for clicks)...
		addMouseListener(this);
		// ... and a mouse motion listener (for drags)!
		addMouseMotionListener(this);

	}
/*
 * Save drawing pane as jpeg
 * save into file to reopen and use
 * When it starts it does stuff, like good stuff :)                                                                                                      
 */
	
	/**
	 * actionPerformed is here in case we need it later. Not currently used.
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		default:
			System.out.println("EVIL BAD PLACE TWO");
			System.exit(-1);
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// handle what happens when the mouse is clicked. This will hinge upon
		// the mode the user has selected in the tool panel.

		System.out.println("mousePressed()");
		switch (ToolPanel.buttonSelected) {

		case (0):

			obj = new MyRectangle();
			objList.add(obj);
			obj.start(e.getPoint());

			break;
		case (1):

			obj = new MyCircle();
			objList.add(obj);
			obj.start(e.getPoint());

			break;
		case (2):

			obj = new DrawLine();
			objList.add(obj);
			obj.start(e.getPoint());
			break;
		case (3):

			obj = new MyStar();
			objList.add(obj);
			obj.start(e.getPoint());
			break;

		case (4):

			for (int ctr = objList.size() - 1; ctr >= 0; ctr--) {
				if (objList.get(ctr).contains(e.getPoint())) {
					obj = objList.get(ctr);
					break;
				}

			}
			break;
		case (5):
			Color color = null;
		System.out.println(objList.size());
			for( int ctr = 0; ctr < objList.size(); ctr++){
				obj = objList.get(ctr);
				if(obj.contains(e.getPoint())){
					System.out.println("FOUND IT");
					color = ToolPanel.colorChooser.getColor();
					obj.setColor(color);
				}
				 
			}
			break;
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (obj != null) {
			if (ToolPanel.buttonSelected == 4) {
				/*
				originX = originX - e.getX();
				originY = originY - e.getY();
				*/
				obj.move(e.getPoint());
				System.out.println("Moving");
			} else if (ToolPanel.buttonSelected == 5){
				
				
			}else{
				obj.drag(e.getPoint());
			}
			repaint();
		}
		System.out.println("mouseDragged( " + x + ", " + y + " )");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("mouseReleased()");
		repaint();
		obj = null;

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int ctr = 0; ctr < objList.size(); ctr++) {
			objList.get(ctr).draw(g);
		}
	}
	
	public void clearAll(){
		objList.clear();
    	repaint();
	}
	
	public void saveFile(String fileName){
		
			
		try {
			FileOutputStream saveStream = new FileOutputStream(fileName + ".AWE");
			ObjectOutputStream saveFile = new ObjectOutputStream(saveStream);
			
			for( int i = 0; i < objList.size(); i++ ){
				saveFile.writeObject(objList.get(i) );
			}
			saveFile.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("File Saved");
	}
	
	public void saveImage( String fileName){
		
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		paint(img.getGraphics());
		try {
			ImageIO.write(img, "JPG", new File(fileName + ".jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}
	public void openFile(String fileName){
		try {
			FileInputStream openStream = new FileInputStream(fileName);
			ObjectInputStream openFile = new ObjectInputStream(openStream);
			
			while(openStream.available() > 0){
				
				obj = (DrawingObject)openFile.readObject();
				objList.add(obj);
				
			}
			repaint();
			openFile.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

