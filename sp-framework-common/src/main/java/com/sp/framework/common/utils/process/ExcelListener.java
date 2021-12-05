package com.sp.framework.common.utils.process;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * [在此注释说明]
 *
 * @author Alex Lu
 * @date 2019/12/13
 */
@Getter
@Setter
public class ExcelListener extends AnalysisEventListener {

    private List<Object> processDataList = new ArrayList();

    /**
     * 逐行解析
     * @param object 当前行数据
     * @param context
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        if(object != null){
            processDataList.add(object);
        }
    }

    /**
     * 解析完所有数据后该方法被调用
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //解析结束销毁不用的资源
    }
}
