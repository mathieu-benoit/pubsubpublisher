.NET Core app to publish messages on PubSub, leveraging [this sample code](https://cloud.google.com/pubsub/docs/publisher#c).

Run with Docker:
```
docker build -t pubsubpublisher .
docker run -it -e DELAY_BETWEEN_MESSAGES="1000" -e PUBSUB_PROJECT_ID="pubsub-test-pull" -e PUBSUB_TOPIC_ID="projects/pubsub-test-pull/topics/streaming-pull" pubsubpublisher
```

Configure dedicated service account:
```
projectId=pubsub-test-pull
namespace=pubsubpublisher
ksaName=pubsubpublisher
gsaName=$projectId-$ksaName
gsaAccountName=$gsaName@$projectId.iam.gserviceaccount.com
gcloud iam service-accounts create $gsaName
gcloud iam service-accounts add-iam-policy-binding \
    --role roles/iam.workloadIdentityUser \
    --member "serviceAccount:$projectId.svc.id.goog[$namespace/$ksaName]" \
    $gsaAccountName
gcloud projects add-iam-policy-binding $projectId --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
```

Deploy on Kubernetes:
```
kubectl create ns pubsubpublisher
kubectl apply -f k8s/serviceaccount.yaml -n pubsubpublisher
kubectl apply -f k8s/deployment.yaml -n pubsubpublisher
```

Other resources:
- [.NET Core samples with PubSub](https://github.com/GoogleCloudPlatform/dotnet-docs-samples/tree/master/pubsub/api/Pubsub.Samples)
- [PubSub load test framework](https://github.com/GoogleCloudPlatform/pubsub/tree/master/load-test-framework/)