package letterrec;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author v4e
 */
public class NeuralNetwork 
{
    private int columnCount, rowCount;
    private int[][] paintedPanel = new int[columnCount][rowCount];
    private float summWeight;
    private List<Symbols> characters = new ArrayList<>();
    private List<Symbols> suitChars = new ArrayList<>();
    private Symbols activeSymbol;

    public NeuralNetwork(int columnCount, int rowCount)
    {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }
    
    public void recCharacter(String symbol)
    {
        findActiveSymbol(symbol);
        paintedPanel = NeuralNetworkService.searchPaintedPanel(rowCount, columnCount);
        activeSymbol.setCountPaintedPanel(NeuralNetworkService.calcCountPaintedPanel(rowCount, columnCount, paintedPanel));
        summWeight = NeuralNetworkService.calcResultWeight(rowCount, columnCount, paintedPanel, activeSymbol.getWeights());
        if (summWeight < (float) activeSymbol.getCountPaintedPanel())
        {
            activeSymbol.setWeights(NeuralNetworkService.recalcWeight(rowCount, columnCount, activeSymbol.getWeights(),
                    activeSymbol.getCountPaintedPanel(), summWeight, paintedPanel));
            for (int i = 0; i < rowCount; i++)
            {
                for (int j = 0; j < columnCount; j++)
                {
                    System.out.print(activeSymbol.getWeights()[i][j] + " ");
                }
                System.out.println("");
            }
            activeSymbol.setCoefSymbol(summWeight - activeSymbol.getCountPaintedPanel());
        }
        else
        {
            defCharacter();
        }
    }
    
    public String defCharacter()
    {
        suitChars.clear();
        paintedPanel = NeuralNetworkService.searchPaintedPanel(rowCount, columnCount);
        for (Symbols val : characters)
        {
            summWeight = NeuralNetworkService.calcResultWeight(rowCount, columnCount, paintedPanel, val.getWeights());
            if (summWeight >= val.getCountPaintedPanel())
            {
                suitChars.add(val);
            }
        }
        
        if (suitChars.isEmpty())
            return "Символ не найден";
        else
        {
            Symbols correctSym = suitChars.get(0);
            for (Symbols val : suitChars)
            {
                if (val.getCoefSymbol() > correctSym.getCoefSymbol())
                    correctSym = val;
            }
            return "Это символ: " + String.valueOf(correctSym.getSymbol());
        }
    }
    
    private void findActiveSymbol(String symbol)
    {
        activeSymbol = null;
        for (Symbols val : characters)
        {
            if (val.getSymbol().equals(symbol))
                activeSymbol = val;
        }
        if (activeSymbol == null)
        {
            activeSymbol = new Symbols(columnCount, rowCount, symbol);
            characters.add(activeSymbol);
        }
    }

    public List<Symbols> getCharacters()
    {
        return characters;
    }

}
