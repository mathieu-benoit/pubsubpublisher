.NET Core app to publish messages on PubSub, leveraging [this sample code](https://cloud.google.com/pubsub/docs/publisher#c).

Run with Docker:
```
docker build -t pubsubpublisher .
docker run -it \
    -e DELAY_BETWEEN_MESSAGES="1000" \
    -e PUBSUB_PROJECT_ID="pubsub-test-pull" \
    -e PUBSUB_TOPIC_ID="projects/pubsub-test-pull/topics/streaming-pull" \
    pubsubpublisher
```

```
projectId=pubsub-test-pull
gcloud config set project $projectId

gcloud pubsub topics create streaming-pull-1ms-1r
gcloud pubsub topics create streaming-pull-1ms-2r
gcloud pubsub topics create streaming-pull-1s-1r
gcloud pubsub topics create streaming-pull-1s-2r
gcloud pubsub topics create streaming-pull-1m-1r
gcloud pubsub topics create streaming-pull-1m-2r
gcloud pubsub topics create streaming-pull-1h-1r
gcloud pubsub topics create streaming-pull-1h-2r
```

Configure dedicated service account:
```
namespace=pubsubpublisher
ksaName=pubsubpublisher
gsaName=pubsubpublisher
gsaAccountName=$gsaName@$projectId.iam.gserviceaccount.com
gcloud iam service-accounts create $gsaName
gcloud iam service-accounts add-iam-policy-binding \
    --role roles/iam.workloadIdentityUser \
    --member "serviceAccount:$projectId.svc.id.goog[$namespace/$ksaName]" \
    $gsaAccountName
gcloud pubsub topics add-iam-policy-binding streaming-pull-1ms-1r --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
gcloud pubsub topics add-iam-policy-binding streaming-pull-1ms-2r --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
gcloud pubsub topics add-iam-policy-binding streaming-pull-1s-1r --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
gcloud pubsub topics add-iam-policy-binding streaming-pull-1s-2r --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
gcloud pubsub topics add-iam-policy-binding streaming-pull-1m-1r --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
gcloud pubsub topics add-iam-policy-binding streaming-pull-1m-2r --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
gcloud pubsub topics add-iam-policy-binding streaming-pull-1h-1r --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
gcloud pubsub topics add-iam-policy-binding streaming-pull-1h-2r --member "serviceAccount:$gsaAccountName" --role "roles/pubsub.publisher"
```

Deploy on Kubernetes:
```
kubectl create ns pubsubpublisher
kubectl apply -f k8s/serviceaccount.yaml -n pubsubpublisher
kubectl apply -f k8s/deployment.yaml -n pubsubpublisher
```

Other resources:
- [Google.Cloud.PubSub.V1 doc](https://googleapis.github.io/google-cloud-dotnet/docs/Google.Cloud.PubSub.V1/)
- [.NET Core samples with PubSub](https://github.com/GoogleCloudPlatform/dotnet-docs-samples/tree/master/pubsub/api/Pubsub.Samples)
- [PubSub load test framework](https://github.com/GoogleCloudPlatform/pubsub/tree/master/load-test-framework/)