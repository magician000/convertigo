# What is Convertigo Low Code Platform ?

Convertigo is an open source fullstack Low Code & No Code platform.. The platform is used to build Enterprise Web & Mobile apps in a few days. Convertigo platform is composed of several components:

1.	**Convertigo Server**: The back-end server part. Handles back-end connectors, micro-services execution, offline data device synchronization and serves Web & Mobile Web apps. Runs as a Docker container with the `convertigo` image
2.	**Convertigo Studio**: Runs on a Windows or a MacOS workstation, Eclipse based IDE, used to program Back-end micro-services workflows and use the "Mobile Builder" edition to build Mobile & Web apps UIs in a MXDP (Multi eXperience Development Platform) Low code mode. Can be directly downloaded from [Sourceforge.net](https://sourceforge.net/projects/convertigo/files/latest/download)
3.	**Convertigo SDKs**: Can be used with third party Client Web & Mobile development tools such as Xcode (iOS) Android Studio (Android) Angualr, Vue.js or plain js . SDKS are available on each platform standard repository (Bintray for Android, Cocoapods for iOS and Nuget for .NET)
4.	**Convertigo Forms**: The No Code App Builder to build form based apps as PWAs or Web applications with a Web Based NoCode studio intented for non technical developpers (Citizen Developpers)

Convertigo Community edition brought to you by Convertigo SA (Paris & San Francisco). The platform is currently used by more than 100K developers worldwide, building enterprise class mobile apps.

> [www.convertigo.com](https://www.convertigo.com)

# How to use this image

## Quick start

	$ docker run --name C8O -d -p 28080:28080 convertigo

This will start a container running the minimum Convertigo server. Convertigo uses images' **/workspace** directory to store configuration file and deployed projects as an Docker volume.

You can access the Server admin console on http://[dockerhost]:28080/convertigo and login using the default credentials: admin / admin

## Link Convertigo to a CouchDB database for FullSync (Convertigo EE only)

Convertigo FullSync module uses Apache CouchDB 2.3.1 as NoSQL repository. You can use the **[couchdb](https://hub.docker.com/_/couchdb/)** docker image and link to it convertigo this way

Launch CouchDB container and name it 'fullsync'

    docker run -d --name fullsync couchdb:2.3.1

Then launch Convertigo and link it to the running 'fullsync' container. Convertigo Low Code sever will automatically use it as its fullsync repository.

    docker run -d --name C8O --link fullsync:couchdb -p 28080:28080 convertigo

## Use embedded PouchDB as FullSync engine (not for production)

Convertigo FullSync is designed to use CouchDB server or cluster. Convertigo FullSync is also compatible with PouchDB but only for little projects or tests. Internet access is required to enable this feature.

It can be enabled directly at startup:

   docker run -d --name C8O -e JAVA_OPTS="-Dconvertigo.engine.fullsync.pouchdb=true" -p 28080:28080 convertigo

## Link Convertigo Low Code Server to a Billing & Analytics database

### MySQL

MySQL is the recommended database for holding Convertigo Low Code server analytics. You can use this command to run convertigo and link it to a running MySQL container. Change `[mysql-container]` to the container name, and `[username for the c8oAnalytics db]`, `[password for specified db user]` with the values for your MySQL configuration.

    docker run -d --name C8O --link [mysql-container]:mysql -p 28080:28080                               \
        -e JAVA_OPTS="-Dconvertigo.engine.billing.enabled=true                                           \ 
                -Dconvertigo.engine.billing.persistence.jdbc.username=[username for the c8oAnalytics db] \
                -Dconvertigo.engine.billing.persistence.jdbc.password=[password for specified db user]   \
                -Dconvertigo.engine.billing.persistence.jdbc.url=jdbc:mysql://mysql:3306/c8oAnalytics"   \
    convertigo

## Where is Convertigo Low Code server storing deployed projects

Projects are deployed in the Convertigo workspace, a simple file system directory. You can map the docker container **/workspace** to your physical system by using:

    docker run --name C8O -v $(pwd):/workspace -d -p 28080:28080 convertigo

You can share the same workspace by all Convertigo containers. In this case, when you deploy a project on a Convertigo container, it will be seen by others. This is the best way to build multi-instance load balanced Convertigo server farms.

**Be sure to have a really fast file sharing between instances !!! We have experienced that Azure File Share is not fast enough**

To avoid log and cache mixing, you have to add 2 variables for instance specific paths:

    -Dconvertigo.engine.cache_manager.filecache.directory=/workspace/cache/[instance name]
    -Dconvertigo.engine.log4j.appender.CemsAppender.File=/workspace/logs/[instance name]/engine.log

## Make image with pre-deployed projects

If you want to make a vertical image ready to start with your application inside, you have to have your built projects **.car** files next to your `Dockerfile`:

    FROM convertigo
    COPY myProject.car /usr/local/tomcat/webapps/convertigo/WEB-INF/default_user_workspace/projects/
    COPY myDependency.car /usr/local/tomcat/webapps/convertigo/WEB-INF/default_user_workspace/projects/

## Migrate from an earlier version of Convertigo Low Code Platform

-	Stop the container to perform a backup. And just back the workspace directory. This will backup all the projects definitions and some project data.
-	Start a new Convertigo docker container mapping the workspace
-	All the workspace (Projects) will be automatically migrated to the new Convertigo MBaaS version

## Security

The default administration account of a Convertigo server is **admin** / **admin** and the **testplatform** is anonymous.

These accounts can be configured through the *administration console* and saved in the **workspace**.

### `CONVERTIGO_ADMIN_USER` and `CONVERTIGO_ADMIN_PASSWORD` Environment variables

You can change the default administration account :

    docker run -d --name C8O -e CONVERTIGO_ADMIN_USER=administrator -e CONVERTIGO_ADMIN_PASSWORD=s3cret -p 28080:28080 convertigo

### `CONVERTIGO_TESTPLATFORM_USER` and `CONVERTIGO_TESTPLATFORM_PASSWORD` Environment variables

You can lock the **testplatform** by setting the account :

    docker run -d --name C8O -e CONVERTIGO_TESTPLATFORM_USER=tp_user -e CONVERTIGO_TESTPLATFORM_PASSWORD=s3cret -p 28080:28080 convertigo

## `JAVA_OPTS` Environment variable

Convertigo is based on a *Java* process with some defaults *JVM* options. You can override our defaults *JVM* options with you own.

Add any *Java JVM* options such as -D[something] :

    docker run -d --name C8O -e JAVA_OPTS="-DjvmRoute=server1" -p 28080:28080 convertigo

[Here the list of convertigo specific properties](https://www.convertigo.com/documentation/latest/operating-guide/appendixes/#list-of-convertigo-java-system-properties) (don't forget the `-Dconvertigo.engine.` prefix).

## `JXMX` Environment variable

Convertigo tries to allocate this amount of memory in the container and will automatically reduce it until the value is compatible for the Docker memory constraints. Once the best value found, it is used as `-Xmx=${JXMX}m` parameter for the JVM.

The default `JXMX` value is `2048` and can be defined :

    docker run -d --name C8O -e JXMX="4096" -p 28080:28080 convertigo

## `COOKIE_PATH` Environment variable

Convertigo generates a `JSESSIONID` to maintain the user session and stores in a *cookie*. The *cookie* is set for the server path `/` by default. In case of a front server with multiple services for different paths, you can set a path restriction for the *cookie* with the `JSESSIONID`. Just define the `COOKIE_PATH` environment variable with a compatible path.

The default `COOKIE_PATH` value is `/` and can be defined :

    docker run -d --name C8O -e COOKIE_PATH="/convertigo" -p 28080:28080 convertigo

## `COOKIE_SECURE` Environment variable

Convertigo use a *cookie* to maintain sessions. Requests on port `28080` are *HTTP* but we advice to use an *HTTPS* front for production (nginx, kubenetes ingress, ...). In this case, you can secure yours cookies to be used only with secured connections by adding the `Secure` flag.

The Secure flag can be enabled by setting the `COOKIE_SECURE` environment variable to `true`. Once enabled, cookies and sessions aren't working through an *HTTP* connection.

The default `COOKIE_SECURE` value is `false` and can be defined :

    docker run -d --name C8O -e COOKIE_SECURE="true" -p 28080:28080 convertigo

## `COOKIE_SAMESITE` Environment variable

Allow to configure the *SameSite* parameter for generated cookies. Can be empty, `none`, `lax` or `strict`.

The default `COOKIE_SAMESITE` value is *empty* and can be defined this way:

    docker run -d –name C8O -e COOKIE_SAMESITE=lax -p 28080:28080 convertigo

## `SESSION_TIMEOUT` Environment variable

Allow to configure the default Tomcat *session-timeout* in minutes. This value is used for non-project calls (Administration console, Fullsync...). This value is overridden by each projects' calls (Sequence, Transaction ...).

The default `SESSION_TIMEOUT` value is *30* and can be defined this way:

    docker run -d –name C8O -e SESSION_TIMEOUT=5 -p 28080:28080 convertigo

## `DISABLE_SUDO` Environment variable

The image include *sudo* command line, configured to allow the *convertigo* user to use it without password and to perform some *root* action inside the container. This variable allow to disable this permission.

The default `DISABLE_SUDO` value is *empty* and can be defined this way:

    docker run -d –name C8O -e DISABLE_SUDO=true -p 28080:28080 convertigo


## Pre configurated Docker compose stack

You can use this [stack](https://github.com/convertigo/docker/blob/master/compose/mbaas/docker-compose.yml) to run a complete Convertigo Low Code server with FullSync repository and MySQL analytics in a few command lines.

	mkdir c8oMBaaS
	cd c8oMBaaS
	wget https://raw.githubusercontent.com/convertigo/docker/master/compose/mbaas/docker-compose.yml
	docker-compose up -d
