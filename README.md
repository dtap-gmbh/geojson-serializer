# geojson-serializer
A library with a JsonSerializer and a set of annotations to serialize any PoJo as GeoJSON.

The serialization of Geometry objects from the **JTS Technology Suite** is handled by the **graphopper implemenation**
of **jackson-datatype-jts**. The original **jackson-datatype-jts** project is not maintained anymore and uses an old
version of the **JTS Topology Suite**, which can cause a nameing conflict due to the different package name.

This library uses the **org.locationtech.jts.geom** classes.

## Links 

 - [GeoJson Specification](https://tools.ietf.org/html/rfc7946)
 - [Jackson Databind](https://github.com/FasterXML/jackson-databind)
 - [JTS Topology Suite](https://github.com/locationtech/jts)
 - [graphhopper implementation of jackson-datatype-jts](https://github.com/graphhopper/jackson-datatype-jts)

## Status
The status of this library is something like **alpha**. Feature and FeatureCollection seem to work pretty well. However, 
the test coverage is low on other GeoJSON types and even some implementation is missing.

Please let me know if you have any ideas for improvement.

## Maven Dependency
```xml
<dependency>
    <groupId>gmbh.dtap.geojson</groupId>
    <artifactId>geojson-serializer</artifactId>
    <version>0.2.0-SNAPSHOT</version> 
</dependency>
```

## Example for Feature
The following PoJo (Attraction) is annotated to be serialized as a GeoJSON Feature.
It has a field annotation to point out the Feature's id, two String properties and one geometry (Point).

```java
    import com.fasterxml.jackson.databind.annotation.JsonSerialize;
    import gmbh.dtap.geojson.annotation.*;
    import gmbh.dtap.geojson.serializer.*;
    import org.locationtech.jts.geom.Point;
    import java.util.UUID;
    
    @GeoJson(type = GeoJsonType.FEATURE)
    @JsonSerialize(using = GeoJsonSerializer.class)
    public class Attraction {
    
       @GeoJsonId private UUID id;
       @GeoJsonProperty private String name;
       @GeoJsonProperty private String description;
       @GeoJsonGeometry private Point location;
    
       public Attraction(UUID id, String name, String description, Point location) {
          this.id = id;
          this.name = name;
          this.description = description;
          this.location = location;
       }
    
       public UUID getId() {
          return id;
       }
    
       public String getName() {
          return name;
       }
    
       public String getDescription() {
          return description;
       }
    
       public Point getLocation() {
          return location;
       }
    }
```

Programmatically calling the ObjectMapper to serialize:  
```java
   import com.fasterxml.jackson.core.JsonProcessingException;
   import com.fasterxml.jackson.databind.ObjectMapper;
   import org.locationtech.jts.geom.Coordinate;
   import org.locationtech.jts.geom.GeometryFactory;
   
   import static java.util.UUID.randomUUID;
    
   public static void main(String... args) throws JsonProcessingException {
      
      ObjectMapper objectMapper = new ObjectMapper();
      GeometryFactory geometryFactory = new GeometryFactory();
            
      Attraction attraction = new Attraction(
            randomUUID(),
            "Eiffel Tower",
            "Champ de Mars, 5 Avenue Anatole France, 75007 Paris, Frankreichh",
            geometryFactory.createPoint(new Coordinate(2.294527, 48.859092)));
      
      String geoJson = objectMapper.writerWithDefaultPrettyPrinter()
                                   .writeValueAsString(attraction);
   }

```

Output:
```json
    {
      "type" : "Feature",
      "id" : "2e38f4b1-d9ea-459a-ba52-6093b09b6dc0",
      "geometry" : {
        "type" : "Point",
        "coordinates" : [ 2.294527, 48.859092 ]
      },
      "properties" : {
        "name" : "Eiffel Tower",
        "description" : "Champ de Mars, 5 Avenue Anatole France, 75007 Paris, Frankreichh"
      }
    }
```

Works with Spring MVC:
```java
   @GetMapping("/api/attractions/{id}")
   public ResponseEntity<Attraction> findById(@PathVariable(value = "id") UUID id) {
      return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
   }
```
(of course this example would require JPA annotations as well)

## Annotation Combinations

**@GeoJson(type=GeoJsonType.FEATURE)**
 - @GeoJsonId {0,1}
 - @GeoJsonGeometry {0,1}
 - @GeoJsonProperties {0,1} **or** @GeoJsonProperty {0,}

**@GeoJson(type=GeoJsonType.FEATURE_COLLECTION)**
 - @GeoJsonFeatures {0,1}

**@GeoJson(type=GeoJsonType.POINT)**, **@GeoJson(type=GeoJsonType.MULTI_POINT)**, 
**@GeoJson(type=GeoJsonType.LINE_STRING)**, **@GeoJson(type=GeoJsonType.MULTI_LINE_STRING)**
**@GeoJson(type=GeoJsonType.POLYGON)**, **@GeoJson(type=GeoJsonType.MULTI_POLYGON)**
 - @GeoJsonGeometry {0,1}

**@GeoJson(type=GeoJsonType.GEOMETRY_COLLECTION**
 - not supported yet


## Credits

Copyright (c) 2019 DTAP GmbH

Please refer to the LICENSE file for details.
