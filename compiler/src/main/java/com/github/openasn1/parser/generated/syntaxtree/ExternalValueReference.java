//
// Generated by JTB 1.3.2
//

package com.github.openasn1.parser.generated.syntaxtree;


 
 
 
 
 
 
 
public class ExternalValueReference implements Node {
   private Node parent;
   public modulereference modulereference;
   public NodeToken nodeToken;
   public valuereference valuereference;

   public ExternalValueReference(modulereference n0, NodeToken n1, valuereference n2) {
      modulereference = n0;
      if ( modulereference != null ) modulereference.setParent(this);
      nodeToken = n1;
      if ( nodeToken != null ) nodeToken.setParent(this);
      valuereference = n2;
      if ( valuereference != null ) valuereference.setParent(this);
   }

   public ExternalValueReference(modulereference n0, valuereference n1) {
      modulereference = n0;
      if ( modulereference != null ) modulereference.setParent(this);
      nodeToken = new NodeToken(".");
      if ( nodeToken != null ) nodeToken.setParent(this);
      valuereference = n1;
      if ( valuereference != null ) valuereference.setParent(this);
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

