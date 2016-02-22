package io.flop.task;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyLogger implements StreamTask {

	private static final Logger logger = LoggerFactory.getLogger(KeyLogger.class);

	@Override
	public void process(IncomingMessageEnvelope envelope, MessageCollector collector, TaskCoordinator coordinator) {
		Object message = envelope.getMessage();
		logger.info("Key <-- {}", message);
	}
}
