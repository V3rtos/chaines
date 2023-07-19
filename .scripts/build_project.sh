#!/bin/bash
application_jarfile="bridgenet-server.jar"
bootstrap_file="bootstrap.xml";
rsiconfig_file="rsiconfig.xml";
injectconfig_file="injectconfig.xml";

mkdir "$BUILD_DIR"

function cleanup() {
  # shellcheck disable=SC2115
  rm -f "$BUILD_DIR/$application_jarfile"
  # shellcheck disable=SC2115
    rm -f "$BUILD_DIR/$bootstrap_file"
  # shellcheck disable=SC2115
  rm -f "$BUILD_DIR/$rsiconfig_file"
  # shellcheck disable=SC2115
  rm -f "$BUILD_DIR/$injectconfig_file"
}

function application() {
  cp -R bootstrap/target/$application_jarfile "$BUILD_DIR"
  cp -R bootstrap/src/main/resources/$bootstrap_file "$BUILD_DIR"
  cp -R rsi/src/main/resources/$rsiconfig_file "$BUILD_DIR"
  cp -R inject/src/main/resources/$injectconfig_file "$BUILD_DIR"
}

function install() {
  cd "$1"
  except_code
  mvn clean install -Dmaven.test.skip || exit
  except_code
  cd ../
}

cleanup
# shellcheck disable=SC2054
declare -a modules_array=("api" "inject" "rsi" "mtp" "rest" "bootstrap")

# shellcheck disable=SC2128
for module in "${modules_array[@]}"
do
  install "$module"
done

application
except_code