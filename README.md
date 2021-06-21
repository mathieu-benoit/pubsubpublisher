.NET Core app to publish messages on PubSub, leveraging [this sample code](https://cloud.google.com/pubsub/docs/publisher#c).

```
docker build -t pubsubpublisher .
docker run -it -e DELAY_BETWEEN_MESSAGES="1000" -e NUMBER_OF_MESSAGES="5" pubsubpublisher
```

Other resources:
- [.NET Core samples with PubSub](https://github.com/GoogleCloudPlatform/dotnet-docs-samples/tree/master/pubsub/api/Pubsub.Samples)
- [PubSub load test framework](https://github.com/GoogleCloudPlatform/pubsub/tree/master/load-test-framework/)