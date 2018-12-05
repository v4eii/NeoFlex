package letterrec;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import javax.swing.border.MatteBorder;


/**
 * @author v4e
 */
public class Pane extends JPanel {

    private static final int SNAKE_TAIL = 5;

    private final int WORKSPACE_WIDTH;
    private final int WORKSPACE_HEIGHT;
    private final Color BACKGROUND_COLOR;
    private final Color[] LAST_MOVED_CELLS_COLOR = new Color[SNAKE_TAIL];
    {
        int tailColor = 192;
        int step = (225 - tailColor)/SNAKE_TAIL;
        for (int i = 0; i < SNAKE_TAIL; i++) {
            LAST_MOVED_CELLS_COLOR[i] = new Color(tailColor, tailColor, tailColor);
            tailColor += step;
        }
    }

    private final int CELL_SIZE;
    private CellPane[][] cellPanes;

    private CellPane[] lastMovedCells = new CellPane[SNAKE_TAIL];


    public Pane(int rowCount, int columnCount, int paneWidth, int paneHeight) {
        int cellSizeByWidth = paneWidth/columnCount;
        int cellSizeByHeight = paneHeight/rowCount;
        CELL_SIZE = cellSizeByWidth < cellSizeByHeight ? cellSizeByWidth : cellSizeByHeight;

        this.WORKSPACE_WIDTH = columnCount * CELL_SIZE;
        this.WORKSPACE_HEIGHT = rowCount * CELL_SIZE;
        this.cellPanes = new CellPane[columnCount][rowCount];
        this.BACKGROUND_COLOR = getBackground();

        setLayout(new GridBagLayout());

        GridBagConstraints gridBag = new GridBagConstraints();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                gridBag.gridx = column;
                gridBag.gridy = row;

                CellPane pane = new CellPane();

                int bottom = row == rowCount - 1 ? 1 : 0;
                int right = column == columnCount - 1 ? 1 : 0;
                pane.setBorder(new MatteBorder(1, 1, bottom, right, Color.GRAY));
                add(pane, gridBag);
                cellPanes[column][row] = pane;
            }
        }

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                CellPane cellPane = getClickedPane(e);
                if (cellPane != null) {
                    cellPane.setBackground(SwingUtilities.isLeftMouseButton(e) ? Color.BLACK : BACKGROUND_COLOR);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                CellPane cellPane = getClickedPane(e);
                if (cellPane != null) {
                    drawCursorAnimation(cellPane);
                }
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                clearCursorAnimation();
                CellPane cellPane = getClickedPane(e);
                if (cellPane != null) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        cellPane.setBackground(Color.BLACK);
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        cellPane.setBackground(BACKGROUND_COLOR);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * Закрашивает ячейки по мере перемешения курсора
     * @param cellPane текушая ячейка над которой курсор
     */ 
    private void drawCursorAnimation(CellPane cellPane) {
        if (BACKGROUND_COLOR.equals(cellPane.getBackground())) {
            if (lastMovedCells[SNAKE_TAIL-1] != null) {
                lastMovedCells[SNAKE_TAIL - 1].setBackground(BACKGROUND_COLOR);
            }
            System.arraycopy(lastMovedCells, 0, lastMovedCells, 1, SNAKE_TAIL - 1);
            lastMovedCells[0] = cellPane;
            for (int i = 0; i < SNAKE_TAIL; i++) {
                if (lastMovedCells[i] != null) {
                    lastMovedCells[i].setBackground(LAST_MOVED_CELLS_COLOR[i]);
                }
            }
        }
    }

    /**
     * Очишает закрашенные ячейки
     */
    private void clearCursorAnimation() {
        for (CellPane cellPane : lastMovedCells) {
            if (cellPane != null) {
                cellPane.setBackground(BACKGROUND_COLOR);
            }
        }
        lastMovedCells = new CellPane[SNAKE_TAIL];
    }

    /**
     * Очищает панель
     */
    public void clearPane() {
        for (int row = 0; row < cellPanes[0].length; row++) {
            for (int column = 0; column < cellPanes.length; column++) {
                cellPanes[row][column].setBackground(BACKGROUND_COLOR);
            }
        }
    }

    /**
     * Определяет над какой ячейкой находится курсор
     *
     * @param e Событие, на действие мыши
     * @return Ячейка над которой находится курсор
     */
    private CellPane getClickedPane(MouseEvent e) {
        // Из коорднат мышки вычетаем расстояния до первой ячейки
        int x = e.getX() - cellPanes[0][0].getX();
        int y = e.getY() - cellPanes[0][0].getY();

        // Проверяем, что курсор находится в рабочей области
        boolean clickedInWorkspace = x >= 0 && y >= 0 && x < WORKSPACE_WIDTH && y < WORKSPACE_HEIGHT;

        if (clickedInWorkspace) {
            return cellPanes[x / CELL_SIZE][y / CELL_SIZE];
        }
        return null;
    }


    public class CellPane extends JPanel {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(CELL_SIZE, CELL_SIZE);
        }

    }

    public CellPane[][] getCellPanes() {
        return cellPanes;
    }

}
