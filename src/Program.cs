using System;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Google.Cloud.PubSub.V1;

var counter = 0;
var max = !string.IsNullOrEmpty(Environment.GetEnvironmentVariable("NUMBER_OF_MESSAGES")) ? Int32.Parse(Environment.GetEnvironmentVariable("NUMBER_OF_MESSAGES")) : 0;
var delay = !string.IsNullOrEmpty(Environment.GetEnvironmentVariable("DELAY_BETWEEN_MESSAGES")) ? Int32.Parse(Environment.GetEnvironmentVariable("DELAY_BETWEEN_MESSAGES")) : 0;
var projectId = Environment.GetEnvironmentVariable("PUBSUB_PROJECT_ID");
var topicId = Environment.GetEnvironmentVariable("PUBSUB_TOPIC_ID");
while (counter < max)
{
    Console.WriteLine("Hello, World!");
    PublishMessagesAsync(projectId, topicId);
    counter++;
    await Task.Delay(delay);
}

void PublishMessagesAsync(string projectId, string topicId)
{
    //var topicName = TopicName.FromProjectTopic(projectId, topicId);
    //var publisher = await PublisherClient.CreateAsync(topicName);
    var timeStamp = DateTime.Now;
    try
    {
        //var message = await publisher.PublishAsync(timeStamp);
        //Console.WriteLine($"Published message {message}");
        Console.WriteLine(timeStamp);
    }
    catch (Exception exception)
    {
        Console.WriteLine($"An error ocurred when publishing message {timeStamp}: {exception.Message}");
    }
}