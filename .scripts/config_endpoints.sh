#!/bin/bash

ENDPOINT_CONFIG_NAME=endpoint.conf
ENDPOINT_ETC_PATH=$SCRIPTS_DIR/etc

function apply() {
  copy_endpoint_conf $1
  edit_endpoint_name $1
}

function copy_endpoint_conf() {
  local target_path="$BUILD_DIR/$1"
  cp -R $ENDPOINT_ETC_PATH/. "$target_path"
  except_code
}

function edit_endpoint_name() {
  local conf_path="$BUILD_DIR/$1/$ENDPOINT_CONFIG_NAME"
  sed -i'.BAK' "s/endpoint_filepath=%endpoint%.jar/endpoint_filepath=$1.jar/" "$conf_path"
  except_code
  rm "$conf_path.BAK"
}

# shellcheck disable=SC2231
for endpoint in $BUILD_DIR/*
do
  tmp=$(echo "$endpoint" | cut -d'/' -f 2)
  echo "$PREF Configure endpoint $tmp"

  if [ -d "$endpoint" ]; then

    apply "$tmp"
    except_code
  fi
done
