#!/bin/bash
ENDPOINTS_MODULE_PATH=services/endpoint
ENDPOINTS_TARGET_PATH=$BUILD_DIR/services

rm -rf "$ENDPOINTS_TARGET_PATH"
mkdir "$ENDPOINTS_TARGET_PATH"

cd "services/model"
mvn clean install -Dmaven.test.skip || exit
cd ../../

function compile() {
  if [ -z "$1" ]; then
    echo "Endpoint '$1' is not found"
    exit 1
  fi

  install_endpoint "$1"
  copy_compiled_endpoint "$1"
}

function install_endpoint() {
  local target_path="$ENDPOINTS_TARGET_PATH/$1"
  local endpoint_path="$ENDPOINTS_MODULE_PATH/$1"

  mkdir "$target_path"
  cd "$endpoint_path" || exit
  mvn clean install
  except_code
}

function copy_compiled_endpoint() {
  local target_path="$ENDPOINTS_TARGET_PATH/$1"
  local endpoint_path="$ENDPOINTS_MODULE_PATH/$1"

  cd ../../../
  cp -R $endpoint_path/endpoint/. "$target_path"
  rm -rf $endpoint_path/endpoint

  except_code
}

# shellcheck disable=SC2231
for endpoint in $ENDPOINTS_MODULE_PATH/*
do
  tmp=$(echo "$endpoint" | cut -d'/' -f 3)
  if [ "$tmp" != "target" ]; then

    echo "$PREF Found endpoint $tmp"
    echo "$PREF Begin installing and compile..."

    if [ -d "$endpoint" ]; then
      compile "$tmp"
      except_code
    fi
  fi
done