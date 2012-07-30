package com.zanthrash.spockextensions

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo
import org.spockframework.runtime.model.FeatureInfo

class JiraIssueExtension extends AbstractAnnotationDrivenExtension<JiraIssue> {
    @Override
    void visitSpecAnnotation(JiraIssue jiraIssue, SpecInfo specInfo) {
        specInfo.features.each {FeatureInfo feature ->
            if(!feature.getReflection().isAnnotationPresent(JiraIssue)) {
                visitFeatureAnnotation(jiraIssue, feature)
            }
        }
    }

    @Override
    void visitFeatureAnnotation(JiraIssue jiraIssue, FeatureInfo featureInfo) {
        featureInfo.getFeatureMethod().addInterceptor(new JiraIssueIntercepter(jiraIssue))
//        featureInfo.setReportIterations(true)
//        def origName = featureInfo.getName()
//        def issueName = jiraIssue.value()
//        def url = createIssueUrl(issueName)
//
//        featureInfo.setName("($issueName) $origName")
    }

    private createIssueUrl(String issueName) {
//        "<a href='http://virtuwell.net/issue?id=$issueName'>($issueName)</a>"

    }



}

