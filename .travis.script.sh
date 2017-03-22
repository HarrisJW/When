if [ "$TRAVIS_SECURE_ENV_VARS" == "true" ]
then
	mvn -B -e -V -Dvaadin.testbench.developer.license=$TESTBENCH_LICENSE verify
fi