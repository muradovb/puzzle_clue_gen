package aiproject;

/**
 * Created by  on 4.11.2019.
 */

import UI.LoadingPanel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;

public class PuzzleGeometry {

    private WebDriver webDriver;
    public int[][] puzzleMatrix;
    public String[][] puzzleMatrixString;
    public String[][] puzzleMatrixQuestionNumbers;
   
    public static ArrayList<Object> allTiles ;
   

    //timeout until the page loads fully
    private static final int TIMEOUT=30;

    public PuzzleGeometry(){
        
        //System.setProperty("webdriver.chrome.driver","C:\\Users\\skerd\\Documents\\NetBeansProjects\\Cs461\\chrom\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver","C:\\Users\\skerd\\Documents\\NetBeansProjects\\Cs461\\chrom\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "./geckodriver");
        ChromeOptions options = new ChromeOptions();
        webDriver = new ChromeDriver(options);

        allTiles = new ArrayList<Object>();

        try {
            //open puzzle page
            webDriver.get("https://www.nytimes.com/crosswords/game/mini");

            WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
            
            
            //====================================================================
            
            List<WebElement> links = webDriver.findElements(By.className("ClueList-wrapper--3m-kd"));

            List<WebElement> across = links.get(0).findElements(By.className("Clue-li--1JoPu"));
            int numberOfAcrossClues = across.size();

            List<WebElement> down = links.get(1).findElements(By.className("Clue-li--1JoPu"));
            int numberOfDownClues = down.size();

            System.out.println("Number of across clues: " + numberOfAcrossClues);
            System.out.println("Number of down clues: " + numberOfDownClues);

            ArrayList<Object> acrossClues = new ArrayList<Object>();
            ArrayList<Object> downClues = new ArrayList<Object>();

            for(WebElement we: across) {
                
                String c = we.getText().replace("\n", " ");
                 
                int questionNr = Integer.parseInt( c.substring(0, c.indexOf(" "))  );
                String question = c.substring( c.indexOf(" ") +1 );
                System.out.println("[A][" + questionNr + "][" + question + "]");

                ArrayList<Object> temp = new ArrayList<Object>();
                temp.add(questionNr);
                temp.add(question);
                
                acrossClues.add(temp);
                   
            }

            for(WebElement we: down) {
                    String c = we.getText().replace("\n", " ");
                    
                    int questionNr = Integer.parseInt( c.substring(0, c.indexOf(" "))  );
                    String question = c.substring( c.indexOf(" ") +1 );

                    System.out.println("[D][" + questionNr + "][" + question + "]");

                    ArrayList<Object> temp = new ArrayList<Object>();
                    temp.add(questionNr);
                    temp.add(question);

                    downClues.add(temp);
                    
            }
            
          
            //====================================================================
            
            
            

            //wait until start button appears
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("buttons-modalButton--1REsR")));

            System.out.println("clicking start");
            //click START
            webDriver.findElements(By.className("buttons-modalButton--1REsR")).get(0).click();
            System.out.println("start clicked");

            //wait until toolbar/REVEAL appears
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div[4]/div/main/div[2]/div/div/ul/div[2]/li[2]/button")));

            //click REVEAL
            webDriver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div[4]/div/main/div[2]/div/div/ul/div[2]/li[2]/button")).click();

            //click PUZZLE
            webDriver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div[4]/div/main/div[2]/div/div/ul/div[2]/li[2]/ul/li[3]/a")).click();

            //wait until pop-up/REVEAL appears
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/article/div[2]/button[2]")));

            //click REVEAL on pop-up
            webDriver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/article/div[2]/button[2]")).click();

            //get page source
            String pageSource = webDriver.getPageSource();

            //parse page source
            Document doc = Jsoup.parse(pageSource);
            Elements groups = doc.select("#xwd-board > g:nth-child(5)");
            //get 'g' tags
            Elements cells = groups.first().children();

            int tracer=0;
            puzzleMatrix = new int[5][5];
            puzzleMatrixString = new String[5][5];
            puzzleMatrixQuestionNumbers = new String[5][5];
            int i=0;
            int j=0;

            for (Element e : cells){
                if(i==5) {
                    break;
                }
                if(j==5) {
                    j=0;
                    i++;
                }
                puzzleMatrix[i][j] = processCellData(e, i, j);
                j++;
            }
            
            printMatrixWithSolution();

            
            ClueGenerator newGen = new ClueGenerator(allSolutionInformation()); 
            newGen.generateClues();
            newGen.printNewClues();
            
            LoadingPanel.newAccrossClues = newGen.newAccrossClues;
            LoadingPanel.newDownClues = newGen.newDownClues;
            LoadingPanel.aaClues = acrossClues;
            LoadingPanel.ddClues = downClues;
            LoadingPanel.tiles = allTiles;
            LoadingPanel.haveAllQuestions = true;

