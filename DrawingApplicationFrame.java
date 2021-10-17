/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JColorChooser;
/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{
  private JPanel outerHeaderPanel;
  private JPanel upperInnerPanel;
  private JPanel lowerInnerPanel;
  private JLabel shape;
  private JComboBox<String> shapePickingBox;
  private JButton colorPicker1;
  private Color color1;
  private JButton colorPicker2;
  private Color color2;
  private JButton undo;
  private JButton clear;
  private JLabel optionsLabel;
  private JCheckBox filled;
  private JCheckBox useGradient;
  private JCheckBox dashed;
  private JLabel lineWidth;
  private JSpinner strokeWidth;
  private JLabel dashLength;
  private JSpinner strokeDashLength;
  private JLabel status;
  private Stroke stroke;
  private Paint paint;
  private final BorderLayout layout;
  ArrayList<MyShapes> shapes = new ArrayList<MyShapes>();
  private MyShapes currentShape;
  private static final String[] shapesText = {"Rectangle","Line","Oval"};

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.

    // create the widgets for the firstLine Panel.

    //create the widgets for the secondLine Panel.

    // Variables for drawPanel.

    // add status label
  
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        super("Java 2D Drawings");
        layout = new BorderLayout(5,5);
        setLayout(layout);
        outerHeaderPanel = new JPanel();
        outerHeaderPanel.setLayout(new GridLayout(2,1));
        outerHeaderPanel.setBackground(Color.decode("#00FFFF"));
        upperInnerPanel = new JPanel();
        upperInnerPanel.setLayout(new FlowLayout());
        upperInnerPanel.setBackground(Color.decode("#00FFFF"));
    
        lowerInnerPanel = new JPanel();
        lowerInnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
        lowerInnerPanel.setBackground(Color.decode("#00FFFF"));

        // add widgets to panels
        
        // firstLine widgets
        shape = new JLabel("Shape:");
        shapePickingBox = new JComboBox<String>(shapesText);
        shapePickingBox.setMaximumRowCount(3);
        colorPicker1 = new JButton("1st Color");
        colorPicker2 = new JButton("2nd Color");
        undo = new JButton("Undo");
        clear = new JButton("Clear");
        upperInnerPanel.add(shape);
        upperInnerPanel.add(shapePickingBox);
        upperInnerPanel.add(colorPicker1);
        upperInnerPanel.add(colorPicker2);
        upperInnerPanel.add(undo);
        upperInnerPanel.add(clear);
        
        // secondLine widgets
        optionsLabel = new JLabel("Options:");
        filled = new JCheckBox("Filled");
        useGradient = new JCheckBox("Use Gradient");
        dashed = new JCheckBox("Dashed");
        lineWidth = new JLabel("Line Width");
        strokeWidth = new JSpinner();
        dashLength = new JLabel("Dash Length");
        strokeDashLength = new JSpinner();
        lowerInnerPanel.add(optionsLabel);
        lowerInnerPanel.add(filled);
        lowerInnerPanel.add(useGradient);
        lowerInnerPanel.add(dashed);
        lowerInnerPanel.add(lineWidth);
        lowerInnerPanel.add(strokeWidth);
        lowerInnerPanel.add(dashLength);
        lowerInnerPanel.add(strokeDashLength);

        // add top panel of two panels  
        outerHeaderPanel.add(upperInnerPanel);
        outerHeaderPanel.add(lowerInnerPanel);
        DrawPanel drawPanel = new DrawPanel();
        status = new JLabel("(0,0)");
        JPanel bottomHolder = new JPanel();
        bottomHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomHolder.setBackground(Color.decode("#D9D9D4"));
        bottomHolder.add(status);

        // add topPanel to North, drawPanel to Center, and statusLabel to South
        super.add(outerHeaderPanel, BorderLayout.NORTH);
        super.add(drawPanel,BorderLayout.CENTER);
        super.add(bottomHolder,BorderLayout.SOUTH);

        //add listeners and event handlers
        ButtonHandlerColorOne colorOneHandler = new ButtonHandlerColorOne();
        ButtonHandlerColorTwo colorTwoHandler = new ButtonHandlerColorTwo();
        ButtonHandlerUndo undoHandler = new ButtonHandlerUndo();
        ButtonHandlerClear clearHandler = new ButtonHandlerClear();
        colorPicker1.addActionListener(colorOneHandler);
        colorPicker2.addActionListener(colorTwoHandler);
        undo.addActionListener(undoHandler);
        clear.addActionListener(clearHandler);
    }
    private class ButtonHandlerColorOne implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent event){
        color1 = JColorChooser.showDialog
        (DrawingApplicationFrame.this,"Choose a color",Color.BLACK);
      }
      }
    private class ButtonHandlerColorTwo implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent event){
        color2 = JColorChooser.showDialog(DrawingApplicationFrame.this,"Choose a color",Color.BLACK);
      }
      }
    private class ButtonHandlerUndo implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent event){
        if (shapes.size() != 0){
          shapes.remove(shapes.size() - 1);
        }
      }
      }
    private class ButtonHandlerClear implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent event){
        shapes.clear();
      }
      }


    // Create event handlers, if needed

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

        public DrawPanel()
        {
          super.setBackground(Color.WHITE);
          MouseHandler handler = new MouseHandler();
          super.addMouseListener(handler);
          super.addMouseMotionListener(handler);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for (MyShapes shp: shapes){
              shp.draw(g2d);
            }

        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            public void mousePressed(MouseEvent event)
            {
              // We need paint variable and set values based on top two lines
              String selectedShape = shapePickingBox.getSelectedItem().toString();
              boolean filledVal = filled.isSelected();
              Point pointA = new Point(event.getX(),event.getY());
              if (useGradient.isSelected()){
                paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
              }
              else{
                paint = color1;
              }
              if (dashed.isSelected())
              {
                float[] strokeWidthVal = {(float) (Integer) strokeWidth.getValue()};
                stroke = new BasicStroke((float) (Integer) strokeDashLength.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, strokeWidthVal, 0);
            } 
              else{
                stroke = new BasicStroke((float) (Integer) strokeDashLength.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }
            if (selectedShape.equals("Rectangle")){
              currentShape = new MyRectangle(pointA, pointA, paint, stroke, filledVal);
              shapes.add(currentShape);
            }
            else if (selectedShape.equals("Line")){
              currentShape = new MyLine(pointA, pointA, paint, stroke);
              shapes.add(currentShape);
            }
            else{
              currentShape = new MyOval(pointA, pointA, paint, stroke, filledVal);
              shapes.add(currentShape);
            }
            }
            public void mouseReleased(MouseEvent event)
            {
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
              Point currentPoint = new Point(event.getX(),event.getY());
              currentShape.setEndPoint(currentPoint);
              repaint();
              
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
              status.setText(String.format("(%d,%d)",event.getX(),event.getY()));
            }
        }

    }
  public static void main(String[] args){
    DrawingApplicationFrame mygui = new DrawingApplicationFrame();
    mygui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mygui.setSize(650,500);    
    mygui.setVisible(true);  
  }
}
