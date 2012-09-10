package spread;

import com.mxgraph.analysis.mxICostFunction;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxCellState;

public class myCostFunction implements mxICostFunction {

	@Override
	public double getCost(mxCellState state) {
		mxCell cell = (mxCell) state.getCell();
		EdgeValue value = (EdgeValue) cell.getValue();
		return value.getCost();			// koszt przejścia po krawędzi to jej waga która jest zapisana w polu cost klasy EdgeValue
	}

}
