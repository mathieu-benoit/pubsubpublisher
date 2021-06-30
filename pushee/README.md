Run with Docker:
```
docker build -t pushee .
docker run -it \
    -e TEXT="World" \
    pushee
```

Deploy on Kubernetes:
```
namespace=pushee
kubectl create ns $namespace
kubectl apply -f k8s/deployment.yaml -n $namespace
```