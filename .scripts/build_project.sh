#!/bin/bash
application_jarfile="bridgenet-server.jar"
bootstrap_file="bootstrap.xml";
rsiconfig_file="rsiconfig.xml";
injectconfig_file="injectconfig.xml";

mvn clean install -N
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
  local -a files_to_copy=(
    bootstrap/target/$application_jarfile
    bootstrap/src/main/resources/$bootstrap_file
    rsi/src/main/resources/$rsiconfig_file
    api/src/main/resources/$injectconfig_file
  )

  for file in "${files_to_copy[@]}"
  do
    cp -R "$file" "$BUILD_DIR"
  done
}

function install() {
  mvn clean install --file "$1/pom.xml" || exit
  except_code
}

cleanup
# shellcheck disable=SC2054
declare -a modules_array=("api" "rsi" "mtp" "rest" "services" "bootstrap" "test-engine")

# shellcheck disable=SC2128
for module in "${modules_array[@]}"
do
  install "$module"
done

application
except_code