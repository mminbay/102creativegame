import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import javax.swing.*;

//    ******************************************************
//    *                                                    *
//    *    !!   DO  NOT  MODIFY THIS FILE     !!           *
//    *                                                    *
//    ******************************************************

/**
	* @author Elodie Fourquet 
	* @date March, 2021
	
  Represents the 2D grid that stores the state of our "game board"
  
  A GameGrid is a matrix of cells
    - the methods YOU NEED are those declared in the GridController
    and implemented below; read comments
    
    **/

public class GameGrid extends JComponent implements GridController {
  
  
  //---------------- Instance Variables -----------------//
  
  private int cellSize;
  
  private Color gridLineColor;
  
  private Color gameBackgroundColor = DEFAULT_BACKGROUND_COLOR;
  
  //background image during game play
  private Image backgroundImg = null;
  
  //splash screen image filename
  private Image splash = null;
  
  //tracks last location clicked on by the user
  private Location lastLocationClicked;
  
  //most important state: a 2D matrix storing the state of each cell
  //  (which is an inner class defined at the bottom of this file)
  private Cell[][] cells;
  
  /**********************     Constructors   **********************/
  
  //takes dimensions and gameplay background image 
  public GameGrid(int numRows, int numCols, String backgroundImg) {
    updateBackground(backgroundImg);
    initCells(numRows, numCols);
  }

  
  public GameGrid(int numRows, int numCols) {
    this(numRows, numCols, null);
  }
  

  
  /*********** GridController Interface methods  
              call those only      *************************/
  
  // Turn on/off GameGrid lines (useful for debugging)
  public void toggleLines(boolean on){
    if (on)
      gridLineColor = DEFAULT_LINE_COLOR; 
    else 
      gridLineColor = null;
    
    repaint();
  }
  
  // Return the total number of rows (width) of the game grid
  public int getTotalRows() {
    return cells.length;
  }
  
  // Return the total number of columns (height) of the game grid
  public int getTotalCols() {
    return cells[0].length;
  }
  
  // Set at the passed location the argument string Image
  // blank if null passed
  public void setAsset(Location loc, String imgName) {
    setCellImage(loc, imgName);
  }
  
  
  // Return the name of the image stored at the location
  // null for empty
  public String getAsset(Location loc) {
    return getCellImage(loc);
  }
  
  // Move the content: "from" --> "to" Locations 
  // image, and color, and null if the "from" location is blank 
  // returns the image previously stored at the "to" location
  public String moveAsset(Location from, Location to) {
  	
    String img = getAsset(from);
    String eraseImg = getAsset(to);
    
    Color color = getAColor(from); 
    
    setAsset(from, null);
    setAColor(from, null);
    setAsset(to, img);
    setAColor(to, color);
    
    return eraseImg;
  }
  
  
  // paint color at a particular location (null to erase)
  public void setAColor(Location loc, Color color) {
    updateColor(loc, color);
  }
  
  // retrieve color (null for none at a particular location, 
  // throws exception for invalid location
  public Color getAColor(Location loc) {
    return retrieveValidColor(loc);
  }
  
    
  //sets splash image drawn drawn on top of the grid to the arg image file
  public void setSplashScreen(String imageFileName) {
    splash = setImage(imageFileName);
    repaint();
  }
  
  public void setBackgroundColor(Color backgroundColor){
    gameBackgroundColor = backgroundColor;
    
    if (gameBackgroundColor == null)            
      gameBackgroundColor = GridController.DEFAULT_BACKGROUND_COLOR;
  }
  
   
  // set the provided img as the background during the game play
  // null for nothing, then gameloopBackgroundColor applies
  public void setBackgroundImage(String img){
    updateBackground(img);        
  }
 
  
  
  
  //---------------- Instance Methods -----------------//
  //            should not be needed in your code
  
  public int getCellSize() {
    return cellSize;
  }
  
  // Take a screenshot of the content of the GameGrid
  public void takeScreenShot(String fileName) {
  	System.out.println("Take screen shot, file at: " + fileName);
    save(fileName);        
  }
  
  //repaints contents of the game grid
  public void paintComponent(Graphics g) {
    
    if (splash!=null)
      g.drawImage(splash, 0, 0, getWidth(), getHeight(), null);
    else 
      drawGameGrid(g);
  }
  
   
  //returns location clicked since last call
  //    null otherwise 
  public Location checkLastLocationClicked() {
    Location loc = lastLocationClicked;
    lastLocationClicked = null;
    return loc;
  }
  
  //------------ LOW-LEVEL methods, you don't need to understand them ----

  //draws the argument image in the cell at the argument location 
  private void setCellImage(Location loc, String imgName) {
    if (isValid(loc)) {
      cells[loc.getRow()][loc.getCol()].setImageFileName(imgName);
      repaint();
    } else 
    System.out.println("Can't set image for this invalid location\n" +
      "Bad grid.setImage(loc, name) call with " + imgName);
    
  } 
  
  
  //returns the name of the image being drawn in the Cell at the arg Location
  private String getCellImage(Location loc) {
    if (isValid(loc))
      return cells[loc.getRow()][loc.getCol()].getImageFileName();
    System.out.println("Can't get image at this invalid location\n" + 
      "Bad grid.getImage(loc) call");
    return null;
    
  }
  

