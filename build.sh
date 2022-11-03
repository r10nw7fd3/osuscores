#!/bin/sh

set -ex

mkdir -p bin/classes
javac -cp ./lib/*.jar -d bin/classes src/*.java
cd bin/classes
jar cvfm ../../osuscores.jar ../../MANIFEST.MF *
