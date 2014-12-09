import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import weka.clusterers.EM;
import weka.core.Instance;
import weka.core.Instances;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class ClusterClassifyBolt extends BaseBasicBolt implements Serializable {

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		
		// load data
		Instances data = null;
		try {
			data = new Instances(new BufferedReader(new FileReader(
					"dataset/bank-data.arff")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// remove the first instance from dataset
		Instance inst = data.instance(0);
		data.delete(0);
		// new instance of clusterer
		EM model = new EM();
		// build the clusterer
		try {
			model.buildClusterer(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// classify instance
		int cls = 0;
		try {
			cls = model.clusterInstance(inst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Cluster: " + cls);
		double[] dist = null;
		try {
			dist = model.distributionForInstance(inst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < dist.length; i++)
			System.out.println("Cluster " + i + ".\t" + dist[i]);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text"));

	}

}
