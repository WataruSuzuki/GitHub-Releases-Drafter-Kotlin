unzip build/distributions/GitHub-Releases-Drafter-Kotlin.zip -d build/distributions/
mv build/distributions/GitHub-Releases-Drafter-Kotlin.zip ./
./build/distributions/GitHub-Releases-Drafter-Kotlin/bin/GitHub-Releases-Drafter-Kotlin \
    --version=$1 \
    --path="WataruSuzuki/GitHub-Releases-Drafter-Kotlin" \
    --publish \
    --token=${PUBLISH_TOKEN}
