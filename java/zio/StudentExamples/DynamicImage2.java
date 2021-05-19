import zio.*;
import java.awt.image.*;

public class DynamicImage2 {
    
    public static void main( String [] fileName ) {
        new DynamicImageDialogWindow2();
    }
    
}

class DynamicImageDialogWindow2 extends DialogWindow {
    
    private DisplayWindow theDisplayWindow;
    
    public DynamicImageDialogWindow2() {
        super("Control","Enter a base file name.");
        theDisplayWindow = null;
    }
    
    public void processInput( String userin ) {
        if( userin.equalsIgnoreCase("stop") || userin.equals("") ) {    
            Zio.stop();
        }
        String txtName = userin + ".txt";
        String imgName = userin + ".jpg";
        if( InOut.canRead(txtName) && InOut.canReadImage(imgName) ) {
           if( theDisplayWindow!=null ) theDisplayWindow.closeWindow();
           theDisplayWindow = new DisplayWindow(
               "Display",
               InOut.readImage( imgName),
               InOut.readText( txtName )
           );
           displayString("Enter another base file name");
        } else {
           displayString(userin + " doesn't work, try again.");
        }
    }
    
}

// We are getting into some real programming here.

// Exercise: Using only the forms of the if statement shown in
// this example, rewrite the DynammicImageDialogWindow class
// so it has the exactly the same effect as far as the user is
// concerned but no && or || is used.
// Does && seem more valuable than ||?  Why?

// Exercise: Rewrite this so that it does not just try to load
// an image file with the ".jpg" extension, but also the ".png"
// and also the ".gif" extension.  Assume that at most one of these
// extensions will work.
// 
// This last exercise will be easier if you create a private method
// tryImage that lets your main method look like this
//        if( userin.equalsIgnoreCase("stop") || userin.equals("") ) {    
//            Zio.stop();
//        }
//        displayString(userin + " doesn't work, try again.");
//        if( InOut.canRead(userin+".txt") ) {
//            tryImage(".jpg");
//            tryImage(".png");
//            tryImage(".gif");
//        }
// The idea here is that, if in fact an image can be displayed, the 
// "doesn't work" message will be overwritten by one of the tryImages
// and the user won't really be aware that it was written.

