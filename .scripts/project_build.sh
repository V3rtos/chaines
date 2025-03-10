#!/bin/bash
mvn clean install -N

rm -rf "$BUILD_DIR"
mkdir "$BUILD_DIR"

function assembly_resources() {
  cp -R assembly/etc/. "$BUILD_DIR/etc"
  cp -R bootstrap/target/bridgenet-server.jar "$BUILD_DIR"
}

function install() {
  mvn clean install -Dmaven.test.skip --file "$1/pom.xml" || exit
  except_code
}

# shellcheck disable=SC2054
declare -a modules_queue=("assembly" "profiler" "api" "mtp" "jdbc" "rmi" "rest" "services" "bootstrap" "client" "testing")

# shellcheck disable=SC2128
for module in "${modules_queue[@]}"
do
  install "$module"
done

assembly_resources
except_code