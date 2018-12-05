package letterrec;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;


/**
 *
 */
public class MainFrame extends JFrame {

    private static final String TITLE = "Title(WOW)";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 900;


    private int rowCount = 10;
    private int columnCount = 10;

    // �������� ��� ���������
    private Pane drawPane;


    private JTextField textField;
    private JButton btnTeach;


    public MainFrame() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
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

        textField = new JTextField() {{
            setPreferredSize(new Dimension(50, 23));
            addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {}
                public void keyReleased(KeyEvent e) {
                    btnTeach.setEnabled(textField.getText().length() > 0);
                }
                public void keyTyped(KeyEvent e) {}
            });
        }};

        JButton btnClear = new JButton("��������") {{
            addActionListener((ActionEvent e) -> drawPane.clearPane());
        }};

        btnTeach = new JButton("�������") {{
            addActionListener((ActionEvent e) -> learning());
            setEnabled(false);
        }};

        Container symbolContainer = new Container() {{
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(new JLabel("������"));
            add(textField);
            add(btnTeach);
        }};

        pane1.add(new Box(BoxLayout.Y_AXIS) {{
            add(symbolContainer);
            add(drawPane);
            add(btnClear);
        }});

        add(pane1);
    }

    private void learning() {
        int[] tmp = new int[columnCount * rowCount];
        for (int i = 0, p = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (drawPane.getCellPanes()[i][j].getBackground().equals(Color.BLACK)) {
                    tmp[p] = 1;
                    p++;
                } else {
                    tmp[p] = 0;
                    p++;
                }
            }
        }
        System.out.println("��������� ������: " + (textField.getText().equals("") ? "!� ������� �� ���!" : textField.getText()) +
                " � �������� ����� 1 � 0\n" + Arrays.toString(tmp));
    }

}
