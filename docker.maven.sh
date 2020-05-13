#!/bin/sh
# https://stackoverflow.com/questions/3545292/how-to-get-maven-project-version-to-the-bash-command-line
if [ $# -lt 3 ]; then
  echo "Usage: $0 <tag> <phases...>, e.g. $0 3-jdk-11-openj9 clean install"
  echo "See https://hub.docker.com/_/maven for available tags"
  exit 1
fi
groupId=$(mvn help:evaluate -Dexpression=project.groupId | grep -v '\[')
artifactId=$(mvn help:evaluate -Dexpression=project.artifactId | grep -v '\[')
version=$(mvn help:evaluate -Dexpression=project.version | grep -v '\[')
tag="$1"
shift
docker run -it --rm \
  --name "$artifactId" \
  -v "$HOME/.m2":/root/.m2 \
  -v "$(pwd)":/usr/src/"$groupId"/"$artifactId"/"$version" \
  -w /usr/src/"$groupId"/"$artifactId"/"$version" \
  maven:"$tag" mvn "$@"
