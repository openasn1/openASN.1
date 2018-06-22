//
// Generated by JTB 1.3.2
//

package com.github.openasn1.parser.generated.syntaxtree;


 
 
public interface Node extends java.io.Serializable {
   public void accept(com.github.openasn1.parser.generated.visitor.Visitor v);
   public <R,A> R accept(com.github.openasn1.parser.generated.visitor.GJVisitor<R,A> v, A argu);
   public <R> R accept(com.github.openasn1.parser.generated.visitor.GJNoArguVisitor<R> v);
   public <A> void accept(com.github.openasn1.parser.generated.visitor.GJVoidVisitor<A> v, A argu);
   // It is the responsibility of each implementing class to call
   // setParent() on each of its child Nodes.
   public void setParent(Node n);
   public Node getParent();
}

