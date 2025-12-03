package com.zidir.medcom.service;

import com.zidir.medcom.domain.Pharmacy;
import com.zidir.medcom.repository.PharmacyRepository;
import com.zidir.medcom.service.dto.PharmacyDTO;
import com.zidir.medcom.service.mapper.PharmacyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.zidir.medcom.domain.Pharmacy}.
 */
@Service
@Transactional
public class PharmacyService {

    private static final Logger LOG = LoggerFactory.getLogger(PharmacyService.class);

    private final PharmacyRepository pharmacyRepository;

    private final PharmacyMapper pharmacyMapper;

    public PharmacyService(PharmacyRepository pharmacyRepository, PharmacyMapper pharmacyMapper) {
        this.pharmacyRepository = pharmacyRepository;
        this.pharmacyMapper = pharmacyMapper;
    }

    /**
     * Save a pharmacy.
     *
     * @param pharmacyDTO the entity to save.
     * @return the persisted entity.
     */
    public PharmacyDTO save(PharmacyDTO pharmacyDTO) {
        LOG.debug("Request to save Pharmacy : {}", pharmacyDTO);
        Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDTO);
        pharmacy = pharmacyRepository.save(pharmacy);
        return pharmacyMapper.toDto(pharmacy);
    }

    /**
     * Update a pharmacy.
     *
     * @param pharmacyDTO the entity to save.
     * @return the persisted entity.
     */
    public PharmacyDTO update(PharmacyDTO pharmacyDTO) {
        LOG.debug("Request to update Pharmacy : {}", pharmacyDTO);
        Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDTO);
        pharmacy = pharmacyRepository.save(pharmacy);
        return pharmacyMapper.toDto(pharmacy);
    }

    /**
     * Partially update a pharmacy.
     *
     * @param pharmacyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PharmacyDTO> partialUpdate(PharmacyDTO pharmacyDTO) {
        LOG.debug("Request to partially update Pharmacy : {}", pharmacyDTO);

        return pharmacyRepository
            .findById(pharmacyDTO.getId())
            .map(existingPharmacy -> {
                pharmacyMapper.partialUpdate(existingPharmacy, pharmacyDTO);

                return existingPharmacy;
            })
            .map(pharmacyRepository::save)
            .map(pharmacyMapper::toDto);
    }

    /**
     * Get all the pharmacies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PharmacyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Pharmacies");
        return pharmacyRepository.findAll(pageable).map(pharmacyMapper::toDto);
    }

    /**
     * Get one pharmacy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PharmacyDTO> findOne(Long id) {
        LOG.debug("Request to get Pharmacy : {}", id);
        return pharmacyRepository.findById(id).map(pharmacyMapper::toDto);
    }

    /**
     * Delete the pharmacy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Pharmacy : {}", id);
        pharmacyRepository.deleteById(id);
    }
}
