import java.awt.Color;


interface GridController {
  
  //----------------------------  Constants -----------------------------//  
  
  //default color for a blank cell on the grid
  public static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK; 
  public static final Color DEFAULT_LINE_COLOR = Color.YELLOW; 

  // default number of vertical/horizontal cells: height/width of grid
  public static final int DEFAULT_GRID_H = 6;
  public static final int DEFAULT_GRID_W = 12;
  

  //----------------- Instance method declarations -----------------------//  


  public int getTotalCols();
  public int getTotalRows();
  
  
  public void setAsset(Location loc, String imgName);
  public String getAsset(Location loc);
  public String moveAsset(Location from, Location to);

  public Color getAColor(Location loc);
  public void setAColor(Location loc, Color color);
   
  // Display the provided color as the background 
  // during the game play, null for no color (default BLACK used)
  public void setSplashScreen(String imageFileName);
  public void setBackgroundImage(String img);
  public void setBackgroundColor(Color color);

}
