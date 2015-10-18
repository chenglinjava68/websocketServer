
package com.hoolai.websocket.drawing.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import com.hoolai.websocket.drawing.data.DrawingObject;
import com.hoolai.websocket.drawing.data.Shape;

public class DrawingWindow extends JFrame {
	
	private static final long serialVersionUID = -5168333226509017640L;
	
	private JButton clearButton = new JButton("Clear");
    private JComboBox<Shape> shapeBox = new JComboBox<>();
    private Color currentColor;
    private JPanel colorPanel = new JPanel();
    private JPanel drawingPanel = new JPanel();
    private JSlider radiusSlider = new JSlider(5, 100, 15);
    
    private String path;
    private DrawingClient client;
    
    private JPanel createLabelPanel(String label, JComponent component) {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel(label));
        panel.add(component);
        return panel;
    }
     
    public DrawingWindow(String path, int topCornerX, int topCornerY) {
        this.path = path;
        this.currentColor = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        this.initWidgets();
        // layout the components
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new LineBorder(Color.BLACK));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        colorPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        controlPanel.add(this.createLabelPanel("Color: ", colorPanel));
        controlPanel.add(this.createLabelPanel("Shape: ", shapeBox));
        controlPanel.add(this.createLabelPanel("Size: ", radiusSlider));
        controlPanel.add(this.createLabelPanel("", clearButton));
        
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setBorder(new LineBorder(Color.BLACK));
        
        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new BorderLayout());
        masterPanel.add(controlPanel, BorderLayout.NORTH);
        masterPanel.add(drawingPanel, BorderLayout.CENTER);
        this.add(masterPanel);
        
        this.updateColor();
        this.setTitle("Connecting...");
        this.setBounds(topCornerX,topCornerY,600,550);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.connect();
    }
    
    private void initWidgets() {
        for (Shape s : Shape.values()) {
            shapeBox.addItem(s);
        }
        this.colorPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                getColor();
            }
        });
        this.drawingPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                drawNewObject(me.getPoint());
            }
        });
        this.drawingPanel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent me) {
                drawNewObject(me.getPoint());
            }
            public void mouseMoved(MouseEvent me) {}
        });
        
        this.clearButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               clearDrawing();
           } 
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                disconnect();
            }
        });  
    }
    
    public void connect() {
       this.client = DrawingClient.connect(this, this.path);
       if (this.client != null) {
           super.setTitle("Connected to " + this.path);
       } else {
           super.setTitle("Not connected");
       }  
    }
    
    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
    }
    
    public void getColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose Color", this.currentColor);
        if (newColor != null) {
            this.currentColor = newColor;
            this.updateColor();
        }
    }
    
    public void drawNewObject(Point center) {
        DrawingObject drawingObject = new DrawingObject((Shape) shapeBox.getSelectedItem(), center, radiusSlider.getValue(), currentColor);       
        this.clearButton.grabFocus();
        this.addDrawingObject(drawingObject);
        this.drawingPanel.grabFocus();
        this.updateServer(drawingObject);
    }
    
    public void addDrawingObject(DrawingObject drawingObject) {
        Graphics g = this.drawingPanel.getGraphics();
        drawingObject.draw(g);
        
    }

    public void updateServer(DrawingObject drawingObject) {
        if (this.client != null) {
            this.client.notifyServerDrawingChanged(drawingObject);  
        } 
    }
    
    public void updateColor() {
        this.colorPanel.setBackground(this.currentColor);
    }
    
    public void clearDrawing() {
        Graphics g = this.drawingPanel.getGraphics();
        if (g != null) {
            g.setColor(Color.WHITE);
            g.fillRect(0,0,this.drawingPanel.getWidth(),this.drawingPanel.getHeight());
        }
    }
    
}
