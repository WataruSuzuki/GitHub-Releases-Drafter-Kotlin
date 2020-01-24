rm -rf build/distributions/

./gradlew build
sleep 1

unzip build/distributions/GitHub-Releases-Drafter-Kotlin.zip -d build/distributions/
mv build/distributions/GitHub-Releases-Drafter-Kotlin.zip ./
./build/distributions/GitHub-Releases-Drafter-Kotlin/bin/GitHub-Releases-Drafter-Kotlin \
    --version=0.0.1 \
    --path="WataruSuzuki/GitHub-Releases-Drafter-Kotlin" \
    --assetname="GitHub-Releases-Drafter-Kotlin.zip" \
    --token=${UPLOAD_TOKEN}
