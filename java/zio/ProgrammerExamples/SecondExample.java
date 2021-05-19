// Second of four examples, see jazimmer.net/source_code/java/zio.html
// copyright 2005 by J Adrian Zimmer
// Licensed under the Open Software License version 2.1

import zio.*;      
import java.awt.*; // for Java AWT's Color class

/**
This program requires a gif, jpg, or png image that will fit on the
screen. Run the program this way

   java SecondExample <photo>

or, more specifically,
    
   java SecondExample myPhoto.jpg

The program will display a picture below of the name of the file which
contains it. Placing one object above another is done by making a
column with ZCol. The column created here is modified with three
suffixes. 
**/

public class SecondExample {

    public static void main( String [] photo) {
        if( photo.length<1 || !InOut.canReadImage(photo[0]) )
            Zio.abort("cannot display that picture");
        Zio.setGlobalBackground(Color.BLACK);
        Zio.setGlobalForeground(Color.WHITE);
        ZWindow g1 = new ZWindow(
            "Second Example",
            new ZCol(
                new ZLabel(photo[0]),
                new ZPicture().showImage(InOut.readImage(photo[0]))
            ).atTop().atBottom().space(10)
        );

    }
    
}

/**
The atTop() and atBottom() suffixes modifying the ZCol remove margins
from the top and bottom of the column and the space() suffix overrides
ZCol's notion of how much minimum space should be between its objects

The showImage() method is being used as a suffix but is not (that means
showImage is not limited to being used only before a window is
constructed). Here showImage() puts the first (and only) image into 
the ZPicture object.

This is the second example in a series of four. You really ought to
compile and run all of them to get the a feel for how zio's layout 
works.
**/
