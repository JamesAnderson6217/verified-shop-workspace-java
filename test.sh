#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p out
javac -d out $(find src/main/java src/test/java -name '*.java')
java -cp out example.checkout.WorkspaceEnrollmentServiceTest
