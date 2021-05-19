import zio.*;

public class ShowFile {
    
    public static void main( String [] ignore ) {
        Zio.show("about to show ReadMe.txt\n");
        Zio.show( InOut.readText("ReadMe.txt") );
    }
    
}