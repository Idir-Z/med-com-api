package com.zidir.medcom.service.mapper;

import com.zidir.medcom.domain.Pharmacy;
import com.zidir.medcom.domain.Product;
import com.zidir.medcom.domain.WatchListItem;
import com.zidir.medcom.service.dto.PharmacyDTO;
import com.zidir.medcom.service.dto.ProductDTO;
import com.zidir.medcom.service.dto.WatchListItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WatchListItem} and its DTO {@link WatchListItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface WatchListItemMapper extends EntityMapper<WatchListItemDTO, WatchListItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "pharmacy", source = "pharmacy", qualifiedByName = "pharmacyId")
    WatchListItemDTO toDto(WatchListItem s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("pharmacyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PharmacyDTO toDtoPharmacyId(Pharmacy pharmacy);
}
