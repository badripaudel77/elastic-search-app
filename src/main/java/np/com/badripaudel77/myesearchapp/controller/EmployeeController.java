package np.com.badripaudel77.myesearchapp.controller;

import np.com.badripaudel77.myesearchapp.model.Employee;
import np.com.badripaudel77.myesearchapp.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to my search app.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    // Gets within the range (hardcoded inside the code now). But will be passed as request param or path variable.
    @GetMapping("/range")
    public ResponseEntity<List<Employee>> getEmployeesInRange() throws IOException {
        List<Employee> employees = employeeService.getAllEmployeesInRange();
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    // Using a scenario like nested object (address inside employee) has geo_point (lat & longitude)
    // and finding employees within certain distance
    // eg URL: http://localhost:8080/api/v1/employees/distance?lat=37.7749&lon=-122.4194&distance=1000km
    @GetMapping("/distance")
    public ResponseEntity<List<Employee>> getAllEmployeeInRangeDistance(@RequestParam(name = "lat", required = false)
                                Double lat, @RequestParam(name = "lon", required = false) Double lon,
                                @RequestParam(name = "distance", required = false) String distance) throws IOException {
        List<Employee> employees = employeeService.getAllEmployeeInRangeDistance(lat, lon, distance);
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        var savedEmployee = employeeService.addEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }
}
