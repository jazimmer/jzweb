import zio.*;

public class EchoDialog {
    
    public static void main( String [] ignore ) {
        WorkingDialog w = new WorkingDialog();
    }
    
}

class WorkingDialog extends DialogWindow {
    
    public WorkingDialog() {
        super(
            "Echo Window",
            "Enter text and I will echo it."
        );
    }
    
    public void processInput(String userin) {
        displayString(userin);
    }

}

// Exercise:  Rewrite this so that, instead of just repeating
// what the user wrote, it tacks the phrase "you wrote:" on the
// front.

