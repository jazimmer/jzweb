import zio.*;

public class Saturn {
    
    public static void main( String [] ignore ) {
        DisplayWindow pw = 
            new DisplayWindow(
                 "Saturn",
                InOut.readImage("Saturn.jpg"),
                "The blemish on this beach ball is a Saturnian storm."
            );
    }    
}

// Note: the pw variable isn't needed because we never use it (only
// give it a value) and because its value is a window.

// Exercise:  Add
//
// import java.awt.image.*;
//
// to the top of your program so that you can use a variable of
// type BufferedInput.  Now make all necessary changes to your
// program so that the last statement can be
//          new DisplayWindow(
//               "Saturn",
//              image,
//              "The blemish on this beach ball is a Saturnian storm."
//          );
