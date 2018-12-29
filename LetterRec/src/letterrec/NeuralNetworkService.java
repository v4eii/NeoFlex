package letterrec;

import java.awt.Color;


/**
 *  @author v4e
 */
public class NeuralNetworkService {

    //TODO: Данный класс использовать как набор методов. Для нейронной сети создать отдельный класс
    //TODO: Реализовать алгоритм обучения нейронной сети Розенблата
    //TODO: Реализовать алгоритм распознования символа
    //TODO: Реализовать алгоритм определения границы нарисованного символа
    //TODO: Реализовать алгоритм растягивания символа на всю заданную сетку
    //TODO: Реализовать алгоритм самообучения сети на основе эталонного изображения
    private static Pane drawPane = MainFrame.getDrawPane();
    private static final float COEFFICIENT = 0.5f;
    
    public static int[][] searchPaintedPanel(int rowCount, int columnCount)
    {
        int[][] paintedPanel = new int[columnCount][rowCount];
        for (int i = 0; i < rowCount; i++)
        {
            for (int j = 0; j < columnCount; j++)
            {
                if (drawPane.getCellPanes()[i][j].getBackground().equals(Color.BLACK))
                {
                    paintedPanel[i][j] = 1;
                }
                else
                {
                    paintedPanel[i][j] = 0;
                }
            }
        }
        return paintedPanel;
    }
    
    public static int calcCountPaintedPanel(int rowCount, int columnCount, int[][] paintedPanel)
    {
        int countPaintedPanel = 0;
        for (int i = 0; i < rowCount; i++)
        {
            for (int j = 0; j < columnCount; j++)
            {
                if (paintedPanel[i][j] == 1)
                    countPaintedPanel++;
            }
        }
        System.out.println("Count : " + countPaintedPanel);
        return countPaintedPanel;
    }
    
    public static float calcResultWeight(int rowCount, int columnCount, int[][] paintedPanel, float[][] weights)
    {
        float summWeight = 0f;
        float result[][] = new float[columnCount][rowCount];
        for (int i = 0; i < rowCount; i++)
        {
            for (int j = 0; j < columnCount; j++)
            {
                result[i][j] = paintedPanel[i][j] * weights[i][j];
                summWeight += result[i][j];
            }
        }
        System.out.println("Summ Weight: " + summWeight);
        
        return summWeight;
    }
    
    public static float[][] recalcWeight(int rowCount, int columnCount, float[][] oldWeights,
            int countPaintedPanel, float summWeight, int[][] paintedPanel)
    {
        float[][] newWeights = new float[columnCount][rowCount];
        for (int i = 0; i < rowCount; i++)
        {
            for (int j = 0; j < columnCount; j++)
            {
                newWeights[i][j] = oldWeights[i][j] + COEFFICIENT * (countPaintedPanel > summWeight ? 1 : -1) * paintedPanel[i][j];
            }
        }
        return newWeights;
    }
    
    
}
