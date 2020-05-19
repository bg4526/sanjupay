package com.shanjupay.transaction.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shanjupay.common.cache.Cache;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.RedisUtil;
import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.api.dto.PayChannelDTO;
import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.api.dto.PlatformChannelDTO;
import com.shanjupay.transaction.convert.PayChannelParamConvert;
import com.shanjupay.transaction.convert.PlatformChannelConvert;
import com.shanjupay.transaction.entity.AppPlatformChannel;
import com.shanjupay.transaction.entity.PayChannelParam;
import com.shanjupay.transaction.entity.PlatformChannel;
import com.shanjupay.transaction.mapper.AppPlatformChannelMapper;
import com.shanjupay.transaction.mapper.PayChannelParamMapper;
import com.shanjupay.transaction.mapper.PlatformChannelMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Classname PayChannelServiceImpl
 * @Description 支付渠道服务 管理平台支付渠道，原始支付渠道，以及相关配置实现类
 * @Date 2020/5/11 23:35
 * @Created by 小二哥
 */
@Slf4j
@Transactional
@org.apache.dubbo.config.annotation.Service
public class PayChannelServiceImpl implements PayChannelService {

    @Autowired
    private PlatformChannelMapper platformChannelMapper;
    @Autowired
    private AppPlatformChannelMapper appPlatformChannelMapper;
    @Autowired
    private PayChannelParamMapper payChannelParamMapper;
    @Autowired
    private Cache cache;

    /**
     * 查询平台的服务类型
     *
     * @return
     * @throws BusinessException
     */
    @Override
    public List<PlatformChannelDTO> queryPlatformChannel() throws BusinessException {
        List<PlatformChannel> platformChannels = platformChannelMapper.selectList(null);
        log.info("PayChannelServiceImpl::支付渠道数据：{}", platformChannels);
        List<PlatformChannelDTO> platformChannelDTOS = PlatformChannelConvert.INSTANCE.listentity2listdto(platformChannels);
        return platformChannelDTOS;
    }