            webDriver.close();
            webDriver.quit();
        }
        catch (TimeoutException e) {
            System.out.println("timed out");
            System.exit(0);
        }

    }

    private int processCellData(Element element, int row, int col){
        Elements components = element.getElementsByTag("text");
        int elSize= components.size();
        int result=-1;
        //3 cases
        //1. number& letter: edge cells
        //2. only letters: inner cells
        //3. nothing: black cells

        if(elSize==4) { //number&letter
            String number= components.get(0).text();
            String letter= components.get(3).text();
            
            //System.out.println("N: [" + number + "]  L[" + letter + "]");
            
            int newNumber = -1;
            
            if( number != null){
               newNumber = Integer.parseInt(number);
            }
            
            ArrayList<Object> temp = new ArrayList<Object>();
            temp.add(newNumber);  temp.add(""); temp.add(row); temp.add(col);
            temp.add(true); temp.add(letter);
            puzzleMatrixString[row][col] = letter;
            puzzleMatrixQuestionNumbers[row][col] = number;
            
            //System.out.println("Adding number+letter: [" + number + "][" + letter + "][" + row  +"][" + col + "]");
            
            allTiles.add(temp);
            
            result=2;
        }
        else if(elSize==2) { //only letter
            String letter= components.get(1).text();
            String number = null;
            
            int newNumber = -1;
            
            if( number != null){
                newNumber = Integer.parseInt(number);
            }
            
            ArrayList<Object> temp = new ArrayList<Object>();
            temp.add(newNumber);  temp.add(""); temp.add(row); temp.add(col);
            temp.add(true); temp.add(letter);
            puzzleMatrixString[row][col] = letter;
            puzzleMatrixQuestionNumbers[row][col] = number;
            
            //System.out.println("Adding only letter: [" + number + "][" + letter + "][" + row  +"][" + col + "]");
            
            allTiles.add(temp);
            
            result=1;
        }
        else if(elSize==0) {//black cell
            String letter=null;
            String number=null;
            
            int newNumber = -1;
            
            if( number != null){
                newNumber = Integer.parseInt(number);
            }
            
            //System.out.println("Adding Black cell: [" + number + "][" + letter + "][" + row  +"][" + col + "]");
            
            ArrayList<Object> temp = new ArrayList<Object>();
            temp.add(newNumber);  temp.add(""); temp.add(row); temp.add(col);
            temp.add(false); temp.add(letter);
            puzzleMatrixString[row][col] = "~";
            puzzleMatrixQuestionNumbers[row][col] = "B";
            
            allTiles.add(temp);
            
            result=0;
        }
        
       return result;

    }
    
    
    private void printMatrixWithSolution(){
        
        for( int i = 0; i < 5; i++){
            for( int j = 0; j < 5; j++){
                System.out.print("[" + puzzleMatrixString[i][j] + "]");
            }
            
            System.out.print("    ");
            
            for( int j = 0; j < 5; j++){
                if( puzzleMatrixQuestionNumbers[i][j] == null){
                    puzzleMatrixQuestionNumbers[i][j] = " ";
                }
                
                System.out.print("[" + puzzleMatrixQuestionNumbers[i][j] + "]");
 
            }
            System.out.println("");
        }
    }

    
    private ArrayList<String[]> allSolutionInformation(){
        
        int currQuestion = -1;
        String currQuestionSol = "";
        
        ArrayList<String[]> returnThis = new ArrayList<String[]>();
        
        String[][] fetchDown = new String[5][5];
        String[][] fetchAccross = new String[5][5];
        
        for( int i = 0; i < 5; i++){
            for( int j = 0; j<5; j++){
               fetchDown[i][j] = puzzleMatrixQuestionNumbers[i][j];
               fetchAccross[i][j] = puzzleMatrixQuestionNumbers[i][j];
            }
        }
        
        
        
        for( int i = 0; i < 5; i++){
            for( int j = 0; j < 5; j++){
                if( !fetchDown[i][j].equals("B") && !fetchDown[i][j].equals(" ") && !fetchDown[i][j].equals("~")){
                    if( currQuestion != Integer.parseInt(fetchDown[i][j])){
                        currQuestion = Integer.parseInt(fetchDown[i][j]);
                        currQuestionSol = "";
                        
                        for( int z = i; z<5 && !fetchDown[z][j].equals("B") && !fetchDown[z][j].equals("~") ; z++){
                            currQuestionSol = currQuestionSol + puzzleMatrixString[z][j];
                            fetchDown[z][j] = "~";
                        }
                        
                        //System.out.println("[DOWN   ]For question: [" + currQuestion + "] the solution is: [" + currQuestionSol + "]");
                        String[] temp = new String[3];
                        temp[0] = currQuestion + "";
                        temp[1] = "Down";
                        temp[2] = currQuestionSol;
                        
                        returnThis.add(temp);
                        
                        
                    }
                }
            }
        }
        
        for( int i = 0; i < 5; i++){
            for( int j = 0; j < 5; j++){
                if( !fetchAccross[i][j].equals("B") && !fetchAccross[i][j].equals(" ") && !fetchAccross[i][j].equals("~")){
                    if( currQuestion != Integer.parseInt(fetchAccross[i][j])){
                        currQuestion = Integer.parseInt(fetchAccross[i][j]);
                        currQuestionSol = "";
                        
                        for( int z = j; z<5 && !fetchAccross[i][z].equals("B") && !fetchAccross[i][z].equals("~") ; z++){
                            currQuestionSol = currQuestionSol + puzzleMatrixString[i][z];
                            fetchAccross[i][z] = "~";
                        }
                        
                        //System.out.println("[ACCROSS]For question: [" + currQuestion + "] the solution is: [" + currQuestionSol + "]");
                        String[] temp = new String[3];
                        temp[0] = currQuestion + "";
                        temp[1] = "Accross";
                        temp[2] = currQuestionSol;
                        
                        returnThis.add(temp);
                    }
                }
            }
        }
        
        
        for( int i = 0; i < returnThis.size(); i++){
            String[] temp = returnThis.get(i);
            System.out.println("[" + temp[1] + "] For question: [" + temp[0] + "] the solution is: [" + temp[2] + "]");
        }
        
        
        return returnThis;
    }
    
    
}
