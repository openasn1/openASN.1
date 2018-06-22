//
// Generated by JTB 1.3.2
//

package com.github.openasn1.parser.generated.syntaxtree;


 
 
public class NodeChoice implements Node {
   public NodeChoice(Node node) {
      this(node, -1);
   }

   public NodeChoice(Node node, int whichChoice) {
      choice = node;
      choice.setParent(this);
      which = whichChoice;
   }

   public void accept(com.github.openasn1.parser.generated.visitor.Visitor v) {
      choice.accept(v);
   }
   public <R,A> R accept(com.github.openasn1.parser.generated.visitor.GJVisitor<R,A> v, A argu) {
      return choice.accept(v,argu);
   }
   public <R> R accept(com.github.openasn1.parser.generated.visitor.GJNoArguVisitor<R> v) {
      return choice.accept(v);
   }
   public <A> void accept(com.github.openasn1.parser.generated.visitor.GJVoidVisitor<A> v, A argu) {
      choice.accept(v,argu);
   }

   public void setParent(Node n) { parent = n; }
   public Node getParent()       { return parent; }

   private Node parent;
   public Node choice;
   public int which;
}

