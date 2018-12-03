package letterrec;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author v4e
 */
public class LetterRec {

    
    private int rowCount = 15,
                columnCount = 15;
    
    // Панелька для отрисовки
    private Pane drawPane;
    
    public static void main(String[] args)
    {
        new LetterRec();
    }
    
    public LetterRec()
    {
        EventQueue.invokeLater(() ->
        {
            JFrame frame = new JFrame("Title(WOW)");
            
            // Панель содержащая текстовое поле и кнопку "Обучить"
            JPanel pane = new JPanel();
            
            // Панель содержащая drawPanel и кнопку "Очистить"
            JPanel pane1 = new JPanel();
            drawPane = new Pane(rowCount, columnCount);
           
            JTextField textField = new JTextField();
            textField.setPreferredSize(new Dimension(250, 23));
            
            JLabel label = new JLabel("Символ");
            JButton btnClear = new JButton("Очистить");
            btnClear.setPreferredSize(new Dimension(200,23));
            btnClear.addActionListener((ActionEvent e) ->
            {
                for (int row = 0; row < rowCount; row++)
                {
                    for (int column = 0; column < columnCount; column++)
                    {
                        drawPane.getCellPanes()[row][column].setBackground(drawPane.getBackgroundColor());
                    }
                }
            });
            
            JButton btnTeach = new JButton("Обучить");
            btnTeach.addActionListener((ActionEvent e) ->
            {
                int tmp[] = new int[columnCount*rowCount];
                for (int i = 0; i < rowCount; i++)
                {
                    for (int j = 0; j < columnCount; j++)
                    {
                        if (drawPane.getCellPanes()[i][j].getBackground().equals(Color.BLACK))
                            tmp[i+j] = 1;
                        else
                            tmp[i+j] = 0;
                    }
                }
                System.out.println(textField.getText() + Arrays.toString(tmp));             // TODO дописать
            });
            
            pane.setPreferredSize(new Dimension(400, 250));
            pane.setLayout(new FlowLayout());
            pane.add(label);
            pane.add(textField);
            pane.add(btnTeach);
            
            pane1.setPreferredSize(new Dimension(750, 800));
            pane1.setLayout(new FlowLayout());
            pane1.add(drawPane);
            pane1.add(btnClear);
            
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());
            frame.add(pane);
            frame.add(pane1);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setMinimumSize(new Dimension(1200, 800));
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
    
}
