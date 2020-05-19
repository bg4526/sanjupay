package com.shanjupay.merchant.controller;

import com.shanjupay.merchant.common.util.SecurityUtil;
import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.api.dto.PayChannelDTO;
import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.api.dto.PlatformChannelDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname PlatformParamController
 * @Description 户平台-渠道和支付参数相关
 * @Date 2020/5/11 23:37
 * @Created by 小二哥
 */
@Api(value = "商户平台-渠道和支付参数相关", tags = "商户平台-渠道和支付参数", description = "商户平台-渠道和支付参数相关")
@Slf4j
@RestController
public class PlatformParamController {
    @Reference
    private PayChannelService payChannelService;

    @ApiOperation("获取平台服务类型")
    @GetMapping(value = "/my/platform-channels")
    public List<PlatformChannelDTO> queryPlatformChannel() {
        return payChannelService.queryPlatformChannel();
    }

    @ApiOperation("绑定服务类型")
    @PostMapping(value = "/my/apps/{appId}/platform-channels")
    @ApiImplicitParams({@ApiImplicitParam(value = "应用id", name = "appId", dataType = "string", paramType = "path"), @ApiImplicitParam(value = "服务类型code", name = "platformChannelCodes", dataType = "string", paramType = "query")})
    public void bindPlatformForApp(@PathVariable("appId") String appId,
                                   @RequestParam("platformChannelCodes") String platformChannelCodes) {
        payChannelService.bindPlatformChannelForApp(appId, platformChannelCodes);
    }

    @ApiOperation("查询应用是否绑定了某个服务类型")
    @ApiImplicitParams({@ApiImplicitParam(name = "appId", value = "应用appId", required = true, dataType = "String", paramType = "query"), @ApiImplicitParam(name = "platformChannel", value = "服务类型", required = true, dataType = "String", paramType = "query")})
    @GetMapping("/my/merchants/apps/platformchannels")
    public int queryAppBindPlatformChannel(@RequestParam String appId, @RequestParam String platformChannel) {
        return payChannelService.queryAppBindPlatformChannel(appId, platformChannel);
    }

    @ApiOperation("根据平台服务类型获取支付渠道列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "platformChannelCode", value = "服务类型编码", required = true, dataType = "String", paramType = "path")})
    @GetMapping(value = "/my/pay-channels/platform-channel/{platformChannelCode}")
    public List<PayChannelDTO> queryPayChannelByPlatformChannel(@PathVariable String platformChannelCode) {
        return payChannelService.queryPayChannelByPlatformChannel(platformChannelCode);
    }

    @ApiOperation("商户配置支付渠道参数")
    @ApiImplicitParams({@ApiImplicitParam(name = "payChannelParam", value = "商户配置支付渠道参数", required = true, dataType = "PayChannelParamDTO", paramType = "body")})
    @RequestMapping(value = "/my/pay-channel-params", method = {RequestMethod.POST, RequestMethod.PUT})
    public void createPayChannelParam(@RequestBody PayChannelParamDTO payChannelParam) {
        Long merchantId = SecurityUtil.getMerchantId();
        payChannelParam.setMerchantId(merchantId);
        payChannelService.savePayChannelParam(payChannelParam);
    }

    @ApiOperation("根据应用和服务类型获取支付渠道参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用id", name = "appId", dataType = "String", paramType = "path"),
            @ApiImplicitParam(value = "服务类型代码", name = "platformChannel", dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/my/pay-channel-params/apps/{appId}/platform-channels/{platformChannel}")
    public List<PayChannelParamDTO> queryPayChannelParam(@PathVariable("appId") String appId, @PathVariable("platformChannel") String platformChannel) {
        return payChannelService.queryPayChannelParamByAppAndPlatform(appId, platformChannel);
    }

    @ApiOperation("根据应用和服务类型和支付渠道获取单个支付渠道参数")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用id", name = "appId", dataType = "String", paramType = "path"),
            @ApiImplicitParam(value = "服务类型代码", name = "platformChannel", dataType = "String", paramType = "path"),
            @ApiImplicitParam(value = "支付渠道代码", name = "payChannel", dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/my/pay-channel-params/apps/{appId}/platform-channels/{platformChannel}/pay-channels/{payChannel}")
    public PayChannelParamDTO queryPayChannelParam(@PathVariable("appId") String appId, @PathVariable("platformChannel") String platformChannel, @PathVariable("payChannel") String payChannel) {
        return payChannelService.queryParamByAppPlatformAndPayChannel(appId, platformChannel, payChannel);
    }
}