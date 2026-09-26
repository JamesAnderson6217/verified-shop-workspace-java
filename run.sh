#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p out
javac -d out $(find src/main/java -name '*.java')
java -cp out example.checkout.CheckoutWorkspaceExample
