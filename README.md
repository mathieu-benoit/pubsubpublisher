Repo to centralize code for apps and infra to reproduce benchmark tests with PubSub to measure latency. The tests consists on 2 main roles in PubSub:
1. Publisher, to send messages to a specific topic per scenario: every 1ms, 1s, 1m or 1h.
2. Puller, to pull message from a specific topic's subscription per scenario.

FIXME:
- 1 README per app to build and deploy it
- 1 README for the infra with the `gcloud` commands to provision GKE, PubSub topics/subscriptions, custom Logs-based metrics, etc.

```
projectId=pubsub-test-pull
gcloud config set project $projectId

gcloud pubsub topics create streaming-pull-1ms-1r
gcloud pubsub topics create streaming-pull-1ms-2r
gcloud pubsub topics create streaming-pull-1ms-5r
gcloud pubsub topics create streaming-pull-1s-1r
gcloud pubsub topics create streaming-pull-1s-2r
gcloud pubsub topics create streaming-pull-1s-5r
gcloud pubsub topics create streaming-pull-1m-1r
gcloud pubsub topics create streaming-pull-1m-2r
gcloud pubsub topics create streaming-pull-1m-5r
gcloud pubsub topics create streaming-pull-1h-1r
gcloud pubsub topics create streaming-pull-1h-2r
gcloud pubsub topics create streaming-pull-1h-5r
```

Configure dedicated service account:
```
namespace=pubsubpublisher
ksaName=publisher
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

Resources:
- [Choosing Pub Sub or Pub Sub Lite?](https://youtu.be/fgVE1OoJ2XI)