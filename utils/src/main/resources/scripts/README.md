# Generating JAXB Classes for Serialization

To make the marshalling easier inside the LRD we use a JAXB Renderer. This allows us to control the format of the XML generated for each domain and
therefore generate valid XML matching the schemas which have been supplied by Genomics England (GeL).

The method is simple, run the `generate_message_jaxb_classes.sh` file. This will generate all classes linked to the core schema specified, making
use of the `binding.xjb` file, which should lie alongiside the core XSD file, to place all generated classes into the correct package and make any
other necessary changes.

The script makes use of the XJC executable included as part of the JVM so you will need to have Java installed.

## Running

```bash
$ cd ouh_lrd/lrd-server/src/main/resources/scripts
$ ./generate_message_jaxb_classes.sh
```

## After Generation

Currently there are some extra steps which do need to be performed.
This is mainly due to the inability to control all functions of the XJC.

**Note:**
Because of these changes you should ideally only run the generation script once as you will need to make the changes again every time you run the
script.

### All Packages

* Update all ObjectFactory.java files by adding:
  extends AbstractObjectFactory

## CoreClinical Package

* Update corelinical/package-info.java with the following XmlSchema annotation:

```java
@javax.xml.bind.annotation.XmlSchema(namespace = "https://genomicsengland.co.uk/xsd",
           elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
           xmlns = @javax.xml.bind.annotation.XmlNs(namespaceURI = "https://genomicsengland.co.uk/xsd", prefix = "")
           )
```
