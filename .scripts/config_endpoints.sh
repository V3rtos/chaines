#!/bin/bash
ENDPOINT_CONFIG_NAME=endpoint_config.json
ENDPOINT_ETC_PATH=$SCRIPTS_DIR/etc
ENDPOINTS_TARGET_PATH=$BUILD_DIR/services
ENDPOINT_TARGET=$1

echo "ENDPOINT_TARGET = $ENDPOINT_TARGET"

function apply() {
  copy_endpoint_confs "$1"
  edit_endpoint_name "$1"
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

function configure_all() {
    # shellcheck disable=SC2231
    for endpoint in $ENDPOINTS_TARGET_PATH/*
    do
      configure $endpoint
    done
}

function configure() {
  tmp=$(echo "$1" | cut -d'/' -f 3)
  echo "$PREF Configure endpoint '$tmp' from $1"

  if [ -d "$1" ]; then

    apply "$tmp"
    except_code
  fi
}

if [[ -z $ENDPOINT_TARGET ]]; then
    configure_all
else
    configure "$ENDPOINTS_MODULE_PATH/$ENDPOINT_TARGET"
fi