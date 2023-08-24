if [[ -d ./Shrines-Structures ]]; then
  rm -r ./Shrines-Structures # make sure no files persisted over multiple runs
fi
mkdir ./Shrines-Structures
cp -r src/main/resources/data ./Shrines-Structures/
touch ./Shrines-Structures/pack.mcmeta
echo "{
            \"pack\": {
              \"description\": \"Shrines Structures\",
              \"pack_format\": 15
            }
          }" >./Shrines-Structures/pack.mcmeta
cd Shrines-Structures || exit
if [[ -z "$shrines_mod_version" ]];
then
  shrines_mod_version="$(date +"%Y:%m:%d-%H:%M:%S")"
fi
if test -z "$MC_VERSION"
then
  MC_VERSION="UnkownMc"
fi
zip -r ../Shrines-Structures-"${MC_VERSION}"-"${shrines_mod_version}".zip data pack.mcmeta
