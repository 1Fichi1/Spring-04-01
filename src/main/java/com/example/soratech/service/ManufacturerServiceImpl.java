package com.example.soratech.service;

import com.example.soratech.model.Manufacturer;
import com.example.soratech.repository.ManufacturerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public Page<Manufacturer> findAllActive(Pageable pageable) {
        return manufacturerRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<Manufacturer> findAllDeleted(Pageable pageable) {
        return manufacturerRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<Manufacturer> searchByName(String name, Pageable pageable) {
        return manufacturerRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name, pageable);
    }

    @Override
    public Page<Manufacturer> filterByCountry(String country, Pageable pageable) {
        return manufacturerRepository.findByCountryAndDeletedFalse(country, pageable);
    }

    @Override
    public List<Manufacturer> findAllActive() {
        return manufacturerRepository.findAllByDeletedFalse();
    }

    @Override
    public Manufacturer findById(Long id) {
        return manufacturerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Производитель с ID " + id + " не найден"));
    }

    @Override
    public void save(Manufacturer manufacturer) {
        manufacturer.setDeleted(false);
        manufacturerRepository.save(manufacturer);
    }

    @Override
    public void update(Long id, Manufacturer manufacturer) {
        Manufacturer existing = findById(id);
        existing.setName(manufacturer.getName());
        existing.setCountry(manufacturer.getCountry());
        existing.setEmail(manufacturer.getEmail());
        existing.setPhone(manufacturer.getPhone());
        existing.setAddress(manufacturer.getAddress());
        manufacturerRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        Manufacturer manufacturer = findById(id);
        manufacturer.setDeleted(true);
        manufacturerRepository.save(manufacturer);
    }

    @Override
    public void logicDeleteAllByIds(List<Long> ids) {
        List<Manufacturer> manufacturers = manufacturerRepository.findAllById(ids);
        manufacturers.forEach(manufacturer -> manufacturer.setDeleted(true));
        manufacturerRepository.saveAll(manufacturers);
    }

    @Override
    public void delete(Long id) {
        Manufacturer manufacturer = findById(id);
        manufacturerRepository.delete(manufacturer);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        manufacturerRepository.deleteAllById(ids);
    }

    @Override
    public void restore(Long id) {
        Manufacturer manufacturer = findById(id);
        manufacturer.setDeleted(false);
        manufacturerRepository.save(manufacturer);
    }
}



