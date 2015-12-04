# crudfaces
A JSF framework and component library which makes it easier to write best-practices compliant modern JSF applications

## About
Find out more about this project:
* [GitHub page](http://codebulb.github.io/pages/crudfaces/)
* [Live showcase](http://jbosswildfly-codebulb.rhcloud.com/CrudFacesDemo/)
* [API doc](http://codebulb.github.io/pages/crudfaces/doc/)
* [VDL doc](http://codebulb.github.io/pages/crudfaces/vdldoc/)

## How to use it
### Library dependency
Use [JitPack](https://jitpack.io/) to add its dependency to your Maven project:
```
<dependency>
    <groupId>com.github.codebulb</groupId>
    <artifactId>crudfaces</artifactId>
    <version>0.2_RC-1</version>
</dependency>
...
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
Replace the version by the tag / commit hash of your choice or `-SNAPSHOT` to get the newest SNAPSHOT.

Visit [JitPack’s docs](https://jitpack.io/docs/) for more information.

### Components
Add the CrudFaces Facelets library declaration `xmlns:cf="http://crudfaces.codebulb.ch"` in order to use its UI components in XHTML Facelets pages.