package com.geekbrains.springms.address.services;

import com.geekbrains.springms.address.entities.*;
import com.geekbrains.springms.address.mappers.AddressMapper;
import com.geekbrains.springms.address.repositories.*;
import com.geekbrains.springms.api.AddressDto;
import org.hibernate.stat.CacheRegionStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private AddressMapper addressMapper;
    private AddressRepository addressRepository;
    private CountryRepository countryRepository;
    private RegionRepository regionRepository;
    private CityRepository cityRepository;
    private StreetRepository streetRepository;

    @Autowired
    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    @Autowired
    public void setRegionRepository(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }
    @Autowired
    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    @Autowired
    public void setStreetRepository(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Autowired
    public void setAddressMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public Address findById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Cannot find address by id '%s'", id)
                ));
    }

    public List<Address> findAllByUsername(String username) {
        return addressRepository.findAllByUsername(username);
    }

    public Address createOrUpdate(AddressDto addressDto, boolean specialAuthority) {

        Address newAddress = addressMapper.toEntity(addressDto);

        //user can change only his addresses,
        //admin can change whatever he wants
        if (!specialAuthority && newAddress.getId() != null) {
            Optional<Address> byId = addressRepository.findById(newAddress.getId());
            if (byId.isEmpty() || !byId.get().getUsername().equals(newAddress.getUsername())) {
                newAddress.setId(null); //if id not belong to user create new address without error
            }
        }
        return addressRepository.save(newAddress);
    }

    public List<String> getAllCountries() {
        return countryRepository.findAll().stream().map(Country::getName).toList();
    }

    public List<String> getAllRegionsInCountry(String countryName) {
        Country country = countryRepository.findByName(countryName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("No such country as '%s' can be found.", countryName)
                ));
        return regionRepository.findAllByCountry(country).stream().map(Region::getName).toList();
    }

    public List<String> getCitiesInRegion(String regionName, String countryName) {
        Country country = countryRepository.findByName(countryName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("No such country as '%s' can be found.", countryName)
                ));
        Region region = regionRepository.findByNameAndCountry(regionName, country)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("No such region as '%s' can be found in country '%s'.", regionName, countryName)
                ));
        return cityRepository.findAllByRegion(region).stream().map(City::getName).toList();
    }

    public List<String> getStreetsInCity(String cityName, String regionName, String countryName) {
        Country country = countryRepository.findByName(countryName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("No such country as '%s' can be found.", countryName)
                ));
        Region region = regionRepository.findByNameAndCountry(regionName, country)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("No such region as '%s' can be found in country '%s'.", regionName, countryName)
                ));
        City city = cityRepository.findByNameAndRegion(cityName, region)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("No such city as '%s' can be found in region '%s'.", cityName, regionName)
                ));
        return streetRepository.findAllByCity(city).stream().map(Street::getName).toList();
    }

    public void deleteById(Long id, String usernameAuthorized, boolean specialAuthority) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Address with id '%s' cannot be found.", id)
                ));

        if (!usernameAuthorized.equals(address.getUsername()) && !specialAuthority) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request.");
        }

        addressRepository.deleteById(id);
    }
}
