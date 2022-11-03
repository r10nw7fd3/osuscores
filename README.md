# osuscores

Discord hook that automatically posts 800pp+ scores of top 50

## Building

Download [JSON-Java](https://github.com/stleary/JSON-java) into `lib/`

Then run

```
./build.sh
```

Create `credentials.txt`

```
discord hook url
osu apiv2 id
osu api key
```

And finally start osuscores

```
java -cp osuscores.jar:lib/(json-java) snr1s.osuscores.Main # replace (json-java) with JSON-Java jar name
```
