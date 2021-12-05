package com.sp.framework.orm.lark.tool.propertyutil;

import java.util.Properties;


import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * {@link NamespaceHandler} for the {@code util} namespace.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public class UtilNamespaceHandler extends NamespaceHandlerSupport {

	private static final String SCOPE_ATTRIBUTE = "scope";


	public void init() {

		registerBeanDefinitionParser("properties", new PropertiesBeanDefinitionParser());
	}

	


	private static class PropertiesBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

		@Override
		protected Class getBeanClass(Element element) {
			return PropertiesFactoryBean.class;
		}

		@Override
		protected boolean isEligibleAttribute(String attributeName) {
			return super.isEligibleAttribute(attributeName) && !SCOPE_ATTRIBUTE.equals(attributeName);
		}

		@Override
		protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
			super.doParse(element, parserContext, builder);
			Properties parsedProps = parserContext.getDelegate().parsePropsElement(element);
			builder.addPropertyValue("properties", parsedProps);
			String scope = element.getAttribute(SCOPE_ATTRIBUTE);
			if (StringUtils.hasLength(scope)) {
				builder.setScope(scope);
			}
		}
	}

}
