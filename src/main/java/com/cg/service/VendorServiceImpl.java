package com.cg.service;

import com.cg.dto.VendorRequestDTO;
import com.cg.entity.Vendors;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepo;

    @Override
    public Vendors createVendor(VendorRequestDTO request) {
        Vendors vendor = new Vendors();
        vendor.setVendorName(request.getVendorName());
        vendor.setDescription(request.getDescription());
        vendor.setContactPersonName(request.getContactPersonName());
        vendor.setContactEmail(request.getContactEmail());
        vendor.setContactPhone(request.getContactPhone());
        vendor.setWebsiteUrl(request.getWebsiteUrl());
        vendor.setTotalGoldQuantity(request.getTotalGoldQuantity());
        vendor.setCurrentGoldPrice(request.getCurrentGoldPrice());
        return vendorRepo.save(vendor);
    }

    @Override
    public Vendors getVendorById(Integer vendorId) {
        return vendorRepo.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor not found with id: " + vendorId));
    }

    @Override
    public List<Vendors> getAllVendors() {
        return vendorRepo.findAll();
    }

    @Override
    public Vendors updateVendor(Integer vendorId, VendorRequestDTO request) {
        Vendors vendor = getVendorById(vendorId);
        vendor.setVendorName(request.getVendorName());
        vendor.setDescription(request.getDescription());
        vendor.setContactPersonName(request.getContactPersonName());
        vendor.setContactEmail(request.getContactEmail());
        vendor.setContactPhone(request.getContactPhone());
        vendor.setWebsiteUrl(request.getWebsiteUrl());
        vendor.setTotalGoldQuantity(request.getTotalGoldQuantity());
        vendor.setCurrentGoldPrice(request.getCurrentGoldPrice());
        return vendorRepo.save(vendor);
    }

    @Override
    public void deleteVendor(Integer vendorId) {
        getVendorById(vendorId);
        vendorRepo.deleteById(vendorId);
    }
}
