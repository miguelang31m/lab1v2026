package com.udea.lab1v2026.service;

import com.udea.lab1v2026.dto.CustomerDTO;
import com.udea.lab1v2026.entity.Customer;
import com.udea.lab1v2026.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toDTO).toList();
    }

    public CustomerDTO getCustomerById(Long id) {
        return toDTO(customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado")));
    }

    public CustomerDTO createCustomer(CustomerDTO dto) {
        if (customerRepository.existsByAccountNumber(dto.getAccountNumber())) {
            throw new IllegalArgumentException("Ya existe un cliente con ese número de cuenta");
        }
        Customer customer = new Customer();
        copy(dto, customer);
        return toDTO(customerRepository.save(customer));
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        customerRepository.findByAccountNumber(dto.getAccountNumber())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> { throw new IllegalArgumentException("Ya existe un cliente con ese número de cuenta"); });
        copy(dto, customer);
        return toDTO(customerRepository.save(customer));
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) throw new IllegalArgumentException("Cliente no encontrado");
        customerRepository.deleteById(id);
    }

    private void copy(CustomerDTO dto, Customer customer) {
        customer.setFirstName(dto.getFirstName().trim());
        customer.setLastName(dto.getLastName().trim());
        customer.setAccountNumber(dto.getAccountNumber().trim());
        customer.setBalance(dto.getBalance());
    }

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(c.getId(), c.getFirstName(), c.getLastName(), c.getAccountNumber(), c.getBalance());
    }
}
