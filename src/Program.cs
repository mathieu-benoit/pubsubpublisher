using System;
using System.Collections.Generic;
using System.Globalization;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Google.Cloud.PubSub.V1;
using Google.Protobuf;
using System.Text.Json;
using System.Text.Json.Serialization;

var delay = !string.IsNullOrEmpty(Environment.GetEnvironmentVariable("DELAY_BETWEEN_MESSAGES")) ? Int32.Parse(Environment.GetEnvironmentVariable("DELAY_BETWEEN_MESSAGES")) : 0;
var topicId = Environment.GetEnvironmentVariable("PUBSUB_TOPIC_ID");
var numberOfPullerReplicas = !string.IsNullOrEmpty(Environment.GetEnvironmentVariable("NUMBER_OF_PULLER_REPLICAS")) ? Int32.Parse(Environment.GetEnvironmentVariable("NUMBER_OF_PULLER_REPLICAS")) : 0;
var kind = Environment.GetEnvironmentVariable("PUBSUB_TOPIC_KIND");
while (true)
{
    PublishMessagesAsync(topicId);
    await Task.Delay(delay);
}

void PublishMessagesAsync(string topicId)
{
    var publisher = PublisherServiceApiClient.Create();
    var startTime = DateTime.UtcNow;
    var runId = startTime.ToString("yyyy-MM-ddTHH:mm:ss.fff", CultureInfo.InvariantCulture);
    Console.Write(runId);
    var messagePayload = new MessagePayload { Timestamp = runId, Frequency = delay, Replicas = numberOfPullerReplicas, Kind = kind };
    var message = new PubsubMessage { Data = ByteString.CopyFromUtf8(JsonSerializer.Serialize(messagePayload)) };
    var response = publisher.Publish(topicId, new[] { message });
    var endTime = DateTime.UtcNow;
    Console.Write($" - {endTime.Subtract(startTime).Milliseconds}");
    Console.WriteLine($" --> {response.MessageIds[0]}");
}

class MessagePayload
{
    public string Timestamp { get; set; }
    public int Frequency { get; set; }
    public int Replicas { get; set; }
    public string Kind {get; set; }
}