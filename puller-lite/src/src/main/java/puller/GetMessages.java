package puller;
import json.CreateJSON;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsublite.CloudRegion;
import com.google.cloud.pubsublite.CloudZone;
import com.google.cloud.pubsublite.ProjectNumber;
import com.google.cloud.pubsublite.SubscriptionName;
import com.google.cloud.pubsublite.SubscriptionPath;
import com.google.cloud.pubsublite.cloudpubsub.FlowControlSettings;
import com.google.cloud.pubsublite.cloudpubsub.Subscriber;
import com.google.cloud.pubsublite.cloudpubsub.SubscriberSettings;
import com.google.pubsub.v1.PubsubMessage;


public class GetMessages {

	public static void main(String[] args) {

		String cloudRegion;
		char zoneId;
		long projectNumber;
		String subscriptionId;

		try {
			cloudRegion = System.getenv("PUBSUB_CLOUD_REGION");
			zoneId = System.getenv("PUBSUB_CLOUD_ZONE_ID").charAt(0);
			projectNumber = Long.parseLong(System.getenv("PUBSUB_PROJECT_NUMBER"));
			subscriptionId = System.getenv("PUBSUB_SUBSCRIPTION_ID");
			
			subscriber(cloudRegion, zoneId, projectNumber, subscriptionId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void subscriber(String cloudRegion, char zoneId, long projectNumber, String subscriptionId)
			throws ApiException {

		SubscriptionPath subscriptionPath = SubscriptionPath.newBuilder()
				.setLocation(CloudZone.of(CloudRegion.of(cloudRegion), zoneId))
				.setProject(ProjectNumber.of(projectNumber)).setName(SubscriptionName.of(subscriptionId)).build();

		// The message stream is paused based on the maximum size or number of messages
		// that the
		// subscriber has already received, whichever condition is met first.
		FlowControlSettings flowControlSettings = FlowControlSettings.builder()
				// 10 MiB. Must be greater than the allowed size of the largest message (1 MiB).
				.setBytesOutstanding(10 * 1024 * 1024L)
				// 1,000 outstanding messages. Must be >0.
				.setMessagesOutstanding(1000L).build();

		MessageReceiver receiver = (PubsubMessage message, AckReplyConsumer consumer) -> {
			System.out.println(CreateJSON.createjson(message, subscriptionId));
			consumer.ack();
		};

		SubscriberSettings subscriberSettings = SubscriberSettings.newBuilder().setSubscriptionPath(subscriptionPath)
				.setReceiver(receiver)
				// Flow control settings are set at the partition level.
				.setPerPartitionFlowControlSettings(flowControlSettings).build();

		Subscriber subscriber = Subscriber.create(subscriberSettings);

		// Start the subscriber. Upon successful starting, its state will become
		// RUNNING.
		subscriber.startAsync().awaitRunning();

		System.out.println("Listening to messages on " + subscriptionPath.toString() + "...");

		try {
			System.out.println(subscriber.state());
			subscriber.awaitTerminated();
		} catch (IllegalStateException t) {
			// Shut down the subscriber. This will change the state of the subscriber to
			// TERMINATED.
			subscriber.stopAsync().awaitTerminated();
			System.out.println("Subscriber is shut down: " + subscriber.state());
		}
	}

}
