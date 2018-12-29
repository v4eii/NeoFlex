package letterrec;

/**
 *
 * @author v4e
 */

public class Symbols {
    private int countPaintedPanel;
    private float[][] weights;
    private char symbol;
    private float coefSymbol;

    public Symbols(int columnCount, int rowCount, char symbol)
    {
        this.symbol = symbol;
        weights = new float[columnCount][rowCount];
        for (int i = 0; i < rowCount; i++)
        {
            for (int j = 0; j < columnCount; j++)
            {
                weights[i][j] = 0;
            }
        }
    }

    public char getSymbol()
    {
        return symbol;
    }

    public int getCountPaintedPanel()
    {
        return countPaintedPanel;
    }

    public float[][] getWeights()
    {
        return weights;
    }

    public void setCountPaintedPanel(int countPaintedPanel)
    {
        this.countPaintedPanel = countPaintedPanel;
    }

    public void setWeights(float[][] weights)
    {
        this.weights = weights;
    }

    public void setCoefSymbol(float coefSymbol)
    {
        this.coefSymbol = coefSymbol;
    }

    public float getCoefSymbol()
    {
        return coefSymbol;
    }
    
}
