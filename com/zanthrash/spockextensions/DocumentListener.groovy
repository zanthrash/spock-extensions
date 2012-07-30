package com.zanthrash.spockextensions

import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.model.SpecInfo
import org.spockframework.runtime.model.FeatureInfo

import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.IterationInfo

class DocumentListener extends AbstractRunListener{
    private Document document
    File baseDir = new File("src/docs/ref/Tests")
    File output

    DocumentListener(Document document) {
        this.document = document
        baseDir.mkdirs()
    }

    @Override
    void beforeSpec(SpecInfo specInfo) {
        output = new File(baseDir, "${specInfo.filename.replace('groovy', 'gdoc')}" )
        output.write "h1. ${specInfo.name} \n"
        output << "h3. Last Ran: ${new Date().format("MM/dd/yyyy 'at' h:mm a")}\n\n"
    }

    @Override
    void beforeFeature(FeatureInfo featureInfo) {
        if( !featureInfo.isParameterized() ) {
            writeTableHeader(featureInfo)
            addJiraIssueRow(featureInfo)
            addAuthorRow(featureInfo)
            addBlocksRow(featureInfo)
        }
    }

    @Override
    void beforeIteration(IterationInfo iterationInfo) {
        if( iterationInfo.parent.isParameterized() ) {
            writeTableHeader(iterationInfo.getParent().line, iterationInfo.name )
            addJiraIssueRow(iterationInfo.parent)
            addAuthorRow(iterationInfo.parent)
            addBlocksRow(iterationInfo.parent)
        }
    }

    @Override
    void afterIteration(IterationInfo iterationInfo) {
        if ( iterationInfo.parent.isParameterized() ) {
            writeTableFooter(iterationInfo.parent)
        }
    }

    @Override
    void afterFeature(FeatureInfo featureInfo) {
        if( !featureInfo.isParameterized() ) {
            writeTableFooter(featureInfo)
        }
    }

    @Override
    void error(ErrorInfo errorInfo) {
        output << " | ${warning("There was an error in this test")}"
    }

    @Override
    void featureSkipped(FeatureInfo featureInfo) {
        writeTableHeader(featureInfo)
        output << " | ${note("Test skipped")}"
        writeTableFooter(featureInfo)
    }

    def addBlocksRow(FeatureInfo featureInfo) {
        featureInfo.blocks.each{ block ->
            if(block.texts.size() == 0) {
                writeKeyValueRow(block.kind )
            } else if(block.texts.size() == 1) {
                writeKeyValueRow(block.kind, block.texts[0])
            } else {
                writeKeyValueRow(block.kind, block.texts[0])
                block.texts[1..-1].each { andText ->
                    writeKeyValueRow( "AND", andText)
                }
            }
        }
    }

    def writeTableHeader(FeatureInfo featureInfo) {
        output << "{table}\n"
        output << "Line:${featureInfo.featureMethod.line} | *${featureInfo.name}*  \n"
    }

    def writeTableHeader(def lineNumber, def testName) {
        output << "{table}\n"
        output << "Line:${lineNumber} | *${testName}*  \n"
    }

    def writeKeyValueRow(key, value = '') {
        output << "*$key* | $value\n"
    }

    protected void addJiraIssueRow(FeatureInfo featureInfo) {
        if (featureInfo.featureMethod.getReflection().isAnnotationPresent(JiraIssue)) {
            JiraIssue issue = featureInfo.featureMethod.getReflection().getAnnotation(JiraIssue)
            output << "*Jira Issue*| ${createUrlList(issue.value()).join(', ')}\n"
        }
    }

    Object addAuthorRow(FeatureInfo featureInfo) {
        if (featureInfo.featureMethod.getReflection().isAnnotationPresent(Author)) {
            Author author = featureInfo.featureMethod.getReflection().getAnnotation(Author)
            output << "*Author* | ${author.value()?.join(', ')}\n"
        }
    }


    def writeTableFooter(FeatureInfo featureInfo) {
        output << "{table}\n\n"
    }

    List createUrlList(String[] strings) {
        strings.collect {"[$it|http://jira.virtuwell.net/browse/$it]"}
    }


    def warning(String string) {
        "{warning}$string{warning}\n"
    }

    def note(String string) {
        "{note}$string{note}\n"
    }
}
