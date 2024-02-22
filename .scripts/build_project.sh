#!/bin/bash
mvn clean install -N
mkdir "$BUILD_DIR"

function cleanup() {
  # shellcheck disable=SC2115
  rm -f "*.xml"
  rm -f "*.json"
  rm -f "*.jar"
}

function assembly_resources() {
  cp -R assembly/src/main/resources/required/. "$BUILD_DIR"
  cp -R bootstrap/target/bridgenet-server.jar "$BUILD_DIR"
}

function install() {
  mvn clean install -Dmaven.test.skip --file "$1/pom.xml" || exit
  except_code
}

cleanup
# shellcheck disable=SC2054
declare -a modules_array=("assembly" "api" "rsi" "mtp" "rest" "services" "bootstrap" "connector" "test-engine")

# shellcheck disable=SC2128
for module in "${modules_array[@]}"
do
  install "$module"
done

assembly_resources
except_code