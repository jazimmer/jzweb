import zio.*;

public class ShowFile2 {
    
    public static void main( String [] ignore ) {
        String name = "ReadMe";
        String extension = "txt";
        String fullName = name + "." + extension;
        Zio.show("about to show " + fullName + "\n");
        Zio.show( InOut.readText(fullName) );
    }
    
}

// Exercise: Rewrite using fullName variable but not the
// name and extension variables

// Exercise: Rewrite with a fileContents variable that will
// refer to the contents of the file before it is shown.  This
// means the last statement of your program will be
// Zio.show(fileContents).
