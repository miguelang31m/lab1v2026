package com.udea.lab1v2026.service;

import com.udea.lab1v2026.dto.CustomerDTO;
import com.udea.lab1v2026.entity.Customer;
import com.udea.lab1v2026.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    private CustomerService service;

    @BeforeEach
    void setUp() {
        service = new CustomerService(customerRepository);
    }

    @Test
    void getAllCustomersReturnsDtos() {
        when(customerRepository.findAll()).thenReturn(List.of(customer(1L, "1001", "Ana", "Rios", 500.0)));

        List<CustomerDTO> result = service.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("1001", result.get(0).getAccountNumber());
    }

    @Test
    void getCustomerByIdReturnsCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer(1L, "1001", "Ana", "Rios", 500.0)));

        CustomerDTO result = service.getCustomerById(1L);

        assertEquals("Ana", result.getFirstName());
        assertEquals(500.0, result.getBalance());
    }

    @Test
    void getCustomerByIdFailsWhenMissing() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.getCustomerById(99L));

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    @Test
    void createCustomerTrimsAndSaves() {
        CustomerDTO input = new CustomerDTO(null, " Ana ", " Rios ", " 1001 ", 500.0);
        when(customerRepository.existsByAccountNumber(" 1001 ")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        CustomerDTO result = service.createCustomer(input);

        assertEquals(1L, result.getId());
        assertEquals("Ana", result.getFirstName());
        assertEquals("1001", result.getAccountNumber());
    }

    @Test
    void createCustomerRejectsDuplicateAccount() {
        CustomerDTO input = new CustomerDTO(null, "Ana", "Rios", "1001", 500.0);
        when(customerRepository.existsByAccountNumber("1001")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createCustomer(input));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void updateCustomerUpdatesExistingCustomer() {
        Customer existing = customer(1L, "1001", "Ana", "Rios", 500.0);
        CustomerDTO input = new CustomerDTO(1L, "Ana Maria", "Rios", "1001", 700.0);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.findByAccountNumber("1001")).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);

        CustomerDTO result = service.updateCustomer(1L, input);

        assertEquals("Ana Maria", result.getFirstName());
        assertEquals(700.0, result.getBalance());
    }

    @Test
    void updateCustomerRejectsAccountUsedByOtherCustomer() {
        Customer existing = customer(1L, "1001", "Ana", "Rios", 500.0);
        Customer other = customer(2L, "2002", "Luis", "Lopez", 100.0);
        CustomerDTO input = new CustomerDTO(1L, "Ana", "Rios", "2002", 500.0);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.findByAccountNumber("2002")).thenReturn(Optional.of(other));

        assertThrows(IllegalArgumentException.class, () -> service.updateCustomer(1L, input));
    }

    @Test
    void deleteCustomerDeletesExistingCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        service.deleteCustomer(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomerRejectsMissingCustomer() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.deleteCustomer(99L));
        verify(customerRepository, never()).deleteById(anyLong());
    }

    private Customer customer(Long id, String account, String firstName, String lastName, Double balance) {
        Customer c = new Customer();
        c.setId(id);
        c.setAccountNumber(account);
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setBalance(balance);
        return c;
    }
}
