// Fourth of four examples, see jazimmer.net/source_code/java/zio.html
// Discussed in detail in jazimmer.net/source_code/java/zio/slices.html
// copyright 2005 by J Adrian Zimmer
// Licensed under the Open Software License version 2.1

// On July 13, 2005, a ZButton class was added to the zio package
// and handling of global colors was changed.  This example
// has been updated to reflect those changes.

import zio.*;      
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
    
/*
This example shows how Swing classes can be integrated with the zio
package.

There will be a separate button class for each button object. Each
button class knows its label and which text area it should work with.
Each button class also has an action() method that is invoked
when the any instantiated button is clicked.  We control what
a button does by overriding that action() method.
*/

class ClearButton extends ZButton {
    
    private JTextArea text;   
        // so button can know which JTextArea to play with
        
    public ClearButton(JTextArea text) {
        super("clear");
        this.text = text;
    }
        
    protected void action() {
        text.setText("");
    }  

}
    
class PrintButton extends ZButton {
    
    private JTextArea text;

    public PrintButton(JTextArea text) {
        super("print the text");
        this.text = text;
    }
        
    public void action() {
        Zio.show(text.getText());
    }

}

/*
The main method instantiates a button of each kind but only
after creating a JTextArea for them to play with.  Since
JTextAreas are not a part of the zio package, this one
must be wrapped in a ZComponent object before it can be
used in a ZWindow. The zio package controls placement and
sizing so the size of the JTextArea is determined by a suffix
to the wrapper ZComponent object.
*/

public class FourthExample {
    
    private static final Dimension BUTTONSIZE = new Dimension(120,28);
    
    public static void main( String [] junk) {

        JTextArea text = new JTextArea(); 
        text.setForeground(Color.BLUE);
        ZComponent textComponent = 
            new ZComponent(
                new JScrollPane( text )   
            ).expandH().expandV().size( new Dimension(200,200) );
        
        ZButton show =  new PrintButton(text).minSize(BUTTONSIZE);
        ZButton clear = new ClearButton(text).minSize(BUTTONSIZE);
        
        Zio.setGlobalBackground( Color.BLACK );
        Zio.setGlobalForeground( Color.WHITE );
        ZWindow w = new ZWindow(
            "Text Entry",
            new ZRow( 
                new ZCol( show, clear ).space(30),
                new ZCol( textComponent ).space(30).unlock()
            ).unlock()                         
        );
    }

}
    
/*
The button size was ascertained by running an early version of this
program with some debug suffixes so I would know the largest size of a
button. This is poor way of doing things. Watch for a much nicer way
with a later version of zio.

The second ZCol is unnecessary but forces margins to appear above and
below the text area. 

When mixing Swing and zio, let zio handle all matters relating to
Dimension and layout.  For Swing components let Swing handle
everything else.  Hence in this example, we set the foreground color 
of the text area using Swing not zio.

This is the last example in a series of four. You really ought to
compile and run all of them to get the a feel for how zio's layout 
works.
*/

