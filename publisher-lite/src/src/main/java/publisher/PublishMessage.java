
package publisher;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsublite.CloudRegion;
import com.google.cloud.pubsublite.CloudZone;
import com.google.cloud.pubsublite.MessageMetadata;
import com.google.cloud.pubsublite.ProjectNumber;
import com.google.cloud.pubsublite.TopicName;
import com.google.cloud.pubsublite.TopicPath;
import com.google.cloud.pubsublite.cloudpubsub.Publisher;
import com.google.cloud.pubsublite.cloudpubsub.PublisherSettings;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PublishMessage {

	public static void main(String[] args) throws Exception {

		String cloudRegion;
		char zoneId;
		String topicId;
		int partitionCount;
		String replicas;
		long projectNumber;
		long delayinms;

		try {
			
			// We need to read environment variables. This is under a TRY/CATCH to avoid
			// possible human errors while setting up variables
			
			cloudRegion = System.getenv("PUBSUB_CLOUD_REGION");
			zoneId = System.getenv("PUBSUB_CLOUD_ZONE_ID").charAt(0);
			topicId = System.getenv("PUBSUB_TOPIC_ID");
			partitionCount = Integer.parseInt(System.getenv("PUBSUB_PARTITION_COUNT"));
			replicas = System.getenv("PUBSUB_REPLICAS");
			projectNumber = Long.parseLong(System.getenv("PUBSUB_PROJECT_NUMBER"));
			delayinms = Long.parseLong(System.getenv("PUBSUB_DELAY_MS"));

			// This try is because we need to catch possible exceptions while creating the publisher
			try {

				// Creating publisher settings

				TopicPath topicPath = TopicPath.newBuilder().setProject(ProjectNumber.of(projectNumber))
						.setLocation(CloudZone.of(CloudRegion.of(cloudRegion), zoneId)).setName(TopicName.of(topicId))
						.build();
				Publisher publisher = null;
				PublisherSettings publisherSettings = PublisherSettings.newBuilder().setTopicPath(topicPath).build();

				// Creating publisher

				publisher = Publisher.create(publisherSettings);

				// Start the publisher. Upon successful starting, its state will become RUNNING.
				// We really NEED to wait until the state is RUNNING
				// We will use the same publisher so we do not expend much data
				
				publisher.startAsync().awaitRunning();

				// This try and while is to 
				while (!Thread.currentThread().isInterrupted()) {
					try {
						publisher(cloudRegion, zoneId, projectNumber, topicId, delayinms, partitionCount,
								replicas, publisher);
						Thread.sleep(delayinms);
					} catch (InterruptedException ex) {
						System.out.println("Stopping");
						publisher.stopAsync().awaitTerminated();
						Thread.currentThread().interrupt();
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	///

	// Publish messages to a topic.
	public static void publisher(String cloudRegion, char zoneId, long projectNumber, String topicId,
			long delayinms, int partitionCount, String replicas, Publisher publisher)
			throws ApiException, ExecutionException, InterruptedException {

		/// Creating the timestamp
		Instant nowTimeStamp = Instant.now();
		List<ApiFuture<String>> futures = new ArrayList<>();

		// Trying to publish the message

		try {

			// Now lets create the json message

			JsonObject message_topubsub = new JsonObject();
			message_topubsub.addProperty("Timestamp", nowTimeStamp.toString());
			message_topubsub.addProperty("Frequency", delayinms);
			message_topubsub.addProperty("Replicas", replicas);
			message_topubsub.addProperty("Partitions", partitionCount);
			message_topubsub.addProperty("Kind", "lite");

			// Convert the message to a byte string.
			ByteString data = ByteString.copyFromUtf8(message_topubsub.toString());

			PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

			// Publish a message. Messages are automatically batched.
			ApiFuture<String> future = publisher.publish(pubsubMessage);
			futures.add(future);

		} finally {
			ArrayList<MessageMetadata> metadata = new ArrayList<>();
			List<String> ackIds = ApiFutures.allAsList(futures).get();
			for (String id : ackIds) {
				// Decoded metadata contains partition and offset.
				metadata.add(MessageMetadata.decode(id));
			}

			System.out.println("TimeStamp: " + nowTimeStamp.toString() + ", Message metadata: " + metadata);

		}
	}
}