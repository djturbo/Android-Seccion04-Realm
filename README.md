# Ver database file realm
adb pull /data/data/fjarquellada.es.sec04realmboard/files .

#!/bin/sh
ADB_PATH="/Volumes/PTAH/Usuarios/francisco/Library/android-sdk-macosx"
PACKAGE_NAME="fjarquellada.es.sec04realmboard"
DB_NAME="default.realm"
DESTINATION_PATH="/Users/xyz/Desktop/${DB_NAME}"
NOT_PRESENT="List of devices attached"
ADB_FOUND=`${ADB_PATH}/adb devices | tail -2 | head -1 | cut -f 1 | sed 's/ *$//g'`
if [[ ${ADB_FOUND} == ${NOT_PRESENT} ]]; then
    echo "Make sure a device is connected"
else
     ${ADB_PATH}/adb exec-out run-as ${PACKAGE_NAME} cat files/${DB_NAME} > ${DESTINATION_PATH}
fi