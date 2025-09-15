#!/bin/sh
set -e

## Start Testcontainers Cloud CLI. This expects the environment variable TC_CLOUD_TOKEN to be set.
if [ -z "$TC_CLOUD_TOKEN" ]; then
  echo "TC_CLOUD_TOKEN env var is not set, exiting!"
  exit 1
fi

export TCC_BINARY_NAME=/tmp/tcc-agent
export TCC_SKIP_AGENT_EXECUTION=true
curl -fsSL https://get.testcontainers.cloud/bash | sh
mkdir -p /tmp/tcc
exec ${TCC_BINARY_NAME} > "/tmp/tcc/agent.log" 2>&1 &

# Execute the original entrypoint script with all passed arguments
exec /opt/docker-entrypoint.sh "$@"
