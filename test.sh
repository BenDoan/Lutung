#!/bin/bash
cp Test.java _Test.java
ant compile jar && java -jar build/jar/lutung.jar Test.java && cat Test.java
mv _Test.java Test.java
