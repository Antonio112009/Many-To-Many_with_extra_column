![Java version](https://img.shields.io/badge/java%20version-14.0.1-red.svg)
![Maven version](https://img.shields.io/badge/maven%20version-3.6.3-red.svg) <br>
![Hibernate version](https://img.shields.io/badge/hibernate%20version-5.4.12.Final-blue.svg)
![Spring version](https://img.shields.io/badge/spring%20framework-2.2.6.RELEASE-green.svg) <br>
![IntelliJ version](https://img.shields.io/badge/intellij%20version-2020.1.RELEASE-purple.svg)

# Many To Many with extra columns

This is an example of the project based on Vlad Mihalcea's post: [The best way to map a many-to-many association with extra columns when using JPA and Hibernate](https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/)

### Before running the project

_Warning! The project was developed in the Intellij. It may not be compatible with Eclipse._

1. Download repository
2. In file `application.properties` add database connection properties.
3. If you are not using MySQL please change database connector in `pom.xml (look at the dependencies)`

### How to run project (IntelliJ)

1. Open the project
2. Go to folder `ManyToManyWithExtraColumn/src/test/java/com/antonio112009/manyToMany/repository`
3. Open `PostRepositoryTest`
4. On the line 19 you will find run button (green arrow right). Press it and watch console.

### Current issues

1. Unable to connect 2 or more object (that are in the database). Test number 3.

Error log:
```
org.springframework.orm.jpa.JpaSystemException: Could not set field value [SHORT_CIRCUIT_INDICATOR] value by reflection : [class com.antonio112009.manyToMany.entity.PostTagId.tagId] 
setter of com.antonio112009.manyToMany.entity.PostTagId.tagId; nested exception is org.hibernate.PropertyAccessException: Could not set field value [SHORT_CIRCUIT_INDICATOR] 
value by reflection : [class com.antonio112009.manyToMany.entity.PostTagId.tagId] setter of com.antonio112009.manyToMany.entity.PostTagId.tagId
```
