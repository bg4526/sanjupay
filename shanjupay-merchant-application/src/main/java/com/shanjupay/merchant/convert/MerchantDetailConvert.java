package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.vo.MerchantDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Classname MerchantDetailConvert
 * @Description 商户资质申请信息vo和dto转换
 * @Date 2020/5/8 17:29
 * @Created by 小二哥
 */
@Mapper
public interface MerchantDetailConvert {
    MerchantDetailConvert INSTANCE = Mappers.getMapper(MerchantDetailConvert.class);

    MerchantDTO vo2dto(MerchantDetailVO vo);

    MerchantDetailVO dto2vo(MerchantDTO dto);
}
