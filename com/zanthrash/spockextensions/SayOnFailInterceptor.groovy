package com.zanthrash.spockextensions

import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.extension.IMethodInterceptor
import org.spockframework.runtime.model.FeatureInfo

class SayOnFailInterceptor implements IMethodInterceptor{

    SayOnFail sayOnError
    FeatureInfo featureInfo

    SayOnFailInterceptor(SayOnFail sayOnError, FeatureInfo featureInfo) {
        this.sayOnError = sayOnError
        this.featureInfo = featureInfo
    }

    void intercept(IMethodInvocation invocation) {
        try {
           invocation.proceed()
        } catch(Throwable t) {
            def methodName = featureInfo.getFeatureMethod().name
            def voiceName = sayOnError.voice()
            def sayText = sayOnError.value() ?: "Danger! Failure for: $methodName"
            try {
                "say -v $voiceName $sayText".execute()
            } catch (IOException ex) {}

            throw t
        }
    }
}
