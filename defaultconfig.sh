if [[ -d ./Shrines-Default-Config ]]; then
  rm -r ./Shrines-Default-Config # make sure no files persisted over multiple runs
fi
mkdir ./Shrines-Default-Config
mkdir ./Shrines-Default-Config/data
./gradlew --info --stacktrace runWorldGenData
./gradlew --info --stacktrace runGenerateData
cp -r src/generated/reports/reports/shrines ./Shrines-Default-Config/data/shrines
mkdir ./Shrines-Default-Config/data/shrines/tags
cp -r src/generated/resources/data/shrines/tags/worldgen ./Shrines-Default-Config/data/shrines/tags/worldgen
mkdir ./Shrines-Default-Config/data/shrines/tags/shrines
cp -r src/generated/resources/data/shrines/tags/shrines/random_variation ./Shrines-Default-Config/data/shrines/tags/shrines/random_variation
cp -r src/main/resources/data/shrines/loot_tables ./Shrines-Default-Config/data/shrines/loot_tables
cp -r src/main/resources/data/shrines/structures ./Shrines-Default-Config/data/shrines/structures
touch ./Shrines-Default-Config/pack.mcmeta
echo "{
            \"pack\": {
              \"description\": \"Default Worldgen Configuration for Shrines Structures\",
              \"pack_format\": 10,
              \"_comment\": \"Modify the contained files to configure Shrines Structures Structures'\"
            }
          }" >./Shrines-Default-Config/pack.mcmeta
cd Shrines-Default-Config || exit
if [[ -z "$shrines_mod_version" ]];
then
  shrines_mod_version="$(date +"%Y:%m:%d-%H:%M:%S")"
fi
if test -z "$MC_VERSION"
then
  MC_VERSION="UnkownMc"
fi
zip -r ../Shrines-Default-Config-"${MC_VERSION}"-"${shrines_mod_version}".zip data pack.mcmeta
