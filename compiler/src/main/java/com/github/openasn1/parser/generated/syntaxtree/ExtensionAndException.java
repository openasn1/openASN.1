//
// Generated by JTB 1.3.2
//

package com.github.openasn1.parser.generated.syntaxtree;


 
 
 
 
 
 
public class ExtensionAndException implements Node {
   private Node parent;
   public NodeToken nodeToken;
   public NodeOptional nodeOptional;

   public ExtensionAndException(NodeToken n0, NodeOptional n1) {
      nodeToken = n0;
      if ( nodeToken != null ) nodeToken.setParent(this);
      nodeOptional = n1;
      if ( nodeOptional != null ) nodeOptional.setParent(this);
   }

   public ExtensionAndException(NodeOptional n0) {
      nodeToken = new NodeToken("...");
      if ( nodeToken != null ) nodeToken.setParent(this);
      nodeOptional = n0;
      if ( nodeOptional != null ) nodeOptional.setParent(this);
   }

   public void accept(com.github.openasn1.parser.generated.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(com.github.openasn1.parser.generated.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(com.github.openasn1.parser.generated.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(com.github.openasn1.parser.generated.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
   public void setParent(Node n) { parent = n; }
   public Node getParent()       { return parent; }
}

