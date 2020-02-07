package com.iisigroup.product.iip.utility.logTransmitter;

public interface IKafkaConstants {
	public static String KAFKA_BROKER = "192.168.62.249:9092";
	public static Integer MESSAGE_COUNT = 10000;
	public static String CLIENT_ID = "client1";
	public static String TOPIC_NAME = "testTopic1";
	public static String GROUP_ID_CONFIG = "consumerGroup1";
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;
	public static String OFFSET_REST_LATEST = "latest";
	public static String OFFSET_REST_EARLIER = "earliest";
	public static Integer MAX_POLL_RECORDS = 1;
}
