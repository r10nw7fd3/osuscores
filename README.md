# osuscores

Discord hook that automatically posts 800pp+ scores of top 50

## Building

Download and save [JSON-Java](https://github.com/stleary/JSON-java) as `json.jar`

Then run

```
./build.sh
```

Create `credentials.txt`

```
discord hook url
osu apiv2 id
osu apiv2 key
```

And finally start osuscores

```
java -jar osuscores.jar # json.jar should be in the same directory
```
