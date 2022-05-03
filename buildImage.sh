SERVICE_NAME=mftservices
VERSION=0.0.7
cat docker login docker.io
docker build -f dockerfiles/dockerfile -t chramu2003/$SERVICE_NAME:latest  .
docker build -f dockerfiles/dockerfile -t chramu2003/$SERVICE_NAME:$VERSION  .
docker push chramu2003/$SERVICE_NAME:latest
docker push chramu2003/$SERVICE_NAME:$VERSION
