/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiproject;

import UI.MainFrame;
import java.awt.Dimension;

/**
 *
 * @author skerd
 */
public class Cs461 {
    
    static public MainFrame main;

    public static void main(String[] args) {
        
        main = new MainFrame();
        main.setSize(new Dimension( 1250,700));
        main.setVisible(true);
        main.setResizable(false);
        
    }
    
}
