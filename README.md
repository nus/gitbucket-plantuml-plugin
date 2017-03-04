gitbucket-plantuml-plugin
========
A GitBucket plugin renders PlantUML sources.

Building
----
Run the below command. Note that use `sbt.bat` when running on Windows.

```
./sbt.sh assembly
```

You can get the plugin at `target/scala-2.11/gitbucket-plantuml-plugin-assembly-1.0.0.jar` and copy the jar file to `~/.gitbucket/plugins` directory.

License
----

   Copyright 2017 Yota Ichino

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

Relating sofware license
----

This plugin contains PlantUML library which exists at `lib` directory.
The library licensed under the Apache License, Version 2.0.
