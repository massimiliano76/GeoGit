######################################################
GeoGit - Geospatial Distributed Version Control System
######################################################

Welcome to the GeoGit project, exploring the use of distributed management of spatial data. GeoGit draws inspiration from `Git <http://git-scm.com/>`_, but adapts its core concepts to handle versioning of geospatial data. Users are able to import raw geospatial data (currently from Shapefiles, PostGIS or SpatiaLite) in to a repository where every change to the data is tracked. These changes can be viewed in a history, reverted to older versions, branched in to sandboxed areas, merged back in, and pushed to remote repositories. GeoGit is written in Java, available under the BSD License.

For background reading see these two papers on the spatial distributed versioning `Concept <http://opengeo.org/publications/distributedversioning/>`_ and 
`Implementation <http://opengeo.org/publications/distributedversioningimplement/>`_..

Details
=======

Project Lead: `Gabriel Roldan <https://github.com/groldan>`_

License: all source code is licensed under the `BSD New License <LICENSE.txt>`_,
except for the GeoServer plugin which is available under the GPL 2.0 License. 

Status: A 0.3 version has been released and it is available for download, with a full commandline 
interface to import data and work with repositories. Performance and scalability improvements are slated for 0.4. 

Download
=========

`Version 0.3.0 <http://sourceforge.net/projects/geogit/files/latest/download>`_ from SourceForge. Documentation available for `download <http://sourceforge.net/projects/geogit/files/geogit-0.3.0/geogit-user-manual-0.3.zip/download>`_ and `online <http://geogit.org/docs/index.html>`_.

Installation
============

Unzip the geogit-cli-app-0.3.0.zip to an applications directory, and then add the unzipped geogit/bin/ folder to your PATH.

Running
=======

See the `QuickStart <http://geogit.org/docs/quickstart.html>`_ guide for an introduction to GeoGit. Additional information available at the full `Manual <http://geogit.org/docs/index.html>`_.

Developing
===========

If you want to get involved in the development of GeoGit, build GeoGit yourself or know more about the technical details behind GeoGit, check the `developers section <https://github.com/opengeo/GeoGit/blob/master/doc/technical/source/developers.rst>`_.

Participation
=============

Everyone is invited to participate in GeoGit and help in its development. Check the `How to help <https://github.com/opengeo/GeoGit/blob/master/helping.rst>`_ section to read about how you can help us improve GeoGit.
