import zio.*;

public class DynamicImage {
    
    public static void main( String [] fileName ) {
        new DynamicImageDialogWindow();
    }
    
}

class DynamicImageDialogWindow extends DialogWindow {
    
    public DynamicImageDialogWindow() {
        super("Control","Enter a base file name.");
    }
    
    public void processInput( String userin ) {
        new DisplayWindow(
               "Display",
               InOut.readImage( userin + ".jpg" ),                                  
               InOut.readText( userin + ".txt" )
        );
    }
    
}
