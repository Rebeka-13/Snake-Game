# Snake-Game  

## Project Overview:
This is a simple Snake Game built in Java, featuring classic mechanics like snake movement, food collection, and collision detection.
It also includes additional functionality such as high score tracking, sound effects, and game over management.
The game interface is built using Java Swing. The game logic is organized into multiple classes to improve maintainability and structure.


## File Descriptions
CardLayoutJFrame.java:  
Sets up the main window (JFrame) and uses a CardLayout to manage transitions between panels, such as the game screen and the game over screen.


GamePanel.java:  
The core panel for the single-player mode, responsible for managing the snake’s movement, food placement, collision detection, and score tracking. It also handles user input and updates the game state in real-time.


GamePanel2.java:  
A modified version of GamePanel, designed for multiplayer mode. It allows two players to control their snakes and interact with the game simultaneously.


Cell.java:  
Represents a single segment of the snake or a piece of food. It stores the coordinates of these elements, enabling accurate placement within the game grid.


BackgroundImageManager.java, SoundManager.java, and HighScoreManager.java:  
These utility classes work together to enhance the overall game experience.  


