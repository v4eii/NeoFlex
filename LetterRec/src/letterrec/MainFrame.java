package letterrec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *  @author v4e
 */
public class MainFrame extends JFrame {

    private static final String TITLE = "Title(WOW)";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 480;

    private final int rowCount = 50;
    private final int columnCount = 50;
    
    NeuralNetwork test = new NeuralNetwork(columnCount, rowCount);

    // Панелька для отрисовки
    private static Pane drawPane;

    private JTextField symbolField, resultField;
    private JButton btnTeach, btnRead;

    public MainFrame() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //setResizable(false);
        setVisible(true);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 2 - WIDTH / 2;
        int y = screenSize.height / 2 - HEIGHT / 2;
        this.setBounds(x, y, WIDTH, HEIGHT);

        JPanel pane1 = new JPanel() {{
            setLayout(new BorderLayout());
        }};
        drawPane = new Pane(rowCount, columnCount, WIDTH - 50, HEIGHT - 50);

        symbolField = new JTextField() {{
            setPreferredSize(new Dimension(50, 23));
            addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {}
                public void keyReleased(KeyEvent e) {
                    btnTeach.setEnabled(symbolField.getText().length() > 0);
                }
                public void keyTyped(KeyEvent e) {}
            });
        }};
        
        resultField = new JTextField()
        {{
            setPreferredSize(new Dimension(200, 23));
        }};

        JButton btnClear = new JButton("Очистить") {{
            addActionListener((ActionEvent e) -> drawPane.clearPane());
        }};

        btnTeach = new JButton("Обучить") {{
            addActionListener((ActionEvent e) -> learning());
            setEnabled(false);
        }};
        
        btnRead = new JButton("Считать") {{
            addActionListener((ActionEvent e) -> read());
        }};

        Container symbolContainer = new Container() {{
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(new JLabel("Символ"));
            add(symbolField);
            add(btnTeach);
            add(btnRead);
        }};
        
        Container resultFieldContainer = new Container() 
        {{
                    setLayout(new FlowLayout(FlowLayout.LEFT));
                    add(new JLabel("Результат"));
                    add(resultField);
        }};
        

        pane1.add(new Box(BoxLayout.Y_AXIS) {{
            add(symbolContainer);
            add(resultFieldContainer);
            add(drawPane);
            add(btnClear);
        }});

        add(pane1);
    }

    private void learning()
    {
        drawPane.stretchY();
        drawPane.stretchX();
        test.recCharacter(symbolField.getText());
    }
    
    private void read()
    {
        drawPane.stretchY();
        drawPane.stretchX();
        resultField.setText(test.defCharacter());
    }

    public static Pane getDrawPane()
    {
        return drawPane;
    }

}