    /**
     * 为某个应用绑定一个服务类型
     *
     * @param appId                应用id
     * @param platformChannelCodes 服务类型的code
     * @throws BusinessException
     */
    @Override
    public void bindPlatformChannelForApp(String appId, String platformChannelCodes) throws BusinessException {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>()
                .eq(AppPlatformChannel::getAppId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannelCodes));
        if (null == appPlatformChannel) {
            appPlatformChannel = new AppPlatformChannel();
            appPlatformChannel.setAppId(appId);
            appPlatformChannel.setPlatformChannel(platformChannelCodes);
            appPlatformChannelMapper.insert(appPlatformChannel);
        }
    }

    /**
     * 应用绑定服务类型的状态
     *
     * @param appId
     * @param platformChannel
     * @return 已绑定 1,否则0
     * @throws BusinessException
     */
    @Override
    public int queryAppBindPlatformChannel(String appId, String platformChannel) throws BusinessException {
        int count = appPlatformChannelMapper.selectCount(
                new QueryWrapper<AppPlatformChannel>().
                        lambda().
                        eq(AppPlatformChannel::getAppId, appId).
                        eq(AppPlatformChannel::getPlatformChannel, platformChannel));
        //已存在绑定关系返回1
        if (count > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 根据服务类型查询支付渠道
     *
     * @param platformChannelCode 服务类型编码
     * @return 支付渠道列表
     * @throws BusinessException
     */
    @Override
    public List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode) throws BusinessException {
        return appPlatformChannelMapper.selectPayChannelByPlatformChannel(platformChannelCode);
    }

    /**
     * 支付渠道参数配置
     *
     * @param payChannelParamDTO 配置支付渠道参数：包括：商户id、应用id，服务类型code，支付渠道code，配置名称，配置参数(json)
     * @throws BusinessException
     */
    @Override
    public void savePayChannelParam(PayChannelParamDTO payChannelParamDTO) throws BusinessException {
        if (payChannelParamDTO == null || StringUtils.isBlank(payChannelParamDTO.getAppId()) || StringUtils.isBlank(payChannelParamDTO.getPlatformChannelCode()) || StringUtils.isBlank(payChannelParamDTO.getPayChannel())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }//根据appid和服务类型查询应用与服务类型绑定id
        Long appPlatformChannelId = selectIdByAppPlatformChannel(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());
        if (appPlatformChannelId == null) {
            //应用未绑定该服务类型不可进行支付渠道参数配置
            throw new BusinessException(CommonErrorCode.E_300010);
        }
        //根据应用与服务类型绑定id和支付渠道查询参数信息
        PayChannelParam payChannelParam = payChannelParamMapper.selectOne(new LambdaQueryWrapper<PayChannelParam>().eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId).eq(PayChannelParam::getPayChannel, payChannelParamDTO.getPayChannel()));
        //更新已有配置
        if (payChannelParam != null) {
            payChannelParam.setChannelName(payChannelParamDTO.getChannelName());
            payChannelParam.setParam(payChannelParamDTO.getParam());
            payChannelParamMapper.updateById(payChannelParam);
        } else {
            //添加新配置
            PayChannelParam entity = PayChannelParamConvert.INSTANCE.dto2entity(payChannelParamDTO);
            entity.setId(null);
            //应用与服务类型绑定id
            entity.setAppPlatformChannelId(appPlatformChannelId);
            payChannelParamMapper.insert(entity);
        }
        //保存值redis
        updateCache(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());
    }

    /**
     * 根据应用和服务类型查询支付渠道参数列表
     *
     * @param appId           应用id
     * @param platformChannel 服务类型code
     * @return
     * @throws BusinessException
     */
    @Override
    public List<PayChannelParamDTO> queryPayChannelParamByAppAndPlatform(String appId, String platformChannel) throws BusinessException {
        //先从redis查询，如果有则返回
        String redisKey = RedisUtil.keyBuilder(appId, platformChannel);
        Boolean exists = cache.exists(redisKey);

        if (exists) {
            //从redis获取支付渠道参数列表（json串）
            String PayChannelParamDTO_String = cache.get(redisKey);
            //将json串转成 List<PayChannelParamDTO>
            List<PayChannelParamDTO> payChannelParamDTOS = JSON.parseArray(PayChannelParamDTO_String, PayChannelParamDTO.class);
            return payChannelParamDTOS;
        }

        //根据应用和服务类型找到它们绑定id
        Long appPlatformChannelId = selectIdByAppPlatformChannel(appId, platformChannel);
        if (null == appPlatformChannelId) {
            return null;
        }
        //应用和服务类型绑定id查询支付渠道参数记录
        List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(new LambdaQueryWrapper<PayChannelParam>().eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId));
        List<PayChannelParamDTO> payChannelParamDTOS = PayChannelParamConvert.INSTANCE.listentity2listdto(payChannelParams);
        //保存到redis
        updateCache(appId, platformChannel);
        return payChannelParamDTOS;
    }

    /**
     * 根据应用、服务类型和支付渠道的代码查询该支付渠道的参数配置信息
     *
     * @param appId
     * @param platformChannel 服务类型code
     * @param payChannel      支付渠道代码
     * @return
     * @throws BusinessException
     */
    @Override
    public PayChannelParamDTO queryParamByAppPlatformAndPayChannel(String appId, String platformChannel, String payChannel) throws BusinessException {
        //根据应用和服务类型查询支付渠道参数列表
        List<PayChannelParamDTO> payChannelParamDTOS = queryPayChannelParamByAppAndPlatform(appId, platformChannel);
        for (PayChannelParamDTO payChannelParamDTO : payChannelParamDTOS) {
            if (payChannelParamDTO.getPayChannel().equals(payChannel)) {
                return payChannelParamDTO;
            }
        }
        return null;
    }

    /**
     * 根据应用、服务类型查询应用与服务类型的绑定id
     *
     * @param appId
     * @param platformChannelCode
     * @return
     */
    private Long selectIdByAppPlatformChannel(String appId, String platformChannelCode) {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>().eq(AppPlatformChannel::getAppId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannelCode));
        if (appPlatformChannel != null) {
            return appPlatformChannel.getId();//应用与服务类型的绑定id
        }
        return null;
    }

    /**
     * 根据应用和服务类型将查询到支付渠道参数配置列表写入redis
     *
     * @param appId               应用id
     * @param platformChannelCode 服务类型code
     */
    private void updateCache(String appId, String platformChannelCode) {
        //得到redis中key(付渠道参数配置列表的key)
        //格式：SJ_PAY_PARAM:应用id:服务类型code，例如：SJ_PAY_PARAM：ebcecedd-3032-49a6-9691-4770e66577af：shanju_c2b
        String redisKey = RedisUtil.keyBuilder(appId, platformChannelCode);
        //根据key查询redis
        Boolean exists = cache.exists(redisKey);
        if (exists) {
            cache.del(redisKey);
        }
        //根据应用id和服务类型code查询支付渠道参数
        //根据应用和服务类型找到它们绑定id
        Long appPlatformChannelId = selectIdByAppPlatformChannel(appId, platformChannelCode);
        if (appPlatformChannelId != null) {
            //应用和服务类型绑定id查询支付渠道参数记录
            List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(new LambdaQueryWrapper<PayChannelParam>().eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId));
            List<PayChannelParamDTO> payChannelParamDTOS = PayChannelParamConvert.INSTANCE.listentity2listdto(payChannelParams);
            //将payChannelParamDTOS转成json串存入redis
            cache.set(redisKey, JSON.toJSON(payChannelParamDTOS).toString());
        }

    }
}

    