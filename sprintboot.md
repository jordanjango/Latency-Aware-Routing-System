# What is spring boot?
Spring boot is a java framework to build robust and strictly typed backend.

# How does spring boot works?
Let's understand the core of the spring boot application

We have a main calss called application class 
That is the main heart of the java spring boot application

Controllers handle the http request for the applications
Services help us in the Business logic set up.
Repositories are nothing but the data access file
Database offcourse will be storing the data

# What are decorators and why are they used?
Decorators are meta data that provide information about your code to the framework.They start with @ and tell spring how to handle 
my classes and methods.


# Building Models with spring boot

We have different type of modelling in spring boot
One of them is modelling data using class representation which uses various decorators like
-> @Entity : This helps in defining the entity
-> @Data : Defines various things like   * @Data =
➡ @Getter +
➡ @Setter +
➡ @ToString +
➡ @EqualsAndHashCode +
➡ @RequiredArgsConstructor
@Entity /*Defines that it is an entity*/
@Table(name = "users") // "user" is a reserved keyword in some DBs, so use "users"
@Data /*Lombok offers this decorrator to auto generate the getter setter to string equals hash code etc inside the public class */
@NoArgsConstructor/**Defines a no arg constructor helps in creating blank objects */
@AllArgsConstructor/**Defines an all arg constructor (Helps in creating a single line arg) */
@Builder /*Builds a user building object helps in writting clean code */
