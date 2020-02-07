package com.iisigroup.product.iip.utility.logTransmitter;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import com.iisigroup.product.iip.utility.logTransmitter.IKafkaConstants;
import com.streambase.sb.DataType;
import com.streambase.sb.Schema;
import com.streambase.sb.StreamBaseException;
import com.streambase.sb.Tuple;
import com.streambase.sb.adapter.kafka.TupleSerializer;


public class logTransmitter {

	public static Producer<String, Tuple> createProducer() {
		Properties props = new Properties();
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("bootstrap.servers", "192.168.62.249:9092");
		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TupleSerializer.class.getName());
		return new KafkaProducer<>(props);
	}

	public static void main(String[] args) throws StreamBaseException {
		Producer<String, Tuple> producer = logTransmitter.createProducer();
		Schema outSchema=new Schema("pro");
		Schema.createField(DataType.STRING,"Field1");
		Schema.createField(DataType.STRING,"Field2");
		Schema.createField(DataType.STRING,"Field3");
		Tuple testTuple=outSchema.createTuple();
		testTuple.setString("Field1", "test1");
		testTuple.setString("Field2", "test2");
		testTuple.setString("Field3", "test3");
//		for(int i = 0; i < 10; i++)
	         System.out.println(producer.send(new ProducerRecord<String, Tuple>(
	        		 "testTopic1",testTuple)).isCancelled());
		producer.close();
				
	}
}
