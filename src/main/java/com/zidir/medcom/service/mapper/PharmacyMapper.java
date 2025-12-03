package com.zidir.medcom.service.mapper;

import com.zidir.medcom.domain.Pharmacy;
import com.zidir.medcom.service.dto.PharmacyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pharmacy} and its DTO {@link PharmacyDTO}.
 */
@Mapper(componentModel = "spring")
public interface PharmacyMapper extends EntityMapper<PharmacyDTO, Pharmacy> {}
