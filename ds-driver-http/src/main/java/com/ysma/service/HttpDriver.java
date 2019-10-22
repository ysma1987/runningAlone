package com.ysma.service;


import com.ysma.constant.NameListConstant;
import com.ysma.ppt.IDxDriver;
import com.ysma.ppt.params.DxRequestModel;
import com.ysma.ppt.params.DxResponseModel;
import com.ysma.requestfactory.CustomRequestFactory;

/**
 * @author ysma 2019-10-21 http驱动包
 */
public class HttpDriver implements IDxDriver {

    private static final String CODE = "HTTP_REQUEST";

    @Override
    public String code() {
        return CODE;
    }

    @Override
    public DxResponseModel queryDataPlatform(DxRequestModel dxRequestModel) {
        DxResponseModel dxResponseModel = new DxResponseModel();

        //1.URL不能为空
        if (dxRequestModel.getApiUrl() == null) {
            dxResponseModel.setResCode(NameListConstant.ARGS_ERROR_CODE);
            dxResponseModel.setResMsg(NameListConstant.ARGS_ERROR_MSG);
            return dxResponseModel;
        }

        //2.HTTP请求
        CustomRequestFactory customRequestFactory = new CustomRequestFactory();

        return customRequestFactory.customRequestExcutor(dxRequestModel);
    }
}
