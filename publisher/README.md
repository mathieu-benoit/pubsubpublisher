.NET Core console app to publish messages on PubSub, leveraging [this sample code](https://cloud.google.com/pubsub/docs/publisher#c).

Run with Docker:
```
docker build -t pubsubpublisher .
docker run -it \
    -e DELAY_BETWEEN_MESSAGES="1000" \
    -e PUBSUB_PROJECT_ID="pubsub-test-pull" \
    -e PUBSUB_TOPIC_ID="projects/pubsub-test-pull/topics/streaming-pull" \
    pubsubpublisher
```

Deploy on Kubernetes:
```
kubectl create ns $namespace
kubectl apply -f k8s/serviceaccount.yaml -n $namespace
kubectl apply -f k8s/deployment.yaml -n $namespace
```

Other resources:
- [Google.Cloud.PubSub.V1 doc](https://googleapis.github.io/google-cloud-dotnet/docs/Google.Cloud.PubSub.V1/)
- [.NET Core samples with PubSub](https://github.com/GoogleCloudPlatform/dotnet-docs-samples/tree/master/pubsub/api/Pubsub.Samples)
- [PubSub load test framework](https://github.com/GoogleCloudPlatform/pubsub/tree/master/load-test-framework/)