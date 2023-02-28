#!/bin/sh

set -ex

mkdir -p bin/classes
javac -cp json.jar -d bin/classes src/*.java
cd bin/classes
jar cfm ../../osuscores.jar ../../MANIFEST.MF *
