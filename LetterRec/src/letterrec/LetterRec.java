package letterrec;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author v4e
 */
public class LetterRec {

    public static void main(String[] args)
    {
        new LetterRec();
    }
    
    public LetterRec()
    {
        EventQueue.invokeLater(() ->
        {
            JFrame frame = new JFrame("Òåñò");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new Pane(10 ,15));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setMinimumSize(new Dimension(600, 600));
            frame.setVisible(true);
        });
    }
    
}
