// Variant example. This makes little sense without
//      http://jazimmer.net/source_code/java/zio/ThirdExample.java

// copyright 2005 by J Adrian Zimmer
// Licensed under the Open Software License version 2.1

import zio.*;      
import java.awt.*; // for Java AWT's Color and Dimension classes

/*
VARIATION keeps first and last R and Y lined up with B's

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

The trick here is two fold:

    get rid of the margins in the row containing the R's and the column
    containing the Y's

    make the objects in the first column have the same minimum/preferred
    heights as the objects in the second column

An easier way to accomplish this will appear in a later version of zio
*/

public class ThirdExampleVariation {
    
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
                        square(20,Color.WHITE).debug("invisible"),                        new ZCol(
                            square(20,Color.YELLOW),
                            square(20,Color.YELLOW),
                            square(20,Color.YELLOW)
                        ).atTop().atBottom().space(70).debug("yellow")
                    ),
                    new ZCol(
                        new ZRow(
                            square(20,Color.RED),
                            square(20,Color.RED),
                            square(20,Color.RED)
                        ).atLeft().atRight().debug("red"),
                        square(200,Color.BLUE)
                        .debug("blue").expandH().expandV()
                    ).unlock()
                ).unlock()
            );
    }
    
}
