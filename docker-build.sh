#!/bin/bash

VERSION=3.2.0

docker build -t "luisfernandobarros/barbearia-pagamentos:$VERSION" .

docker push "luisfernandobarros/barbearia-pagamentos:$VERSION"
