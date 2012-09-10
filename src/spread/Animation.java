package spread;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.util.mxAnimation;
import com.mxgraph.view.mxGraph;

public class Animation extends mxAnimation {
	
	mxGraph graph;
	Object[] path;
	int currentIndex;
	
	public Animation(mxGraph graph, Object[] path){
		super(500);				// mxAnimation(int delay)
		this.graph = graph;
		this.path = path;
		currentIndex = 0;
	}
	
	@Override
	public void updateAnimation(){
		if(currentIndex == path.length){
			this.stopAnimation();
		}
		else {
			if(currentIndex == 0)
				for(Object cell : path)
					((mxCell) cell).setStyle(null);
			if(((mxCell) path[currentIndex]).isVertex())
				((mxCell) path[currentIndex]).setStyle("fillColor=#FF3D3D");
			else if(((mxCell) path[currentIndex]).isEdge())
				((mxCell) path[currentIndex]).setStyle("strokeWidth=2;strokeColor=#008A00");
			graph.refresh();
			currentIndex++;
		}
	}
}
