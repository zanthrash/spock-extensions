package com.zanthrash.spockextensions

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension

import org.spockframework.runtime.model.SpecInfo

class DocumentExtension extends AbstractAnnotationDrivenExtension<Document> {
    @Override
    void visitSpecAnnotation(Document document, SpecInfo specInfo) {
        specInfo.addListener(new DocumentListener(document))
    }

}

