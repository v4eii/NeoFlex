package letterrec;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author v4e
 */
public class Pane extends JPanel {

    private int rowCount = 10;
    private int columnCount = 10;
    
    public Pane()
    {
        setLayout(new GridBagLayout());
        
        GridBagConstraints gridBag = new GridBagConstraints();
        for (int row = 0; row < rowCount; row++)
        {
            for (int column = 0; column < columnCount; column++)
            {
                gridBag.gridx = column;
                gridBag.gridy = row;
                
                CellPane pane = new CellPane();
                Border border;
                if (row < 9)
                {
                    if (column < 9)
                    {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    }
                    else
                    {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                }
                else
                {
                    if (column < 9)
                    {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    }
                    else
                    {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                
                pane.setBorder(border);
                add(pane, gridBag);
            }
        }
    }
    
    public class CellPane extends JPanel {

        private final Color defaultColor = getBackground();
        
        public CellPane()
        {
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                }
                
                @Override
                public void mousePressed(MouseEvent e)
                {
                    if (e.getButton() == MouseEvent.BUTTON1)
                    {
                        setBackground(Color.BLACK);
                    }
                    if (e.getButton() == MouseEvent.BUTTON3)
                    {
                        setBackground(defaultColor);
                    }
                }
                
                @Override
                public void mouseReleased(MouseEvent e)
                {
                }
                
                @Override
                public void mouseEntered(MouseEvent e)
                {
                    if (getBackground() != Color.BLACK)
                    {
                        setBackground(Color.LIGHT_GRAY);
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e)
                {
                    if (getBackground() != Color.BLACK)
                    {
                        setBackground(defaultColor);
                    }
                }
            });
        }
        
        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(50, 50);
        }
        
    }
    
}