# JetOrm

JetOrm is a lightweight Object-Relational Mapping (ORM) framework inspired by Hibernate. It simplifies database interaction by automatically generating tables and handling CRUD operations using Java Reflection.

## Features

- **Automatic Table Generation**: Tables are created based on classes annotated with `@Entity`. Primary keys are identified using `@Id` annotations.
  
- **CRUD Operations**: Supports `save`, `getById`, and `deleteById` operations for interacting with the database.
  
- **Exception Handling**: Handles exceptions such as `ClassMismatchException`, `PrimaryKeyNotPresentException`, `UnsupportedDataTypeException`, and SQL-related exceptions.

- **Logging**: Utilizes logging via `DbLogger` for tracing, debugging, info, warning, and error messages.

- **Configuration**: Database connection configuration is managed through `DatabaseConnectionManager`, which loads properties from a configuration file using `PropertyLoader`.

- **Annotations**: Annotations like `@Entity`, `@Id`, and `@Column` provide metadata for entity mapping and column definitions.

## Usage

### Configuration

```java
//add database properties in database.properties file
database.driver.name=com.mysql.cj.jdbc.Driver
database.host=jdbc:mysql://localhost/demo?
database.username=root
database.password=password
```

```java
// Configure path of your project
JetOrm jetOrm = new JetOrmImpl();
jetOrm.configure("org.dummy")
```

### Save or Update Object

```java
Userr userr = new Userr(1l, "ABC", "abc@gmail.com");
jetOrm.save(Userr.class, userr);
```

### Retrieve Object by ID

```java
Object user = jetOrm.getById(Userr.class, 1l);
```

### Delete Object by ID

```java
jetOrm.deleteById(Userr.class, 1l);
```

## Getting Started

1. **Setup Database**: Ensure your database is set up with appropriate permissions and a schema.

2. **Add Annotations**: Use `@Entity`, `@Id`, and `@Column` annotations to define your entity classes and their mappings.

3. **Configure Path**: Set the path to scan for entity classes in `JetOrmImpl.configure(path)`.

4. **Use JetOrm**: Instantiate `JetOrm` and start performing CRUD operations on your entities.

## Dependencies

- Java 8 or higher
---

Thank You!
