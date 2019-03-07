package letterrec;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.MatteBorder;


/**
 * @author v4e
 */
public class Pane extends JPanel {

    private static final int SNAKE_TAIL = 5;

    private final int WORKSPACE_WIDTH;
    private final int WORKSPACE_HEIGHT;
    private final int columnCount,
                      rowCount;
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
        this.columnCount = columnCount;
        this.rowCount = rowCount;
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
    
    private boolean checkSize(int x, int y)
    {
        for (int i = 0; i < columnCount; i++)
        {
            if (cellPanes[i][x].getBackground().equals(Color.BLACK))
            {
                for (int j = 0; j < rowCount; j++)
                {
                    if (cellPanes[y][j].getBackground().equals(Color.BLACK))
                        return true;
                }
            }
        }
        return false;
    }
    
    public void stretchY()
    {
        clearCursorAnimation();
        
        List<List<Distance>> steps = new ArrayList<>();
        boolean first = true;
        int posY = 0,
            posX = 0,
            lastPosX = 0,
            lastPosY = 0;
        
        if (!steps.isEmpty())
            steps.clear();
        
        for (int i = 0; i < columnCount; i++)
        {
            for (int j = 0; j < rowCount; j++)
            {
                if (cellPanes[i][j].getBackground().equals(Color.BLACK) || checkSize(j, i))
                {
                    if (first)
                    {
                        posX = j+1;
                        posY = i+1;
                        first = false;
                    }
                        lastPosX = j + 1;
                        lastPosY = i + 1;
                }
            }
        }
        
        System.out.println(posX + " " + posY);
        System.out.println(lastPosX + " " + lastPosY);
        System.out.println("weight = " + (lastPosX - posX + 1));
        System.out.println("height = " + (lastPosY - posY + 1));
        
        int symbolSizeX = lastPosX - posX + 1;
        
        float oldStep = 1f / (float) symbolSizeX;
        
        int posList = 0;
        int posNumber;
        for (int i = posY - 1; i < lastPosY; i++)      // Хз как, но X - это по Y, гыг
        {
            float tmp = 0f;
            posNumber = 0;
            for (int j = posX - 1; j < lastPosX; j++)
            {
                if (cellPanes[i][j].getBackground().equals(Color.BLACK))
                {
                    if (posList > steps.size() - 1 || steps.isEmpty())
                    {
                        steps.add(new ArrayList<>());
                        if (posList > steps.size() - 1)
                        {
                            while (posList > steps.size() - 1)
                                posList--;
                        }
                    }
                    steps.get(posList).add(new Distance(tmp));
                    
                    for (int k = j+1;; k++)
                    {
                        tmp += oldStep;
                        if (k >= lastPosX)
                        {
                            steps.get(posList).get(posNumber).setEndPos(tmp);
                            posNumber++;
                            j = k;
                            break;
                        }
                        if (!cellPanes[i][k].getBackground().equals(Color.BLACK))
                        {
                            steps.get(posList).get(posNumber).setEndPos(tmp);
                            posNumber++;
                            j = k;
                            break;
                        }
                    }
                }
                tmp += oldStep;
            }
            posList++;
        }
        
        steps.forEach((t) ->
        {
            System.out.println(t.toString());
        });
        
        float step = 1f / (float) columnCount;

        posList = 0;
        posNumber = 0;
        clearPane();
        for (int i = 0; i < columnCount; i++)
        {
            float tmp = 0;
            if (posList > steps.size() - 1)
                break;
            for (int j = 0; j < rowCount; j++)
            {
                if (posNumber > steps.get(posList).size() - 1)
                    break;
                if (tmp >= steps.get(posList).get(posNumber).getStartPos()
                        || (tmp + oldStep/2 > steps.get(posList).get(posNumber).getStartPos() && j < rowCount/2))
                {
                    while (tmp <= steps.get(posList).get(posNumber).getEndPos() - oldStep/2 && j < rowCount/2) // мб step
                    {
                        cellPanes[i][j].setBackground(Color.BLACK);
                        tmp += step;
                        j++;
                    }
                    while (tmp < steps.get(posList).get(posNumber).getEndPos() && j >= rowCount/2)
                    {
                        if (j >= rowCount)
                            break;
                        cellPanes[i][j].setBackground(Color.BLACK);
                        tmp += step;
                        j++;
                    }
                    posNumber++;
                }
                tmp += step;
            }
            posList++;
            posNumber = 0;
        }
    }
    
