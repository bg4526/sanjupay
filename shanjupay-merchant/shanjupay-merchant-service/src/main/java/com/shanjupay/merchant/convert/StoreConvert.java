package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.StoreDTO;
import com.shanjupay.merchant.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Classname StoreConvert
 * @Description 商户转换
 * @Date 2020/5/14 23:36
 * @Created by 小二哥
 */
@Mapper
public interface StoreConvert {
    StoreConvert INSTANCE = Mappers.getMapper(StoreConvert.class);

    StoreDTO entity2dto(Store entity);

    Store dto2entity(StoreDTO dto);

    List<StoreDTO> listentity2dto(List<Store> store);

}
    