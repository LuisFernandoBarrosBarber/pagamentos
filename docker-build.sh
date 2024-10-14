#!/bin/bash

VERSION=2.9.4

docker build -t "luisfernandobarros/barbearia-pagamentos:$VERSION" .

docker push "luisfernandobarros/barbearia-pagamentos:$VERSION"
