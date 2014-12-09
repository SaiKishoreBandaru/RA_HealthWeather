import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFrame;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class TreeVisualizationBolt extends BaseBasicBolt {

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		 		   Instances data = new Instances(new BufferedReader(new
				   FileReader("dataset/ddata.arff")));
				   data.setClassIndex(data.numAttributes() - 1);
				   J48 classifier = new J48();
				   classifier.buildClassifier(data);
				   TreeVisualizer tv = new TreeVisualizer(null, classifier.graph(), new PlaceNode2());
				   ￼￼￼JFrame framea =null;
				   framea=new javax.swing.JFrame("Tree Visualizer");
				   framea.setSize(800, 500);
				   framea.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				   framea.getContentPane().add(tv);
				   framea.setVisible(true);
				   tv.fitToScreen();
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text"));
	}

}
