package np.com.badripaudel77.myesearchapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

// Employee.java
// Elasticsearch index name
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "employees")
public class Employee {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String name;
    // don't show response in the JSON response.
    @JsonIgnore
    @Field(type=FieldType.Text)
    private String department;
    // don't write to DB
    @Transient
    private double salary;
    @Field(type = FieldType.Date, pattern = "uuuu-MM-dd")
    private LocalDate joinDate;

    @Field(type=FieldType.Object)
    private Address address;

    public Employee() {}

    public Employee(String id, String name, String department, double salary, LocalDate joinDate, Address address) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.joinDate = joinDate;
        this.address = address;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", joinDate=" + joinDate +
                ", address=" + address +
                '}';
    }
}