  //saves a screenshot of the grid's  state to a img file of the arg name
  private void save(String imgFileName) {
    try {
      BufferedImage bi = new BufferedImage(getWidth(), getHeight(), 
        BufferedImage.TYPE_INT_RGB);
      
      paintComponent(bi.getGraphics());
      int index = imgFileName.lastIndexOf('.');
      if (index == -1)
        throw new RuntimeException("invalid file name:  " + imgFileName);
      ImageIO.write(bi, imgFileName.substring(index + 1), new File(imgFileName));
      
    } catch(IOException e) {
      throw new RuntimeException("unable to save to file:  " + imgFileName);
    }
  }
  

  // set the color at that valid location 
  //    null passed for color to represent no color i.e. no block present
  private void updateColor(Location loc, Color color) {
    if (isValid(loc)) {  
      cells[loc.getRow()][loc.getCol()].setColor(color);
      repaint();
    }
  }
  
  
  // return either the color at that valid location 
  //        or null 
  //             for invalid location or 
  //             no color there
  private Color retrieveValidColor(Location loc) {
    if (isValid(loc))
      return cells[loc.getRow()][loc.getCol()].getColor();
    
    return null;
  }
  
    
  //sets the background of the grid to the argument image file name; 
  //null for no background image
  private void updateBackground(String imageFileName) {
   // if (imageFileName!=null) {
      backgroundImg = setImage(imageFileName);
  //    splash = null;
      repaint();
   // } 
  }
  
  //Initilizes a single cell on the grid
  private void initCells(int numRows, int numCols) {

    cells = new Cell[numRows][numCols];
    
    for (int row = 0; row < numRows; row++)
      for (int col = 0; col < numCols; col++)
      cells[row][col] = new Cell();
    
    //initial cellSize for determining Window size  
    cellSize = (int)Math.ceil(Math.min(1000.0 / numRows, 1000.0 / numCols));  
  }
  
  
  
  //(re)paints the contents of the grid
  private void drawGameGrid(Graphics g) {
    
    if (backgroundImg!=null) {
      g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null);
    }
    
    for (int row = 0; row < getTotalRows(); row++) {
      for (int col = 0; col < getTotalCols(); col++) {
        
        int cellSize = getCellSize();
        
        int x = col * cellSize;
        int y = row * cellSize; 
        
        drawCell(g, x, y, new Location(row, col), cellSize);
        
        //draw boundary if gridLineColor has been set
        if (gridLineColor != null) {
          Graphics2D g2 = (Graphics2D) g;
          g.setColor(gridLineColor);
          g2.setStroke(new BasicStroke(3));
          g.drawRect(x, y, cellSize, cellSize);
        }
      }  
    }
  }
  
  
  //repaints a single cell on the GameGrid
  private void drawCell(Graphics g, int x, int y, Location loc, int cellSize) {
    
    Cell cell = cells[loc.getRow()][loc.getCol()];
    
    Image image = setImage(cell.getImageFileName());
    
    cell.draw(g, x,  y, image, cellSize, backgroundImg);
  }
  
 
  
  
  
  //determines if the arg Location is valid (ie. not out of bounds)
  private boolean isValid(Location loc) {
    int row = loc.getRow();
    int col = loc.getCol();
    if (0 <= row && row < getTotalRows() && 0 <= col && col < getTotalCols())
      return true;
    else 
      throw new RuntimeException("Invalid " + loc);
  }
  
  //opens and returns the image of the argument filename
  private Image setImage(String imageFileName) {
    if (imageFileName == null)
      return null;
    URL url = getClass().getResource(imageFileName);
    if (url == null)
      throw new RuntimeException("cannot find file:  " + imageFileName);
    return new ImageIcon(url).getImage();
  }
  
  
  //---------------   Inner class Cell  -------------------- 
  class Cell {
  	
    private Color color = null;
    private String imageFileName;
    
    public Cell() {
      imageFileName = null;
    }
    
    
    public void setColor(Color c) {
      color = c;
    }
    
    public Color getColor() {
      return color;
    }
    
    public String getImageFileName() {
      return imageFileName;
    }
    
    public void setImageFileName(String fileName) {
      imageFileName = fileName;
    }
    
    public void draw(Graphics g, int x, int y, Image image, int cellSize, Image background) {
      
      if (background == null || color != null) {
        
        if (background == null) 
          g.setColor(gameBackgroundColor);       
        
        if (color != null)
          g.setColor(color);      
        
        g.fillRect(x, y, cellSize, cellSize);   
      }
      
      if (image!= null) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        
        if (width > height) {
          int drawHeight = cellSize * height / width;
          
          g.drawImage(image, x, y + (cellSize - drawHeight) / 2,
            cellSize, drawHeight, null);
        } else {
          int drawWidth = cellSize * width / height;
          
          g.drawImage(image, x + (cellSize - drawWidth) / 2, y, 
            drawWidth, cellSize, null);
        }
      }                                                                                      
      
    }
    
  }
  
  
}