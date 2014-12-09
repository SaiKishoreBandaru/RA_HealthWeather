package com.learningstorm.kafka;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.spout.Scheme;
import backtype.storm.topology.TopologyBuilder;

public class KafkaTopology {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//
		ZkHosts zkHosts = new ZkHosts("localhost:2181");
		SpoutConfig kafkaConfig = new SpoutConfig(zkHosts,"words_topic","","id7");
		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		kafkaConfig.forceFromStart = true;
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("KafkaSpout",new KafkaSpout(kafkaConfig),1);
		
		builder.setBolt("SentenceBolt",new SentenceBolt(),1).globalGrouping("KafkaSpout");
		builder.setBolt("PrinterBolt", new PrinterBolt(),1).globalGrouping("SentenceBolt");
	
		LocalCluster cluster = new LocalCluster();
		Config conf = new Config();
		cluster.submitTopology("KafkaTopology",conf,builder.createTopology());
		try{
			System.out.println("waiting to consume from kafka");
			Thread.sleep(1000);
			
			
		}catch(Exception e){
			System.out.println("Exception"+ e.toString());
		}
		cluster.killTopology("KafkaTopology");
		cluster.shutdown();
		
		
		
		
		

	}

}
