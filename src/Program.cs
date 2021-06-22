using System;
using System.Collections.Generic;
using System.Globalization;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Google.Cloud.PubSub.V1;
using Google.Protobuf;

var delay = !string.IsNullOrEmpty(Environment.GetEnvironmentVariable("DELAY_BETWEEN_MESSAGES")) ? Int32.Parse(Environment.GetEnvironmentVariable("DELAY_BETWEEN_MESSAGES")) : 0;
var topicId = Environment.GetEnvironmentVariable("PUBSUB_TOPIC_ID");
while (true)
{
    PublishMessagesAsync(topicId);
    await Task.Delay(delay);
}

void PublishMessagesAsync(string topicId)
{
    var runId = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fff", CultureInfo.InvariantCulture);
    Console.Write(runId);
    var publisher = PublisherServiceApiClient.Create();
    var message = new PubsubMessage {Data = ByteString.CopyFromUtf8(runId)};
    var response = publisher.Publish(topicId, new[] { message });
    Console.WriteLine($" --> {response.MessageIds[0]}");
}