    public void stretchX()
    {
        clearCursorAnimation();
        
        List<List<Distance>> steps = new ArrayList<>();
        boolean first = true;
        int posY = 0,
            posX = 0,
            lastPosX = 0,
            lastPosY = 0;
        
        if (!steps.isEmpty())
            steps.clear();
        
        for (int i = 0; i < columnCount; i++)
        {
            for (int j = 0; j < rowCount; j++)
            {
                if (cellPanes[j][i].getBackground().equals(Color.BLACK) || checkSize(i, j))
                {
                    if (first)
                    {
                        posX = j+1;
                        posY = i+1;
                        first = false;
                    }
                        lastPosX = j + 1;
                        lastPosY = i + 1;
                }
            }
        }
        
        System.out.println(posX + " " + posY);
        System.out.println(lastPosX + " " + lastPosY);
        System.out.println("weight = " + (lastPosX - posX + 1));
        System.out.println("height = " + (lastPosY - posY + 1));
        
        int symbolSizeX = lastPosX - posX + 1;
        
        float oldStep = 1f / (float) symbolSizeX;
        
        int posList = 0;
        int posNumber;
        for (int i = posY - 1; i < lastPosY; i++)      // Хз как, но X - это по Y, гыг
        {
            float tmp = 0f;
            posNumber = 0;
            for (int j = posX - 1; j < lastPosX; j++)
            {
                if (cellPanes[j][i].getBackground().equals(Color.BLACK))
                {
                    if (posList > steps.size() - 1 || steps.isEmpty())
                    {
                        steps.add(new ArrayList<>());
                        if (posList > steps.size() - 1)
                        {
                            while (posList > steps.size() - 1)
                            {
                                posList--;
                            }
                        }
                    }
                    steps.get(posList).add(new Distance(tmp));
                    
                    for (int k = j+1;; k++)
                    {
                        tmp += oldStep;
                        if (k >= lastPosX)
                        {
                            steps.get(posList).get(posNumber).setEndPos(tmp);
                            posNumber++;
                            j = k;
                            break;
                        }
                        if (!cellPanes[k][i].getBackground().equals(Color.BLACK))
                        {
                            steps.get(posList).get(posNumber).setEndPos(tmp);
                            posNumber++;
                            j = k;
                            break;
                        }
                    }
                }
                tmp += oldStep;
            }
            posList++;
        }
        
        steps.forEach((t) ->
        {
            System.out.println(t.toString());
        });
        
        float step = 1f / (float) columnCount;

        posList = 0;
        posNumber = 0;
        clearPane();
        for (int i = 0; i < columnCount; i++)
        {
            float tmp = 0;
            if (posList > steps.size() - 1)
                break;
            for (int j = 0; j < rowCount; j++)
            {
                if (posNumber > steps.get(posList).size() - 1)
                    break;
                if (tmp >= steps.get(posList).get(posNumber).getStartPos()
                        || (tmp + oldStep/2 > steps.get(posList).get(posNumber).getStartPos() && j < rowCount/2))
                {
                    while (tmp <= steps.get(posList).get(posNumber).getEndPos() - oldStep/2 && j < rowCount/2) // мб step
                    {
                        cellPanes[j][i].setBackground(Color.BLACK);
                        tmp += step;
                        j++;
                    }
                    while (tmp < steps.get(posList).get(posNumber).getEndPos() && j >= rowCount/2)
                    {
                        if (j >= rowCount)
                            break;
                        cellPanes[j][i].setBackground(Color.BLACK);
                        tmp += step;
                        j++;
                    }
                    posNumber++;
                }
                tmp += step;
            }
            posList++;
            posNumber = 0;
        }
    }

    public CellPane[][] getCellPanes() {
        return cellPanes;
    }
    
    class Distance
    {
        private Float startPos;
        private Float endPos;
        private boolean used = false;

        public boolean isUsed()
        {
            return used;
        }

        public void setUsed(boolean used)
        {
            this.used = used;
        }

        public Float getStartPos()
        {
            return startPos;
        }

        public void setStartPos(Float startPos)
        {
            this.startPos = startPos;
        }

        public Float getEndPos()
        {
            return endPos;
        }

        public void setEndPos(Float endPos)
        {
            this.endPos = endPos;
        }

        public Distance()
        {
        }

        public Distance(Float startPos)
        {
            this.startPos = startPos;
        }
        
        @Override
        public String toString()
        {
            return startPos + " " + endPos;
        }
        
    }
}
