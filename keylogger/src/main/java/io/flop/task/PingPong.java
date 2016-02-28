package io.flop.task;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong implements StreamTask {

	private static final Logger logger = LoggerFactory.getLogger(PingPong.class);
	private static final SystemStream outputStream = new SystemStream("kafka", "ping-pong");

	@Override
	public void process(IncomingMessageEnvelope envelope, MessageCollector collector, TaskCoordinator coordinator) throws InterruptedException {
		Object message = envelope.getMessage();
		logger.info("Ping <-- {}", message);
		Thread.sleep(1000);
		collector.send(new OutgoingMessageEnvelope(outputStream, message));
		logger.info("Pong --> {}", message);
	}
}
