package com.ecommerce.monolith.customer.service;

import com.ecommerce.monolith.customer.dto.CreateCustomerRequest;
import com.ecommerce.monolith.customer.dto.CustomerDTO;
import com.ecommerce.monolith.customer.mapper.CustomerMapper;
import com.ecommerce.monolith.customer.model.Customer;
import com.ecommerce.monolith.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers");
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        log.info("Fetching customer with id: {}", id);
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Customer not found with id: " + id));
        return mapper.toDTO(customer);
    }

    @Override
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer: {}", request.getName());
        Customer customer = mapper.toEntity(request);
        Customer saved = repository.save(customer);
        return mapper.toDTO(saved);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CreateCustomerRequest request) {
        log.info("Updating customer with id: {}", id);
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Customer not found with id: " + id));
        mapper.updateEntity(request, customer);
        Customer updated = repository.save(customer);
        return mapper.toDTO(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Customer not found with id: " + id);
        }
        repository.deleteById(id);
    }
}