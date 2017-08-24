package da.cascading;

import da.graph.AccidentalHBGraph;
import da.graph.HappensBeforeGraph;
import da.graph.LogInfoExtractor;

public class CascadingAnalyzer {

	String projectDir;
	HappensBeforeGraph hbg;
	AccidentalHBGraph ag;
	
	
	public CascadingAnalyzer(String projectDir, HappensBeforeGraph hbg, AccidentalHBGraph ag) {
		this.projectDir = projectDir;
		this.hbg = hbg;
		this.ag = ag;
	}
	
	public void doWork() {
	    //Lock
	    LockCase lockCase = new LockCase(this.projectDir, this.hbg, this.ag, this.ag.getLogInfoExtractor());
	    lockCase.doWork();
		
	    //Queue
	    QueueCase queueCase = new QueueCase(this.projectDir, this.hbg, this.ag.getLogInfoExtractor());
	    queueCase.doWork();
		
	}
}
