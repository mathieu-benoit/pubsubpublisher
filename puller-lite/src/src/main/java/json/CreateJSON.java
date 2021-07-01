package json;

import java.time.Duration;
import java.time.Instant;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.pubsub.v1.PubsubMessage;

public class CreateJSON {

	public static JsonObject createjson(PubsubMessage message, String subscriptionId) {

		JsonObject data_json = new JsonObject();
		JsonObject pubsub_message = new JsonObject();
		Instant local_time = Instant.now();
		Gson gson = new Gson();

		try {
			pubsub_message = gson.fromJson(message.getData().toStringUtf8(), JsonObject.class);
		} catch (Exception e) {

		}

		// We get the payload ts and transform it to Instant

		Instant payload_ts = Convert_ts(pubsub_message.get("Timestamp").getAsString());

		// PULL latency

		Instant message_time = Instant.ofEpochSecond(message.getPublishTime().getSeconds(),
				message.getPublishTime().getNanos());
		long pull_latencydiffms = Duration.between(message_time, local_time).toMillis();

		// Push Latency

		long push_latencydiffms = Duration.between(payload_ts, message_time).toMillis();

		// Full Latency

		long full_latencydiffms = Duration.between(payload_ts, local_time).toMillis();

		data_json.addProperty("Global Log type", "latency");
		data_json.addProperty("Global App Status", "Running");
		data_json.addProperty("Global Direction", "Pub/Sub to Container");

		data_json.addProperty("Pub/Sub Subscription id", subscriptionId);
		data_json.addProperty("Pub/Sub Message id", message.getMessageId());
		data_json.addProperty("Pub/Sub Message publishtime", message_time.toString());
		data_json.addProperty("Pub/Sub Message kind", pubsub_message.get("Kind").getAsString());

		data_json.addProperty("Pub/Sub Message Payload", message.getData().toStringUtf8());
		data_json.addProperty("Pub/Sub Message Payload timestamp", payload_ts.toString());

		data_json.addProperty("GKE Container Pull timestamp", local_time.toString());
		data_json.addProperty("GKE Container Pull Data frequency", pubsub_message.get("Frequency").getAsString());
		data_json.addProperty("GKE Container replicas", pubsub_message.get("Replicas").getAsString());

		// MATH operations
		data_json.addProperty("GKE Container Pull latency [ms]", pull_latencydiffms);
		data_json.addProperty("Global latency [ms]", full_latencydiffms);
		data_json.addProperty("Pub/Sub Push latency [ms]", push_latencydiffms);

		return data_json;
	}

	public static Instant Convert_ts(String time_st) {

		try {
			return Instant.parse(time_st);
		} catch (Exception e) {
			return Instant.now();
		}

	}
}
