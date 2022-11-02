# Running the REST FileSystem Adapter

## Using Docker

### Using Dropwizard Docker Image

> :warning: Docker cannot be installed on Virtual Machines in our infrastructure.

Make sure [Docker for Desktop](https://docs.docker.com/desktop/install/windows-install/) is installed.

Open command prompt and run:

```powershell
docker run -p 8080:8080 docker.io/apolloscm/prereleases:restadapter-filesystem-dropwizard-1.0.0-SNAPSHOT
```

## Using Tomcat 9 and WAR File on Windows

Apache Tomcat can be used to run the REST FileSystem Adapter. This method can be used to run the adapter on a VM.

Install [Tomcat 9](https://tomcat.apache.org/download-90.cgi).
 Chocolatey can be used for that purpose:

```powershell
choco install tomcat -y
```

Change the user running `Apache Tomcat 9.0` service to `Local System`. This will avoid any folder permissions issues.

Download the latest WAR file from [Sonatype](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/fileanalysissuite/restadapters/filesystem/restadapter-filesystem-war/).

Rename the WAR file to `restadapter-filesystem.war` and copy it into `%CATALINA_BASE%\webapps` folder (by default 'C:\ProgramData\Tomcat9\webapps').

Open the users configuration file located at `%CATALINA_BASE%\conf\tomcat-users.xml` and add the following line under `tomcat-users`
element:

```xml
<user username="admin" password="admin" roles="manager-gui"/>
```

This will allow you to log into the admin console.

Restart the `Apache Tomcat 9.0` service.

Open the [admin console](http://localhost:8080/manager/html).

Verify that the `/restadapter-filesystem` application is running.

The REST FileSystem Adapter should now be running on localhost under the default Tomcat port 8080:

`http://localhost:8080/restadapter-filesystem/`

Check the `adapterDescriptor` endpoint (update the application name):

`http://localhost:8080/restadapter-filesystem/adapterDescriptor`
