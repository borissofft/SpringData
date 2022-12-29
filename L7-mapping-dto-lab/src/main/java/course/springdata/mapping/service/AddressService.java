package course.springdata.mapping.service;

import course.springdata.mapping.entity.Address;

import java.util.List;

public interface AddressService {

    List<Address> getAllAddresses();
    Address getAddressById(Long id);
    Address addAddress(Address address);
    Address updateAddress(Address address);
    Address deleteAddress(Long id);
    Long getAddressesCount();

}
