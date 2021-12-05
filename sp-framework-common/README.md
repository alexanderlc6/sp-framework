# yh-infra-common-lib

#### 介绍
SuperSpace公共库工程,用于规范和统一所有项目工程基础功能的调用源。

#### 软件架构
包括了常用的软件工具类和中间件的客户端操作

#### 使用说明
> 1.包依赖说明

直接通过POM依赖:
~~~
        <dependency>
            <groupId>com.sp.framework.common</groupId>
            <artifactId>yh-infra-common-lib</artifactId>
            <version>1.0.0-RELEASE</version>
        </dependency>
~~~

---

> 2.API定义规约

` ` **2.1 Controller类规约** 
- 自定义的Controller类可以继承com.sp.framework.common.base.BaseController
  
` ` **2.2 接口定义POJO对象规约**
    
` ` 2.2.1 请求入参对象定义
  - 若API接口是给前端系统发起调用的，建议入参对象定义为VO，可以继承com.sp.framework.common.vo.BaseRequestVO
  - 若API接口是给其它服务发起调用的，建议入参对象定义为DTO，可以继承BaseRequestDTO
  - (均适用)需要分页的请求对象继承com.sp.framework.common.dto.PagerBaseRequestDTO、响应对象继承com.sp.framework.common.dto.PagerBaseResponseDTO<T>
  - (均适用)需要排序的请求对象继承com.sp.framework.common.dto.SortPropertyInfoDTO,其中sortTagName、sortPropertyName、sortField是在PagerBaseResponseDTO<T>返回的排序属性集合List<SortPropertyInfoDTO> sortPropertyInfoList告知前端开发人员的，
    前端开发人员根据每个前端页面的实际情况传需要排序的属性。
    
` ` 2.2.2 响应返回对象定义
- 一般情况下，响应返回使用com.sp.framework.common.vo.ResponseVO即可，特殊需求的情况可自行定义。

` ` **2.3 示例代码**
~~~
public TestController extends com.sp.framework.common.base.BaseController {
	@RequestMapping("/getInfo")
	public ResponseVO getInfo(@RequestParam(value = "alarmId")  String id, @RequestParam(value = "version")  long version) {
		return getFromData(alarmService.getInfo(id, version));
	}
}
~~~