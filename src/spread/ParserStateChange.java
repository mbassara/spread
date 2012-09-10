package spread;

public class ParserStateChange {
	
	Object source;
	int progress;
	int taskSize;
	String info;
	
	public ParserStateChange() {
		this(null, 0, 0, "");
	}
	
	public ParserStateChange(Object source, int progress, int taskSize, String info) {
		this.source = source;
		this.progress = progress;
		this.taskSize = taskSize;
		this.info = new String("info");
	}
	
	public void setParameters(Object source, int progress, int taskSize, String info) {
		this.source = source;
		this.progress = progress;
		this.taskSize = taskSize;
		this.info = new String("info");
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getTaskSize() {
		return taskSize;
	}

	public void setTaskSize(int taskSize) {
		this.taskSize = taskSize;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
