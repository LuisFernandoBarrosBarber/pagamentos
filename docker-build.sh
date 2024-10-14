#!/bin/bash

VERSION=2.9.5

docker build -t "luisfernandobarros/barbearia-pagamentos:$VERSION" .

docker push "luisfernandobarros/barbearia-pagamentos:$VERSION"
