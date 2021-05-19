// Third of four examples, see jazimmer.net/source_code/java/zio.html
// copyright 2005 by J Adrian Zimmer
// Licensed under the Open Software License version 2.1

import zio.*;      
import java.awt.*; // for Java AWT's Color and Dimension classes

/*
Display a window with this pattern

                R  R  R

              Y BBBBBBB
                BBBBBBB
              Y BBBBBBB
                BBBBBBB
              Y BBBBBBB

where 
              the three R's are red and make up a row
              the three Y's are yellow and make up a column
              the B's are one expanding blue object

This window is constructed around two columns. The first contains the Y
objects and the second contains a row of R objects and the large B
object.

To make the first column behave like the second, the Y objects are
placed in their own column within the first column and an invisible
object is placed in the upper left hand corner.

Since a ZWindow can have only one object in it, an outer row is
created to contain the two columns.
*/

public class ThirdExample {
    
    private static ZBox square( int size, Color color ) {
        return 
        new ZBox().size( new Dimension( size, size )).color(color);
    }
    
    public static void main( String [] ignore ) {
        Zio.setGlobalBackground(Color.WHITE);
        ZWindow w = 
            new ZWindow(
                "Third Example",
                new ZRow(
                    new ZCol(
                        square(20,Color.WHITE).debug("invisible"),
                        new ZCol(
                            square(20,Color.YELLOW),
                            square(20,Color.YELLOW),
                            square(20,Color.YELLOW)
                        ).debug("yellow")
                    ),
                    new ZCol(
                        new ZRow(
                            square(20,Color.RED),
                            square(20,Color.RED),
                            square(20,Color.RED)
                        ).debug("red"),
                        square(200,Color.BLUE)
                        .debug("blue").expandH().expandV()
                    ).unlock()
                ).unlock()
            );
    }

}
    
/*   
ZObjects do not normally expand when the user resizes a window. Rows
stretch horizontally and columns stretch vertically but that's it
unless you use suffixes to change things.
    
Here, the expandH() and expandV() suffixes make the large blue square
expandable.  However, just because the large blue square can expand,
it doesn't mean the user can expand it.  Columns cannot normally be
expanded horizontally and rows cannot normally be expanded vertically.
The unlock() suffix unlocks rows and columns so they can be expanded
in two directions.


By the way, it is possible to get the red and yellow squares to line 
up with the edges of the big blue square.  Check out

   http://jazimmer.net/source_code/java/zio/ThirdExampleVariation.java


This is the third example in a series of four. You really ought to
compile and run all of them to get the a feel for how zio's layout 
works.
*/

