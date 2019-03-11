package org.spookit.externalcore;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class D {

	public static void main(String[]args) {
		JFrame frame = new JFrame();
		frame.setSize(800, 600);
		frame.setLayout(new GridLayout(1,1));
		frame.add(new GuiStatsComponent());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
		
	public static class GuiStatsComponent extends JComponent
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final int[] b;
	    private int c;
	    private final String[] d;
	    
	    public GuiStatsComponent() {
	        this.b = new int[256];
	        this.d = new String[11];
	        this.setPreferredSize(new Dimension(456, 246));
	        this.setMinimumSize(new Dimension(456, 246));
	        this.setMaximumSize(new Dimension(456, 246));
	        new Timer(5, new ActionListener() {
	            @Override
	            public void actionPerformed(final ActionEvent actionEvent) {
	                GuiStatsComponent.this.a();
	            }
	        }).start();
	        this.setBackground(Color.BLACK);
	    }
	    
	    private void a() {
	        final long n = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	        System.gc();
	        this.d[0] = "Memory use: " + n / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
	        this.d[1] = "Avg tick: "+0+" ms";
	        this.b[this.c++ & 0xFF] = (int)(new Random().nextInt(50));
	        //n * 100L / Runtime.getRuntime().maxMemory()
	        this.repaint();
	    }
	    
	    
	    @Override
	    public void paint(final Graphics graphics) {
	        graphics.setColor(Color.DARK_GRAY);
	        graphics.fillRect(0, 0, 456, 246);
	        for (int i = 0; i < 256; ++i) {
	            final int n = this.b[i + this.c & 0xFF];
	            graphics.setColor(new Color(n + 28 << 10));//Color.green*/);
	            graphics.fillRect(i, 100 - n, 1, n);
	        }
	        graphics.setColor(Color.BLACK);
	        for (int j = 0; j < this.d.length; ++j) {
	            final String s = this.d[j];
	            if (s != null) {
	                graphics.drawString(s, 32, 116 + j * 16);
	            }
	        }
	    }
	    
	    public Color get() {
	    	return null;
	    }
	    
	}
}
