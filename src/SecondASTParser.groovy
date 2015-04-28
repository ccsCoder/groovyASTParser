import org.codehaus.groovy.antlr.*
import org.codehaus.groovy.antlr.parser.*
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.*

//def sourceFile = new File("C:/Users/I312054/Desktop/SampleStructuralScript.groovy")
def sourceFile = new File("C:/Users/I312054/Desktop/Person.groovy")
def reader = sourceFile.newReader()
SourceBuffer sourceBuffer = new SourceBuffer()
//UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(reader, sourceBuffer)

// Generate a GroovyRecognizer, compile an AST and assign it to 'ast'
def ast = new SourceBuffer().with { buff ->
//  new UnicodeEscapingReader( new StringReader( code ), buff ).with { read ->
  new UnicodeEscapingReader( reader, sourceBuffer ).with { read ->
    read.lexer = new GroovyLexer( read )
    GroovyRecognizer.make( read.lexer ).with { parser ->
      parser.sourceBuffer = buff
      parser.compilationUnit()
      parser.AST
    }
  }
}

// Walks the ast looking for types
def findByPath( ast, types, multiple=false ) {
  [types.take( 1 )[ 0 ],types.drop(1)].with { head, tail ->
    if( tail ) {
      findByPath( ast*.childrenOfType( head ).flatten(), tail, multiple )
    }
    else {
      ast*.childrenOfType( head ).with { ret ->
           (multiple && ret!=null) ? ret[ 0 ] : ret.head()[0]
        
      }
    }
  }
}

// Walk through the returned ast
while( ast ) {
  def methodModifier = findByPath( ast, [ MODIFIERS   ] ).firstChild?.toStringTree() ?: 'public'
  def returnType     = findByPath( ast, [ TYPE, IDENT ] ) ?: 'Object'
  def methodName     = findByPath( ast, [ IDENT       ] )
  def body           = findByPath( ast, [ SLIST ] )
  def parameters     = findByPath( ast, [ PARAMETERS, PARAMETER_DEF ], true ).collect { param ->
    [ type: findByPath( param, [ TYPE ] ).firstChild?.toStringTree() ?: 'Object',
      name: findByPath( param, [ IDENT ] ) ]
  }

 
  println '------------------------------'
  println "modifier: $methodModifier"
  println "returns:  $returnType"
  println "name:     $methodName"
  println "params:   $parameters"
//  println "$snip\n"

  // Step to next branch and repeat
  ast = ast.nextSibling
}