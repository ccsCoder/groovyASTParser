package groovyastparser;

import groovyjarjarantlr.RecognitionException;
import groovyjarjarantlr.TokenStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;

public class Parser {

    public static void main(String[] args) throws FileNotFoundException, RecognitionException, TokenStreamException {

// TODO Auto-generated method stub
        Reader reader = new InputStreamReader(new FileInputStream("C:/Users/I312054/Desktop/Person.groovy"));
//        Reader reader = new InputStreamReader(new FileInputStream("C:/Users/I312054/Desktop/SampleStructuralScript.groovy"));
        SourceBuffer buff = new SourceBuffer();
        UnicodeEscapingReader uer  = new UnicodeEscapingReader(reader, buff);

        GroovyLexer lexer  = new GroovyLexer(uer);
        uer.setLexer(lexer);

        GroovyRecognizer parser = GroovyRecognizer.make(lexer);
        parser.setSourceBuffer(buff);

        parser.compilationUnit();
        GroovySourceAST parentAST = (GroovySourceAST) parser.getAST();

        while (parentAST != null) {
            System.out.println("----------------------------");

// modifier
            GroovySourceAST modifierSubAST = findByPath(parentAST, GroovyTokenTypes.MODIFIERS);
            if(modifierSubAST!=null) {
                String modifier = modifierSubAST.getFirstChild()== null ? "public" : modifierSubAST.getFirstChild().toStringTree();
                System.out.println("Modifier: " + modifier);
            }

// method name
            GroovySourceAST methodNameSubAST = findByPath(parentAST, GroovyTokenTypes.IDENT);
            System.out.println("Method Name: " + methodNameSubAST);

// return type
            GroovySourceAST returnTypeSubAST = findByPath(parentAST, GroovyTokenTypes.TYPE);
            if(returnTypeSubAST!=null) {
                String returnType = returnTypeSubAST.getFirstChild()== null ? "Object" : returnTypeSubAST.getFirstChild().toStringTree();
                System.out.println("Return type: " + returnType);
            }
            

//parameters
            GroovySourceAST paramsSubAST = findByPath(parentAST, GroovyTokenTypes.PARAMETERS);
            
            if(paramsSubAST!=null) {
                GroovySourceAST paramDefSubAST = (GroovySourceAST) paramsSubAST.getFirstChild();

                while (paramDefSubAST != null) {

                    GroovySourceAST paramTypeSubAST = findByPath(paramDefSubAST, GroovyTokenTypes.TYPE);
                    String parameter_type = paramTypeSubAST.getFirstChild()== null ? "Object" : paramTypeSubAST.getFirstChild().toStringTree();

                    GroovySourceAST paramNameSubAST = findByPath(paramDefSubAST, GroovyTokenTypes.IDENT);

                    System.out.println("Params: " + "[" + "type: " + parameter_type + ", name: " + paramNameSubAST + " ]");

                    paramDefSubAST = (GroovySourceAST) paramDefSubAST.getNextSibling();

                }
            }

            parentAST = (GroovySourceAST) parentAST.getNextSibling();

        }

    }

    private static GroovySourceAST findByPath(GroovySourceAST groovyAST,
            int type) {
        List<GroovySourceAST> subAST=groovyAST.childrenOfType(type);
        //System.out.println("TYPE:"+type);
        //System.out.println(subAST==null);
        return (subAST==null || subAST.isEmpty())?null:subAST.get(0);
    }
}
