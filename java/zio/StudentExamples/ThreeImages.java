import zio.*;
import java.awt.image.*;

public class ThreeImages {
    
    private static void makeImageWindow( String baseName ) {
        BufferedImage image = InOut.readImage(baseName+".jpg");
        String caption = InOut.readText(baseName+".txt");
        new DisplayWindow( baseName, image, caption );
    }
    
    public static void main( String [] ignore ) {
       makeImageWindow("Saturn");
       makeImageWindow("Mars");
       makeImageWindow("Earth");
    }
    
}

//  Exercise: change "baseName" to a variable name of your
//  choice AND add a fourth image window to this program.
//  If you cannot fine another image, then copy one of these 
//  to a different file name and make up your own caption.
