package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.entity.App;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Classname AppCovert
 * @Description 商户转换
 * @Date 2020/5/10 16:56
 * @Created by 小二哥
 */
@Mapper
public interface AppCovert {
    AppCovert INSTANCE = Mappers.getMapper(AppCovert.class);

    AppDTO entity2dto(App entity);

    App dto2entity(AppDTO dto);

    List<AppDTO> listentity2dto(List<App> app);
}
