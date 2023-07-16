#!/bin/bash

ENDPOINT_CONFIG_NAME=endpoint_config.json
ENDPOINT_ETC_PATH=$SCRIPTS_DIR/etc
ENDPOINTS_TARGET_PATH=$BUILD_DIR/services

function apply() {
  copy_endpoint_confs $1
  edit_endpoint_name $1
}

function copy_endpoint_confs() {
  local target_path="$ENDPOINTS_TARGET_PATH/$1"

  cp -R $ENDPOINT_ETC_PATH/.gen/. "$target_path"
  except_code

  cp -R $ENDPOINT_ETC_PATH/$1/. "$target_path"
}

function edit_endpoint_name() {
  local conf_path="$ENDPOINTS_TARGET_PATH/$1/$ENDPOINT_CONFIG_NAME"
  sed -i'.BAK' "s/%endpoint%/$1/" "$conf_path"
  except_code
  rm "$conf_path.BAK"
}

# shellcheck disable=SC2231
for endpoint in $ENDPOINTS_TARGET_PATH/*
do
  tmp=$(echo "$endpoint" | cut -d'/' -f 3)
  echo "$PREF Configure endpoint $tmp"

  if [ -d "$endpoint" ]; then

    apply "$tmp"
    except_code
  fi
done
