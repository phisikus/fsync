# fsync

Fsync is a basic rsync-inspired tool that can be used to synchronize local directory with remote copy.
You can download contents of remote directory using "pull" command and after modifications you can use "push" command to apply them remotely. In both cases client connects to server and structures of both remote and local directories are analyzed. Contents are represented in a tree structure that contains hashes of specific files. Differences between remote and local trees are calculated and formed into "patch" that is used to update files. 

Patch is basically a list of operations that sould be applied in order to reduce differences between different versions of files. Currently supported operations are basically: create file, delete file, replace content. It would be possible to implement smarter diff-like approach in handling file contents. Currently fsync is not a very practical tool, but with its FP implementation could be a base for other projects.

## Usage

You can build fsync package using SBT:

	sbt clean && sbt package

Output JAR file should appear in ./target/scala-2.11/ directory.
Running fsync without parameteres will provide further instructions.

	scala fsync_2.11-0.1.jar 	

You can setup server in current directory:

	scala fsync_2.11-0.1.jar server

You can pull files using:

	scala fsync_2.11-0.1.jar client pull 127.0.0.1 directory1

Providing that "directory1" exists both in current working directory of a server (127.0.0.1) and client, local contents on client side will be changed to match server-side version. "Push" operation would end with oposite result.

## License

This software is distributed under GPL v3 License (check LICENSE file).
