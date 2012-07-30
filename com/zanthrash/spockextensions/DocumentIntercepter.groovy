package com.zanthrash.spockextensions

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.BlockInfo
import org.spockframework.runtime.model.FeatureInfo

class DocumentIntercepter extends AbstractMethodInterceptor{

    File baseDir = new File("src/docs/guide")
    File output
    private Document document

    DocumentIntercepter(Document document) {
        this.document= document
        baseDir.mkdirs()
        output = new File(baseDir, "specifications.gdoc")
        output.write("")
    }

    @Override
    void interceptSpecExecution(IMethodInvocation iMethodInvocation) {
        output << "spec execution"
    }

    @Override
    void interceptSetupMethod(IMethodInvocation iMethodInvocation) {
        output << "method setup"
    }

    @Override
    void interceptInitializerMethod(IMethodInvocation iMethodInvocation) {
        output << "initializer method"
    }

    @Override
    void interceptSharedInitializerMethod(IMethodInvocation iMethodInvocation) {
        output << "shared initializer method 8"
    }

    @Override
    void interceptCleanupMethod(IMethodInvocation iMethodInvocation) {
        output << "shared initializer method 7"
    }

    @Override
    void interceptCleanupSpecMethod(IMethodInvocation iMethodInvocation) {
        output << "shared initializer method 6"
    }

    @Override
    void interceptDataProcessorMethod(IMethodInvocation iMethodInvocation) {
        output << "shared initializer method 5"
    }

    @Override
    void interceptDataProviderMethod(IMethodInvocation iMethodInvocation) {
        output << "shared initializer method 4"
    }

    @Override
    void interceptFeatureExecution(IMethodInvocation iMethodInvocation) {
        output << "shared initializer method 3"
    }

    @Override
    void interceptIterationExecution(IMethodInvocation iMethodInvocation) {
        output << "shared initializer method 2"
    }

    @Override
    void interceptSetupSpecMethod(IMethodInvocation iMethodInvocation) {
        output << "shared initializer method 1"
    }

    @Override
    void interceptFeatureMethod(IMethodInvocation methodInvocation) {
        output << methodInvocation.getSpec().filename
        FeatureInfo feature = methodInvocation.feature
        String featureName = feature.name
        output << "Name: ${featureName}\n"
        feature.getBlocks().each {BlockInfo block ->
            output << "${block.kind}: ${block.texts[0]}\n"
        }
        output << "\n\n"

        methodInvocation.proceed()
    }
}
