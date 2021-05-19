// First of four examples, see jazimmer.net/source_code/java/zio.html 
// copyright 2005 by J Adrian Zimmer
// Licensed under the Open Software License version 2.1

import zio.*;      
import java.awt.*; // for Java AWT's Color class

/**
Make sure zio.jar is on your classpath before you compile! For example,
by putting it in all the lib/ext folders you can find. 

This program creates two windows. The windows appear in a cascade form
and the program will end when the user has closed both of them. 
**/

public class FirstExample {

    private static void TheFirstWayToMakeAWindow() {
        Zio.setGlobalBackground(Color.WHITE);
        Zio.setGlobalForeground(Color.BLUE);
        ZLabel zp = new ZLabel("Hellow Orld");
        ZWindow w = new ZWindow("First Example", zp );
    }
    
    private static void TheSecondWayToMakeAWindow() {
        ZWindow.setBackgroundColor(Color.WHITE);
        ZWindow w = new ZWindow(
            "First Example",
             new ZLabel("Hellow Orld").foreground(Color.BLUE)
        );   
    }

    public static void main( String [] ignore) {
        TheFirstWayToMakeAWindow();
        TheSecondWayToMakeAWindow();
    }

    
}

/**
The foreground() method used in the TheSecondWayToMakeAWindow is called
a "suffix". This suffix is missing from TheFirstWayToMakeAWindow().
Suffixes are very common in the zio package.  Suffixes can be
can be appended after an object is instantiated as with

     new ZLabel("Hellow Orld").foreground(Color.BLUE)
   
The can be used in other contexts but ONLY BEFORE THE WINDOW CONTAINING
THE OBJECT IS INSTANTIATED (with ZWindow).

In TheSecondWayToMakeAWindow the BLUE foreground color applies only to
the ZLabel whereas in TheFirstWayToMakeAWindow the BLUE foreground
color applies to everything. Of course in this example, the only use of
the foreground color is for the letters in "Hellow Orld" so the two
windows look the same.


Here are two rules that help explain limitations of the zio package:

      NO OBJECT CAN BE USED MORE THAN ONCE.
      NO OBJECT CAN BE REPLACED ONCE PUT INTO A WINDOW.

The rules are similar. Enforcing them saves a lot of potential
problems. For this example, the first rule means that the code cannot
be rewritten so that there is just one +/c{ZLabel} object displaying
"Hellow Orld".

This is the first example in a series of four. You really ought to
compile and run all of them to get the a feel for how zio's layout 
works.
**/
