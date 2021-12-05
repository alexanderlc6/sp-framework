package com.sp.framework.common.utils;

import org.hibernate.validator.HibernateValidator;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

public class BeanValidators {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Map<String, String> validate(Validator validator, T object, Class<?>... groups) {
		Set constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			return extractPropertyAndMessage(constraintViolations);
		} else {
			return null;
		}
	}

	public static <T> List<Map<String, String>> validate(Validator validator, List<T> objects, Class<?>... groups) {
		if (objects != null && !objects.isEmpty()) {
			List<Map<String, String>> validateResult = new ArrayList<Map<String, String>>();
			for (T object : objects) {
				Map<String, String> validateMap = validate(validator, object, groups);
				if (validateMap != null) {
					validateResult.add(validateMap);
				}
			}
			return validateResult;
		} else {
			return null;
		}
	}

	private static Validator validator = Validation
			.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> String validate(T object, Class<?>... groups) {
		Set constraintViolations = validator.validate(object, groups);

		if (!constraintViolations.isEmpty()) {
			StringBuilder stb = new StringBuilder();
			Map<String, String> constraintAttrs = extractPropertyAndMessage(constraintViolations);
			if (constraintAttrs != null && !constraintAttrs.isEmpty()) {
				for (String errMsg : constraintAttrs.values()){
					stb.append(errMsg);
				}
			}

			return stb.toString();
		} else {
			return null;
		}
	}

	public static <T> List<String> validate(List<T> objects, Class<?>... groups) {
		if (objects != null && !objects.isEmpty()) {
			List<String> validateResult = new ArrayList();
			for (T object : objects) {
				Set constraintViolations = validator.validate(object, groups);
				if (!constraintViolations.isEmpty()) {
					StringBuilder stb = new StringBuilder();
					Map<String, String> validateMap = extractPropertyAndMessage(constraintViolations);

					if (!CollectionUtils.isEmpty(validateMap)) {
						for (String errMsg : validateMap.values()){
							stb.append(errMsg);
						}
						validateResult.add(stb.toString());
					}
				}
			}
			return validateResult;
		} else {
			return null;
		}
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为Map<property, message>.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations) {
		Map<String, String> errorMessages = new HashMap<String, String>();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return errorMessages;
	}
}
