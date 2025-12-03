package com.zidir.medcom.service.mapper;

import com.zidir.medcom.domain.Notification;
import com.zidir.medcom.domain.Pharmacy;
import com.zidir.medcom.domain.User;
import com.zidir.medcom.domain.WatchListItem;
import com.zidir.medcom.service.dto.NotificationDTO;
import com.zidir.medcom.service.dto.PharmacyDTO;
import com.zidir.medcom.service.dto.UserDTO;
import com.zidir.medcom.service.dto.WatchListItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "pharmacy", source = "pharmacy", qualifiedByName = "pharmacyId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "watchListItem", source = "watchListItem", qualifiedByName = "watchListItemId")
    NotificationDTO toDto(Notification s);

    @Named("pharmacyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PharmacyDTO toDtoPharmacyId(Pharmacy pharmacy);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("watchListItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WatchListItemDTO toDtoWatchListItemId(WatchListItem watchListItem);
}
