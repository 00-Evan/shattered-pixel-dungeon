#!/bin/sh

# This shell script helps automate the process of notarizing
# It is based on the guide found here: https://www.joelotter.com/2020/08/14/macos-java-notarization.html
# requires xcode tools, script may take a minute or two to run as it uploads results to apple

# usage is: "notarize.sh <path-to-.app> <path-to-entitlements-.plist> <certificate-name> <apple-id> <app-password>"
# There is no input validation to check your arguments!
APP="$1"
PLIST=`PWD`"/$2" #need absolute path
CERT="$3"
USER="$4"
PASS="$5"

#extracts the team ID from the certification name
TEAM="${CERT#*(}"
TEAM="${TEAM%)}"

#first sign the naked dylib in /Contents/runtime/Contents/MacOS/libjli.dylib
codesign --force --options runtime --timestamp --sign "$CERT" \
      --entitlements "$PLIST" "${APP}/Contents/runtime/Contents/MacOS/libjli.dylib"

#then iterate over each jar and sign all .dylib files within it
# to do this we have to unzip each JAR, sign the files and re-zip =/
# several commands are piped to dev/null to cut down on console spam
pushd "${APP}"/Contents/app/ > /dev/null
rm -rf jar/
for JAR in *.jar; do

  mkdir jar
  mv "$JAR" jar/
  pushd jar/ > /dev/null
  unzip "${JAR}" > /dev/null
  rm "${JAR}"

  for LIB in `find . -name '*.dylib'`; do
    codesign --force --options runtime --timestamp --sign "$CERT" \
      --entitlements "$PLIST" "${LIB}"
  done

  zip -r "../${JAR}" * > /dev/null
  popd > /dev/null
  rm -rf jar/

done
popd > /dev/null

#finally do one more deep sign on the whole .app
codesign --deep --force --options runtime --timestamp --sign "$CERT" \
      --entitlements "$PLIST" "${APP}"

#zip it up and send it to apple!
rm -rf "${APP}".zip
zip -r "${APP}".zip "${APP}" > /dev/null

echo "Uploading to apple, this may take a few minutes:"

xcrun notarytool submit "${APP}".zip \
  --apple-id "$USER" \
  --password "$PASS" \
  --team-id "$TEAM" \
  --wait

rm -rf "${APP}".zip

echo "Notarizing finished, if it worked, run: xcrun stapler staple \"${APP}\""