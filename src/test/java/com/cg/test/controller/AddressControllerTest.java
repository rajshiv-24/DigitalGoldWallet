package com.cg.test.controller;

import com.cg.controller.AddressController;
import com.cg.dto.AddressRequestDTO;
import com.cg.entity.Address;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressRepository;
import com.cg.service.AddressService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class AddressControllerTest {

    @MockitoBean
    private AddressService addressService;

    @MockitoBean
    private AddressRepository addressRepository;

    @Autowired
    private AddressController addressController;

    private AddressRequestDTO requestDTO;
    private Address address;

    @BeforeEach
    public void setup() {
        requestDTO = new AddressRequestDTO(
                "Connaught Place",
                "New Delhi",
                "Delhi",
                "110001",
                "India"
        );

        address = new Address();
        address.setAddressId(1);
        address.setStreet("Connaught Place");
        address.setCity("New Delhi");
        address.setState("Delhi");
        address.setPostalCode("110001");
        address.setCountry("India");
    }
    @Test
    public void testCreateAddress() {
        Mockito.when(addressService.createAddress(Mockito.any()))
                .thenReturn(address);

        ResponseEntity<Address> response = addressController.createAddress(requestDTO);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("New Delhi", response.getBody().getCity());
    }

    @Test
    public void testGetAllAddresses() {
        Mockito.when(addressService.getAllAddresses())
                .thenReturn(List.of(address));

        List<Address> list = addressController.getAllAddresses();

        Assertions.assertEquals(1, list.size());
    }

    @Test
    public void testGetAddressById() {
        Mockito.when(addressService.getAddressById(1))
                .thenReturn(address);

        Address result = addressController.getAddressById(1);

        Assertions.assertEquals(1, result.getAddressId());
    }

    @Test
    public void testGetAddressById_NotFound() {
        Mockito.when(addressService.getAddressById(99))
                .thenThrow(new ResourceNotFoundException("Not found"));

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> addressController.getAddressById(99));
    }

    @Test
    public void testUpdateAddress() {
        Mockito.when(addressService.updateAddress(Mockito.eq(1), Mockito.any()))
                .thenReturn(address);

        Address updated = addressController.updateAddress(1, requestDTO);

        Assertions.assertEquals("New Delhi", updated.getCity());
    }

    @Test
    public void testDeleteAddress() {
        Mockito.doNothing().when(addressService).deleteAddress(1);

        ResponseEntity<Void> response = addressController.deleteAddress(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testCountAddresses() {
        Mockito.when(addressRepository.count()).thenReturn(10L);

        Map<String, Long> result = addressController.countAddresses();

        Assertions.assertEquals(10L, result.get("count"));
    }

    @Test
    public void testAddressExists_True() {
        Mockito.when(addressRepository.existsById(1)).thenReturn(true);

        Map<String, Boolean> result = addressController.addressExists(1);

        Assertions.assertTrue(result.get("exists"));
    }

    @Test
    public void testAddressExists_False() {
        Mockito.when(addressRepository.existsById(99)).thenReturn(false);

        Map<String, Boolean> result = addressController.addressExists(99);

        Assertions.assertFalse(result.get("exists"));
    }

    @Test
    public void testGetAddressesByCity() {
        Mockito.when(addressRepository.findAll())
                .thenReturn(List.of(address));

        List<Address> result = addressController.getAddressesByCity("New Delhi");

        Assertions.assertEquals(1, result.size());
    }


    @Test
    public void testGetAddressesByCountry() {
        Mockito.when(addressRepository.findAll())
                .thenReturn(List.of(address));

        List<Address> result = addressController.getAddressesByCountry("India");

        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testSearchAddresses() {
        Mockito.when(addressRepository.findAll())
                .thenReturn(List.of(address));

        List<Address> result = addressController.searchAddresses("delhi");

        Assertions.assertFalse(result.isEmpty());
    }
}