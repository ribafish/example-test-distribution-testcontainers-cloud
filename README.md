# An example on how to get Develocity Test Distribution to work with Testcontainers Cloud

There are three variants on how to run the test ditribution agent:
1. Jar
2. Docker
3. Kubernetes

### Testing with this example
You can use the project defined in `example-testcontaiers-cloud` to run tests with Test Distribution + Testcontainers. It requires you to define one or both of `-PmaxRemoteExecutors=<number>` or `-PmaxLocalExecutors=<number>` when running the `test` (or any other task that invokes it) task. When running with `PmaxRemoteExecutors`, you will need a Test Distribution agent with Testcontainers Cloud configured. Examples on how to get it running are below.

## 1. Test distribution Jar + Testcontainers Cloud

This is the simplest and recommended way to get started. You just need to download the [TD agent jar](https://docs.gradle.com/develocity/test-distribution-agent/#jar) and run it with the appropriate parameters and run the [Testcontainers Cloud CI agent](https://testcontainers.com/cloud/docs/#tcc-for-ci).

Generally, this consists of two steps, done on the machine where you want to run the TD and TCC agents.:

1. Start the TCC agent:

```bash
export TC_CLOUD_TOKEN=<<testcontainers-cloud-token>>
sh -c "$(curl -fsSL https://get.testcontainers.cloud/bash) &
```

2. Start the TD agent:

```bash
java -Xms64m -Xmx64m -XX:MaxMetaspaceSize=64m \
    -jar develocity-test-distribution-agent-<<version>>.jar \
    --server https://<<develocity-hostname>> \
    --registration-key <<registration-key>> \
    --pool <<pool-id>>
```

## 2. Test distribution Docker + Testcontainers Cloud

You can also run the TD agent as a Docker container. You can find the image on [Docker Hub](https://hub.docker.com/r/gradle/develocity-test-distribution-agent). However, you will need to extend the image to run the TCC agent as well. AN example `Dockerfile` and `docker-compose.yml` is provided in the `docker-td-tcc` folder. 

To build and run the example container in the `docker-td-tcc` folder using Docker Compose, run the following commands:

```bash
export TC_CLOUD_TOKEN=<<testcontainers-cloud-token>>
docker compose run -d td-tc \
    --server https://<<develocity-hostname>> \
    --registration-key <<registration-key>> \
    --pool <<pool-id>>
```

Alternatively, using plain Docker commands:

```bash
export TC_CLOUD_TOKEN=<<testcontainers-cloud-token>>
docker build -t td-tc .
docker run -d -e TC_CLOUD_TOKEN=$TC_CLOUD_TOKEN td-tc \
    --server https://<<develocity-hostname>> \
    --registration-key <<registration-key>> \
    --pool <<pool-id>>
```

Note: A prebuilt example image, based on the `Dockerfile` in the `docker-td-tcc` folder, is available on [Docker Hub](https://hub.docker.com/r/gkojekgradle/develocity-td-tcc-agent).

## 3. Test distribution Kubernetes + Testcontainers Cloud

To run TD+TCC in Kubernetes, you will need to create a custom image that contains both agents. An example `Dockerfile` is provided in the `docker-td-tcc` folder. The image needs to be built and pushed to a container registry that your Kubernetes cluster can access. You can use the template from [TD docs](https://docs.gradle.com/develocity/test-distribution-agent/#kubernetes), with the only change that you will also need to supply the `TC_CLOUD_TOKEN` as a Kubernetes secret. An example `td-tcc-deployment.yaml` is provided in the `k8s-td-tcc` folder.

To deploy the example in the `k8s-td-tcc` folder, modify the depoyment file with your data and run the following commands:

```bash
kubectl apply -f k8s-td-tcc/td-tcc-deployment.yaml
```

Note: A prebuilt example image, based on the `Dockerfile` in the `docker-td-tcc` folder, is available on [Docker Hub](https://hub.docker.com/r/gkojekgradle/develocity-td-tcc-agent).
