package com.example.reactive.controller;

import com.example.reactive.model.Employee;
import com.example.reactive.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<Employee> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Employee> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> create(@RequestBody Employee employee) {
        return repository.save(employee);
    }

    @PutMapping("/{id}")
    public Mono<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setName(employee.getName());
                    existing.setRole(employee.getRole());
                    return repository.save(existing);
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return repository.deleteById(id);
    }
}
