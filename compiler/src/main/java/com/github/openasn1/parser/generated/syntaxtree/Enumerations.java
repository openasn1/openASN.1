//
// Generated by JTB 1.3.2
//

package com.github.openasn1.parser.generated.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * rootEnumeration -> RootEnumeration()
 * nodeOptional -> [ &lt;COMMA_TKN&gt; &lt;ELLIPSIS_TKN&gt; ]
 * nodeOptional1 -> [ ExceptionSpec() ]
 * nodeOptional2 -> [ &lt;COMMA_TKN&gt; AdditionalEnumeration() ]
 * </PRE>
 */
public class Enumerations implements Node {
   private Node parent;
   public RootEnumeration rootEnumeration;
   public NodeOptional nodeOptional;
   public NodeOptional nodeOptional1;
   public NodeOptional nodeOptional2;

   public Enumerations(RootEnumeration n0, NodeOptional n1, NodeOptional n2, NodeOptional n3) {
      rootEnumeration = n0;
      if ( rootEnumeration != null ) rootEnumeration.setParent(this);
      nodeOptional = n1;
      if ( nodeOptional != null ) nodeOptional.setParent(this);
      nodeOptional1 = n2;
      if ( nodeOptional1 != null ) nodeOptional1.setParent(this);
      nodeOptional2 = n3;
      if ( nodeOptional2 != null ) nodeOptional2.setParent(this);
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
