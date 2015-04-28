
import org.codehaus.groovy.antlr.GroovySourceAST
import org.codehaus.groovy.antlr.AntlrASTProcessor
import org.codehaus.groovy.antlr.SourceBuffer
import org.codehaus.groovy.antlr.UnicodeEscapingReader
import org.codehaus.groovy.antlr.parser.GroovyLexer
import org.codehaus.groovy.antlr.parser.GroovyRecognizer
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal
import org.codehaus.groovy.tools.groovydoc.SimpleGroovyClassDoc
import org.codehaus.groovy.tools.groovydoc.SimpleGroovyClassDocAssembler
import org.codehaus.groovy.tools.groovydoc.SimpleGroovyMethodDoc

import org.codehaus.groovy.groovydoc.GroovyParameter;

def sourceFile = new File("C:/Users/I312054/Desktop/Person.groovy")
//def sourceFile = new File("C:/Users/I312054/Desktop/SampleStructuralScript.groovy")
def reader = sourceFile.newReader()
SourceBuffer sourceBuffer = new SourceBuffer()
UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(reader, sourceBuffer)
GroovyLexer lexer = new GroovyLexer(unicodeReader)
unicodeReader.setLexer(lexer)
GroovyRecognizer parser = GroovyRecognizer.make(lexer)
parser.setSourceBuffer(sourceBuffer)
parser.compilationUnit()
GroovySourceAST ast = (GroovySourceAST)parser.getAST()

def visitor = new SimpleGroovyClassDocAssembler(sourceFile.getAbsolutePath(), sourceFile.getName(), sourceBuffer, [], new Properties(), true)

AntlrASTProcessor traverser = new SourceCodeTraversal(visitor)
traverser.process(ast)


visitor.visitImport(ast);
//visitor.g

//Go through all the classes.
def classDocs = (visitor.getGroovyClassDocs().values() as List);
println(classDocs.size());
//System.exit(0);
for(i=0;i<classDocs.size();i++) {
    processClass (classDocs, i);
}

    

def processClass(classDocs,index) {
    
    //    SimpleGroovyClassDoc doc = (visitor.getGroovyClassDocs().values() as List)[index]
    SimpleGroovyClassDoc doc = classDocs[index]
    println("\n "+doc.name()+" \n-------------------------------------")
    doc.methods().each {
    
//        print (" Modifiers: "+ it.modifiers()==null?"none":it.modifiers())
        if(it.isPrivate()) {
            print(" private ")
        }
        else if(it.isPublic()) {
            print(" public ")
        }
        else if(it.isProtected()) {
            print(" protected ")
        }
        else
            print(" ")
            
        print (" Return Type:"+(it.returnType()==null?"none":(it.returnType().qualifiedTypeName())))
        print (" Method Name:"+it.name())
    
        GroovyParameter [] params = it.parameters();
        params.each {
            print("[ Param Name:"+ it.name())
            print(", Param Type:"+ it.typeName())
            print(", Param Default Value:"+it.defaultValue()+"] ")
        }
        println()
    
    }
}






