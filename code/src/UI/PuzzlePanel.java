/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;


import static UI.MainFrame.hours;
import static UI.MainFrame.min;
import static UI.MainFrame.sec;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author skerd
 */
public class PuzzlePanel extends javax.swing.JPanel {

    /**
     * Creates new form PuzzlePanel
     */
    
    public static Tile[][] tileArray;
    static String direction;
    static int lastRow;
    static int lastCol;
    static boolean clicedTwice;
    
    public PuzzlePanel( ArrayList<Object> tileInfo ) {
        initComponents();
        
        datee.setText( java.time.LocalDate.now() + " PowerPuzz" );
        
        direction = "D";
        
        tileArray = new Tile[5][5];
        int count = 0;
       
        System.out.println(tileInfo.size() + " at puzzle panel !!!!!!!!!!!!!! ");
        
        for( int i = 0; i < 5 ; i++){
            for( int j = 0; j < 5; j++){
                
                ArrayList<Object> ti = (ArrayList<Object>) tileInfo.get(count);
                
                int tn = (int) ti.get(0);
                String tl = (String) ti.get(1);
                int r = (int) ti.get(2);
                int c = (int) ti.get(3);
                boolean en = (boolean) ti.get(4);
                String cl = (String) ti.get(5);
               
                //int tileNumber, String tileLetter, int row, int col, boolean enabled, String correctLetter
                tileArray[i][j] = new Tile( tn, tl, r, c, en, cl  );
                tileArray[i][j].setPreferredSize(new Dimension(100,100));
                puzzlePanel.add( tileArray[i][j] );
                count++;
            }
        }
    }
    
    
    public static void findQuestion(int questionNr){
        
        for( int i = 0; i < 5 ; i++){
            for( int j = 0; j < 5; j++){
                if( tileArray[i][j].isTileEnabled() && tileArray[i][j].getTileNumber() == questionNr ){
                    System.out.println("Found the tile at: [" + i + "," + j + "]");
                    tileArray[i][j].selectTile( i, j );
                    return;
                }
            }
        }  
        
    }
    
    public void clearTiles(){
        for( int i = 0; i < 5 ; i++){
            for( int j = 0; j < 5; j++){
                if( tileArray[i][j].isTileEnabled() )
                    tileArray[i][j].setBackground(StaticVars.free);
            }
        }  
    }
    
    public void fill(int row, int col){

//        if( direction.equals("A") ){
//            for( int i = 0; i < 5 ; i++){
//                if( tileArray[row][i].isTileEnabled() ){
//                    if( !(i == col) ){
//                        tileArray[row][i].setBackground(StaticVars.nearSelected);
//                    }
//                    else{
//                        tileArray[row][i].setBackground(StaticVars.selected);
//                        System.out.println("Selecting: [" + row + "," + col + "]");
//                    }
//                }
//            }
//        }
//        else{
//            for( int i = 0; i < 5; i++){
//                if( tileArray[i][col].isTileEnabled() ){
//                    if( !(i == row) ){
//                        tileArray[i][col].setBackground(StaticVars.nearSelected);
//                    }
//                    else{
//                        System.out.println("Selecting: [" + row + "," + col + "]");
//                        tileArray[i][col].setBackground(StaticVars.selected);
//                    }
//                }
//            }
//        }


    }
   
    public static void isPuzzleComplete(){
        boolean isComplete = true;
        for( int i = 0; i < 5 && isComplete; i++){
            for( int j = 0; j < 5; j++){
                if( !tileArray[i][j].isTileCorrect() && tileArray[i][j].isTileEnabled()){
                    isComplete = false;
                    break;
                }
            }
        }
        
        if( isComplete ){
            
            MainFrame.stopTimer = false;
            JFrame popUpFrame = new JFrame();
            //JOptionPane.showMessageDialog( popUpFrame, "The puzzle is completed in " + TimeLabel.getText().replace("Time: ", "") );
        }
        else{
            System.out.println("The puzzle is not yet completed!!");
        }
        
    }

    public static void clearLastTile(){
        tileArray[lastRow][lastCol].clearTile();
    }
    
    public static void clearWord(){
        if( direction.equals("A") ){
            for( int i = 0; i < 5; i++){
               tileArray[lastRow][i].clearTile();
            }
        }
        else{
            for( int i = 0; i < 5; i++){
                tileArray[i][lastCol].clearTile();
            }
        }
    }
    
    public static void erasePuzzle(){
        for( int i = 0; i < 5; i++){
            for( int j = 0; j < 5; j++){
                tileArray[i][j].clearTile();
            }
        }
    }
    
    public static void clearPuzzleAndTimer(){
        
        for( int i = 0; i < 5; i++){
            for( int j = 0; j < 5; j++){
                tileArray[i][j].hardClearTile();
            }
        }
        
        MainFrame.sec = 0;
        MainFrame.min = 0;
        MainFrame.hours = 0;
        
        //TimeLabel.setText("Time: " +"Time: 0:0:0");
        
        MainFrame.stopTimer = true;
        //MainFrame.runTimer();
        
    }
    
    public static void revealTile(){
        tileArray[lastRow][lastCol].revealTile();
        isPuzzleComplete();
    }
    
    public static void revealWord(){
        
        if( direction.equals("A") ){
            for( int i = 0; i < 5; i++){
               tileArray[lastRow][i].revealTile();
            }
        }
        else{
            for( int i = 0; i < 5; i++){
                tileArray[i][lastCol].revealTile();
            }
        }
        isPuzzleComplete();
    }
    
    public static void revealPuzzle(){
        for( int i = 0; i < 5; i++){
            for( int j = 0; j < 5; j++){
                tileArray[i][j].revealTile();
            }
        }
        
        isPuzzleComplete();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        puzzlePanel = new javax.swing.JPanel();
        datee = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(607, 641));

        puzzlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        puzzlePanel.setLayout(new java.awt.GridLayout(5, 5));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(puzzlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(puzzlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE))
        );

        datee.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        datee.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(datee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datee, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel datee;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel puzzlePanel;
    // End of variables declaration//GEN-END:variables
}
