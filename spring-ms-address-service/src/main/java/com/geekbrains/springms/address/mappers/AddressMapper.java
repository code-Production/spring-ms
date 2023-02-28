package com.geekbrains.springms.address.mappers;

import com.geekbrains.springms.address.entities.*;
import com.geekbrains.springms.address.repositories.*;
import com.geekbrains.springms.api.AddressDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AddressMapper {

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


    public AddressDto toDto(Address address) {
        return new AddressDto(
                address.getId(),
                address.getUsername(),
                address.getStreet().getCity().getRegion().getCountry().getName(),
                address.getStreet().getCity().getRegion().getName(),
                address.getStreet().getCity().getName(),
                address.getStreet().getName(),
                address.getHouseNumber(),
                address.getApartmentNumber()
        );
    }

    @Transactional
    public Address toEntity(AddressDto addressDto) {

        //check correctness of Dto content
        Country country = countryRepository.findByName(addressDto.getCountry())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("No such country as '%s' can be found.", addressDto.getCountry())
                ));

        Region region = regionRepository.findByNameAndCountry(addressDto.getRegion(), country)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format(
                                "No such region as '%s' can be found in country '%s'.",
                                addressDto.getRegion(),
                                country.getName()
                        )
                ));
        City city = cityRepository.findByNameAndRegion(addressDto.getCity(), region)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format(
                                "No such city as '%s' can be found in region '%s'.",
                                addressDto.getCity(),
                                region.getName()
                        )
                ));
        Street street = streetRepository.findByNameAndCity(addressDto.getStreet(), city)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format(
                                "No such street as '%s' can be found in city '%s'.",
                                addressDto.getStreet(),
                                city.getName()
                        )
                ));

        return new Address(
                addressDto.getId(),
                addressDto.getUsername(),
                street,
                addressDto.getHouseNumber(),
                addressDto.getApartmentNumber()
        );
    }


}
