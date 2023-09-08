package com.example.demo.service.impl;

import com.example.demo.dto.CustomFieldDto;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.inventory.CustomFieldData;
import com.example.demo.repository.SellerDetailsRepository;
import com.example.demo.repository.inventory.CustomFieldRepository;
import com.example.demo.service.UserAccountService;
import com.example.demo.service.SessionUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final CustomFieldRepository customFieldRepository;
    private final SellerDetailsRepository sellerDetailsRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final SessionUser sessionUser;

    @Override
    public ResponseEntity<SellerDetails> getCurrentUserProfile() {
        Optional<SellerDetails> sellerDetails = sellerDetailsRepository
                .findByUsername(sessionUser.getCurrentUserUsername());
        return sellerDetails
                .map(details -> new ResponseEntity<>(sellerDetails.get() , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null , HttpStatus.NOT_FOUND));

    }
    @Override
    public ResponseEntity<String> setVariantsForCustomFields(List<String> list , String name) {
        try {
            CustomFieldData customFieldData = new CustomFieldData();
            customFieldData.setUsername(sessionUser.getCurrentUserUsername());
            customFieldData.setFieldsUsed(list.size());
            customFieldData.setVariantType(name);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                switch (i) {
                    case 0:
                        customFieldData.setCustomField1(list.get(i).toUpperCase());
                        break;
                    case 1:
                        customFieldData.setCustomField2(list.get(i).toUpperCase());
                        break;
                    case 2:
                        customFieldData.setCustomField3(list.get(i).toUpperCase());
                        break;
                    case 3:
                        customFieldData.setCustomField4(list.get(i).toUpperCase());
                        break;
                    case 4:
                        customFieldData.setCustomField5(list.get(i).toUpperCase());
                        break;
                    case 5:
                        customFieldData.setCustomField6(list.get(i).toUpperCase());
                        break;
                }
            }
            customFieldRepository.save(customFieldData);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<CustomFieldData> getCurrentUserCustomFieldsVariantFromDatabase(String type) {
        Optional<CustomFieldData> customFieldData = customFieldRepository
                .findByUsernameAndVariantType(
                        sessionUser.getCurrentUserUsername() ,
                        type);
        return customFieldData
                .map(fieldData -> new ResponseEntity<>(
                        fieldData ,
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        HttpStatus.NOT_FOUND));
    }
    @Override
    public ResponseEntity<Map<String, String>> getVariantsLists(String type) {
        CustomFieldData customFieldData = getCurrentUserCustomFieldsVariantFromDatabase(type).getBody();
        log.info("custom fields available " + customFieldData);
        Map<String, String> custom = new HashMap<>();
        for (int i = 0; i < Objects.requireNonNull(customFieldData).getFieldsUsed(); i++) {
            switch (i) {
                case 0:
                    custom.put(customFieldData.getCustomField1() , "");
                    break;
                case 1:
                    custom.put(customFieldData.getCustomField2() , "");
                    break;
                case 2:
                    custom.put(customFieldData.getCustomField3() , "");
                    break;
                case 3:
                    custom.put(customFieldData.getCustomField4() , "");
                    break;
                case 4:
                    custom.put(customFieldData.getCustomField5() , "");
                    break;
                case 5:
                    custom.put(customFieldData.getCustomField6() , "");
                    break;

            }
        }
        return new ResponseEntity<>(custom , HttpStatus.OK);
    }
    @Override
    public ResponseEntity<List<CustomFieldDto>> getCurrentUserVariants() {
        Optional<Iterable<CustomFieldData>> strings = customFieldRepository
                .findByUsername(sessionUser.getCurrentUserUsername());

        if (strings.isPresent()) {
            Iterator<CustomFieldData> data = strings.get().iterator();
            List<CustomFieldDto> returnList = new ArrayList<>();
            while (data.hasNext()) {
                CustomFieldData customFieldData = data.next();
                System.out.println(customFieldData);
                CustomFieldDto customFieldDto = new CustomFieldDto();
                int size = customFieldData.getFieldsUsed();

                List<String> list = new ArrayList<>(size);

                for (int i = 0; i < size; i++) {
                    switch (i) {
                        case 0:
                            list.add(i , customFieldData.getCustomField1());
                            break;
                        case 1:
                            list.add(i , customFieldData.getCustomField2());
                            break;
                        case 2:
                            list.add(i , customFieldData.getCustomField3());
                            break;
                        case 3:
                            list.add(i , customFieldData.getCustomField4());
                            break;
                        case 4:
                            list.add(i , customFieldData.getCustomField5());
                            break;
                        case 5:
                            list.add(i , customFieldData.getCustomField6());
                            break;
                    }
                }
                customFieldDto.setList(list);
                customFieldDto.setName(customFieldData.getVariantType());
                returnList.add(customFieldDto);
            }
            return new ResponseEntity<>(
                    returnList ,
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                null ,
                HttpStatus.NO_CONTENT);
    }
    @Override
    public ResponseEntity<String> setInventoryStatus(boolean status) {
        Optional<SellerDetails> sellerDetails = sellerDetailsRepository
                .findByUsername(sessionUser.getCurrentUserUsername());
        if (sellerDetails.isPresent()) {
            sellerDetails.get().setInventory(status);
            sellerDetailsRepository.save(sellerDetails.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Override
    public ResponseEntity<Boolean> getCurrentUserInventoryStatus() {
        Optional<SellerDetails> sellerDetails = sellerDetailsRepository
                .findByUsername(sessionUser.getCurrentUserUsername());
        return sellerDetails
                .map(details ->
                        new ResponseEntity<>(
                                details.isInventory() ,
                                HttpStatus.OK))
                .orElseGet(() ->
                        new ResponseEntity<>(
                                null ,
                                HttpStatus.NO_CONTENT));
    }
    @Override
    public ResponseEntity<SellerDetails> updateSellerDetails(SellerDetails sellerDetails) {
        System.out.println(sellerDetails);

        String oldPublicId = sellerDetails.getLogoPublicIdOld();
        if (oldPublicId != null){
            cloudinaryServiceImpl.removeImg(oldPublicId);
        }
        return new ResponseEntity<>(
                sellerDetailsRepository.save(sellerDetails) ,
                HttpStatus.OK);
    }

}
