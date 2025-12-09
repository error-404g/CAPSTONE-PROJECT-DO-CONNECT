@OneToOne --- Many ---- @ManyToOne--- @ManyToMany

Two more annotations

1)
@Embeddable -- Using the attributes of one class to another 

for eg:
(which has not existence without any entity class)
@Embeddable
public class Address
{
private String street;
private String city;
private String zip;

}

@Entity
public class Student
{
@Id
@GeneratedValue
private Long id;

private String name;

@Embdedded  // Will embed the fields or properties of address in student entity class
private Address address;

}

for eg : the result will look like this
id name street city zip
12 fdsf dfd    nd    23

---Another way 

2) @DiscriminatorValue --  generally we use it with InheritanceType
In inheritance mapping (@Inheritance(Single_table) , all subclasses are stored in one table). JPA adds a hidden column (called Discriminator Column  with any name you can give)to differentiate rows.
@DiscriminatorVale("address") will tell to hibernate : that when dtype = address  it will map to address entity

for eg:

@Entity
@Inheritance(strategy = InheritanceType.Single_Table)
@DiscriminatorColumn(name="dtype")
public abstract class Person
{

@Id @GeneratedValue
Long id;
String name;


}





3) 

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends Person
{
private String course;

}

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends Person
{
private String subject;

}

// Output will be like:

id	name	course	subject	 dtype
1	Niti	BTech	null     STUDENT
2       Jiya	null    AI	 TEACHER 

The inheritance type will be 3 types

1) SingleTable -- There is one Parent Table and rest are the child tables ( Unnecessary memory wastage (nullable columns for subclass) . We use discriminator type and value, 
2) Joined -- Each class has its own table and Parent table has common fields , child tables have specific fields , queries require the joins ( No null)
3) Table_Per_Class -- There is no common Parent Table , No joins when querying child entities, it's hard to query because there is no common parent table
for eg:

@Entity
@Inheritance(strategy = InheritanceType.Table_Per_Class)
public abstract class Person
{

@Id @GeneratedValue
Long id;
String name;


}

@Entity
public class Student extends Person
{
private String course;

}

@Entity
public class Teacher extends Person
{
private String subject;

}

// output looks like:
Student :  id name course
Teacher :  id name subject

//

@EntityGraph --  Normally , JPA Associations are lazy(eg: @OneToMany , @ManyTOMany) , If you fetch an entity and later try entity.getChildren outside the transaction you get and error as 

LazyInitializationException : Could not initialize proxy - no session

so @EntityGraph solves this by telling JPA as when you fetch this entity , also fetch these associations eagerly in one query.

@EntityGraph :- Controls fetch strategy per query -- avoids LazyInitializationexception

Polymorphic Behaviour : To query with Parent  and you will get all childs automatically



