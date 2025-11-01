package com.example.soratech.service;

import com.example.soratech.model.Supplier;
import com.example.soratech.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Page<Supplier> findAllActive(Pageable pageable) {
        return supplierRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<Supplier> findAllDeleted(Pageable pageable) {
        return supplierRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<Supplier> searchByName(String name, Pageable pageable) {
        return supplierRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name, pageable);
    }

    @Override
    public List<Supplier> findAllActive() {
        return supplierRepository.findAllByDeletedFalse();
    }

    @Override
    public Supplier findById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Поставщик с ID " + id + " не найден"));
    }

    @Override
    public void save(Supplier supplier) {
        supplier.setDeleted(false);
        supplierRepository.save(supplier);
    }

    @Override
    public void update(Long id, Supplier supplier) {
        Supplier existing = findById(id);
        existing.setName(supplier.getName());
        existing.setContactPerson(supplier.getContactPerson());
        existing.setEmail(supplier.getEmail());
        existing.setPhone(supplier.getPhone());
        existing.setAddress(supplier.getAddress());
        supplierRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        Supplier supplier = findById(id);
        supplier.setDeleted(true);
        supplierRepository.save(supplier);
    }

    @Override
    public void logicDeleteAllByIds(List<Long> ids) {
        List<Supplier> suppliers = supplierRepository.findAllById(ids);
        suppliers.forEach(supplier -> supplier.setDeleted(true));
        supplierRepository.saveAll(suppliers);
    }

    @Override
    public void delete(Long id) {
        Supplier supplier = findById(id);
        supplierRepository.delete(supplier);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        supplierRepository.deleteAllById(ids);
    }

    @Override
    public void restore(Long id) {
        Supplier supplier = findById(id);
        supplier.setDeleted(false);
        supplierRepository.save(supplier);
    }
}



