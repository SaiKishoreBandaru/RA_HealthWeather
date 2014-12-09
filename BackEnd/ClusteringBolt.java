import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instances;
import weka.clusterers.EM;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;


public class ClusteringBolt extends BaseBasicBolt implements Serializable {

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		//load data
		Instances data = null;
		try {
			data = new Instances(new BufferedReader(new
   FileReader("dataset/data.arff")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	       // new instance of clusterer
	       EM model = new EM();
	       // build the clusterer
	       try {
			model.buildClusterer(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	       System.out.println(model);

		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text"));
	}

}
