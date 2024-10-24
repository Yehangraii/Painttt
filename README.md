Release Notes:

v1.5
New Features
- The program can copy-paste a selected piece of the image. 
- Has hovering tool tips. Also, indicates which tool is selected.
- The software logs which feature is selected and tracks how long it was selected when a switch occurs. This is handled by thread.
- The software logs which files are opened and saved.  This is handled by thread.
- "Invent a feature". The application has emoji button and a emoji combo box which has list of emojis that the user can draw on the canvas when clicked. 

![paint4](https://user-images.githubusercontent.com/56151022/159076865-db3ea42b-80c2-4912-b79c-a5ec2509a96a.png)

v1.4
New Features
- The code has 3 unit tests. 
- The application has a timer that allows for autosave using threading.
- The user has the option to turn on/off autosave using the checkbox.
- Resets the timer on a user's save.
- The code has JavaDoc commenting. 
- The application saves in an alternative file format than the one that was the original for the image, and pops up a warning.

Improvements
- When selecting a piece of image, the application no longer strokes a rectangle.

Fixes:
- When opening and saving, there is only ever 3 extension to choose from compared to all the duplicates before.

Known Issues
- Resize doesn't work the way I want to.

Next Step:
- Remove hard coded codes.
- Fix resizing function

![paint3](https://user-images.githubusercontent.com/56151022/159076900-88365334-f084-4464-a172-0c096094698b.png)


v1.3
Features
- The program can open jpg and png files. Also has an option to view all files. 
- Can save the opened files on both jpg and png formats.
- Has BorderPane as the basic layout. Might change it in the future.
- Can exit successfully. 
- Has a pencil tool which can draw freely
- Has a line tool which doesn't work quite well
- Color Picker and width tool work for pencil
- Can save and save as
- The canvas resizes to fit the image being opened
- Has scrollbar which follows the canvas
- Has About in Help Menu
- Can "Smart Save"
- Can open multiple image type files (.jpg, .png)
- Has a toolbar which contains colorpicker, width, and pencil
- Has Keyboard UI controls. So Alt + F will open file menu, Ctrl + S - Save, Ctrl + Shift + S - Save As, Ctrl + O - Open.
- Has a pencil, line, rectangle, square, ellipse and circle tool
- Has a color grabber/dropper
- Has undo functionality
- Has a Keyboard UI control
- Can open upto 3 types of file types
- Can add text to the canvas and image
- Has triangle as an additional shape
- Has an eraser tool
- Has redo functionality
- Indicates which tool is selected and also indicates if no tool is selected currently
- The drawing tools update while dragging
- Has access to release notes in the about
- Has help about how to use all the tools
- Can select a peice of image and move it
- Allows user to input number of sides of polygon, select points in the canvas and draw it


Known Issues
- N/A

Next Step:
- Seperate the code into many classes and make it such that each one does one thing.


![paint2](https://user-images.githubusercontent.com/56151022/159076910-a9d201d4-9d15-47e5-9146-c8fd4dbdc949.png)


v1.2
Features
- The program can open jpg and png files. Also has an option to view all files. 
- Can save the opened files on both jpg and png formats.
- Has BorderPane as the basic layout. Might change it in the future.
- Can exit successfully. 
- Has a pencil tool which can draw freely
- Has a line tool which doesn't work quite well
- Color Picker and width tool work for pencil
- Can save and save as
- The canvas resizes to fit the image being opened
- Has scrollbar which follows the canvas
- Has About in Help Menu
- Can "Smart Save"
- Can open multiple image type files (.jpg, .png)
- Has a toolbar which contains colorpicker, width, and pencil
- Has Keyboard UI controls. So Alt + F will open file menu, Ctrl + S - Save, Ctrl + Shift + S - Save As, Ctrl + O - Open.
- Has a pencil, line, rectangle, square, ellipse and circle tool
- Has a color grabber/dropper
- Has a undo functionality
- Has a Keyboard UI control
- Can open upto 3 types of file types


Known Issues
- N/A

Next Step:
- Make the drawing tools update as the user drags it.

![paint1](https://user-images.githubusercontent.com/56151022/159076920-a6f2bec7-aaf6-465c-bf70-48f0e5c2fcf8.png)


v1.1
Features
- The program can open jpg and png files. Also has an option to view all files. 
- Can save the opened files on both jpg and png formats.
- Has BorderPane as the basic layout. Might change it in the future.
- Can exit successfully. 
- Has a pencil tool which can draw freely
- Has a line tool which doesn't work quite well
- Color Picker and width tool work for pencil
- Can save and save as
- The canvas resizes to fit the image being opened
- Has scrollbar which follows the canvas
- Has About in Help Menu
- Can "Smart Save"
- Can open multiple image type files (.jpg, .png)
- Has a toolbar which contains colorpicker, width, and pencil
- Has Keyboard UI controls. So Alt + F will open file menu, Ctrl + S - Save, Ctrl + Shift + S - Save As, Ctrl + O - Open.

Known Issues
- Line Tool makes a straight line but clears out the contents of the canvas. I have to clearRect the canvas to make the line tool work but that wipes out everything in the canvas.
- Actually have the resize canvas functionality but it doesn't work correctly
- JavaFx doesn't have good things for drawing lines and other shapes.

Next Steps
- Fix line tool for drawing a straight line keeping everything on canvas on place.
- Work on resizing the canvas.
- Figure out a way to incorporate Java Swing in order to make a straight line and draw other shapes because Swing is way better in doing that in doing that.


![paint](https://user-images.githubusercontent.com/56151022/159076931-1b88709e-a815-42fa-adf4-1d741d9bd935.png)



v1.0
Features
- The program can open jpg and png files. Also has an option to view all files. 
- Can save the opened files on both jpg and png formats.
- Has BorderPane as the basic layout. Might change it in the future.
- Can exit successfully. 

Known Issues
-N/A

Next Steps
-N/A




