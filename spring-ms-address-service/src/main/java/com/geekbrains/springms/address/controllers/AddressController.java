package com.geekbrains.springms.address.controllers;


import com.geekbrains.springms.address.entities.Address;
import com.geekbrains.springms.address.mappers.AddressMapper;
import com.geekbrains.springms.address.services.AddressService;
import com.geekbrains.springms.api.AddressDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Enumeration;
import java.util.List;

@RestController
public class AddressController {

    private AddressMapper addressMapper;
    private AddressService addressService;

    @Autowired
    public void setAddressMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Autowired
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{id}")
    public AddressDto getAddressById(@PathVariable Long id, HttpServletRequest request) {
        String authorizedUsername = checkAuthorizationHeaderOrThrowException(request);
        Address address = addressService.findById(id);
        if (!address.getUsername().equals(authorizedUsername)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request.");
        }
        return addressMapper.toDto(address);
    }

    @GetMapping("/all")
    public List<AddressDto> getUserAddresses(@RequestParam(required = false) String username, HttpServletRequest request) {
        String authorizedUsername = checkAuthorizationHeaderOrThrowException(request);

        boolean specialAuthority = hasSpecialAuthority(request);
        if (username != null && !specialAuthority) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request.");
        }
        if (username == null) {
            username = authorizedUsername;
        }
        List<Address> userAddresses = addressService.findAllByUsername(username);
        return userAddresses.stream().map(address -> addressMapper.toDto(address)).toList();
    }

    @PostMapping("/address")
    public AddressDto createOrUpdateAddress(@RequestBody AddressDto addressDto, HttpServletRequest request) {
        String username = checkAuthorizationHeaderOrThrowException(request);
        boolean specialAuthority = hasSpecialAuthority(request);
        //unauthorized posting
        if (!username.equals(addressDto.getUsername()) && !specialAuthority) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request.");
        }
        Address address = addressService.createOrUpdate(addressDto, specialAuthority);
        return addressMapper.toDto(address);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id, HttpServletRequest request) {
        String usernameAuthorized = checkAuthorizationHeaderOrThrowException(request);
        boolean specialAuthority = hasSpecialAuthority(request);
        addressService.deleteById(id, usernameAuthorized, specialAuthority);
    }

    @GetMapping("/countries")
    public List<String> getAllCountries() {
        return addressService.getAllCountries();
    }

    @GetMapping("/regions")
    public List<String> getAllRegionsInCountry(@RequestParam String country) {
        return addressService.getAllRegionsInCountry(country);
    }

    @GetMapping("/cities")
    public List<String> getAllCitiesInRegion(@RequestParam String region, @RequestParam String country) {
        return addressService.getCitiesInRegion(region, country);
    }

    @GetMapping("/streets")
    public List<String> getAllStreetsInCity(
            @RequestParam String country, @RequestParam String region, @RequestParam String city) {

        return addressService.getStreetsInCity(city, region, country);
    }


    private boolean hasSpecialAuthority(HttpServletRequest request) {
        if (request.getHeaders("roles").hasMoreElements()) {
            Enumeration<String> roles = request.getHeaders("roles");
            while (roles.hasMoreElements()) {
                if (roles.nextElement().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String checkAuthorizationHeaderOrThrowException(HttpServletRequest request) {
        String username = request.getHeader("username");
        if (username != null && !username.isBlank()) {
            return username;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access.");
    }
}
