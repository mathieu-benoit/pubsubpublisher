.NET Core app to publish messages on PubSub, leveraging [this sample code](https://cloud.google.com/pubsub/docs/publisher#c).

```
docker build -t pubsubpublisher .
docker run -it -e DELAY_BETWEEN_MESSAGES="1000" -e PUBSUB_PROJECT_ID="your-pubsub-project-id" -e PUBSUB_TOPIC_ID="your-pubsub-topic-id" pubsubpublisher
```

```
kubectl apply -f k8s/deployment.yaml
```

Other resources:
- [.NET Core samples with PubSub](https://github.com/GoogleCloudPlatform/dotnet-docs-samples/tree/master/pubsub/api/Pubsub.Samples)
- [PubSub load test framework](https://github.com/GoogleCloudPlatform/pubsub/tree/master/load-test-framework/)