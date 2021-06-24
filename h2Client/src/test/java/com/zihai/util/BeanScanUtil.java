package com.zihai.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanScanUtil {
    public static List<Class> scanClass(String classPath, Class<? extends Annotation> annotation) {
        List<Class> classList = new ArrayList<>();
        if (ObjectUtils.isEmpty(classPath)) {
            return classList;
        }
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        TypeFilter includeFilter = (metadataReader, metadataReaderFactory) -> true;
        provider.addIncludeFilter(includeFilter);
        Set<BeanDefinition> beanDefinitionSet = new HashSet<>();
        // 指定扫描的包名
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(classPath);
        beanDefinitionSet.addAll(candidateComponents);

        beanDefinitionSet.forEach(beanDefinition -> {
            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinition;
            try {
                Class clazz = Class.forName(beanDefinition.getBeanClassName());

                if (!ObjectUtils.isEmpty(annotation)) {
                    if (!ObjectUtils.isEmpty(AnnotationUtils.getAnnotation(clazz, annotation))) {
                        classList.add(clazz);
                    }
                } else {
                    classList.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return classList;
    }
}
