#!/bin/bash

VERSION=2.8.3

docker build -t "luisfernandobarros/barbearia-pagamentos:$VERSION" .

docker push "luisfernandobarros/barbearia-pagamentos:$VERSION"
