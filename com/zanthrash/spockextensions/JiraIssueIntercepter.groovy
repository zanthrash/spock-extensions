package com.zanthrash.spockextensions

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.BlockInfo

class JiraIssueIntercepter extends AbstractMethodInterceptor{

    File baseDir = new File("src/docs/guide")
    File output
    private JiraIssue jiraIssue

    JiraIssueIntercepter(JiraIssue jiraIssue) {
        this.jiraIssue = jiraIssue
        baseDir.mkdirs()
        output = new File(baseDir, "specifications.gdoc")
        output.write("")
    }

    @Override
    void interceptFeatureMethod(IMethodInvocation methodInvocation) {
        FeatureInfo feature = methodInvocation.feature
        String featureName = feature.name
        output << "(${jiraIssue.value()}) Name: ${featureName}\n"
        feature.getBlocks().each {BlockInfo block ->
            output << "${block.kind}: ${block.texts[0]}\n"
        }
        output << "\n\n"
    }
}
