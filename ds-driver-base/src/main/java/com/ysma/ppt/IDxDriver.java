package com.ysma.ppt;


import com.ysma.ppt.params.DxRequestModel;
import com.ysma.ppt.params.DxResponseModel;

/**
 * @author ysma 2019-10-16 顶象自研驱动接口
 */
public interface IDxDriver {

    /**驱动编码 DX_REQUEST等*/
    String code();

    /**查询三方数据源平台*/
    DxResponseModel queryDataPlatform(DxRequestModel dxRequestModel);

}
