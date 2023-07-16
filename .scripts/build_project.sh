#!/bin/bash

function install() {
  cd "$1"
  except_code
  mvn clean install -Dmaven.test.skip || exit
  except_code
  cd ../
}

install api
install inject
install rsi
install protocol
install rest
install bootstrap