//
// Generated by JTB 1.3.2
//

package com.github.openasn1.parser.generated.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;LEFT_PARENTHESIS_TKN&gt;
 * constraintSpec -> ConstraintSpec()
 * nodeOptional -> [ ExceptionSpec() ]
 * nodeToken1 -> &lt;RIGHT_PARENTHESIS_TKN&gt;
 * </PRE>
 */
public class Constraint implements Node {
   private Node parent;
   public NodeToken nodeToken;
   public ConstraintSpec constraintSpec;
   public NodeOptional nodeOptional;
   public NodeToken nodeToken1;

   public Constraint(NodeToken n0, ConstraintSpec n1, NodeOptional n2, NodeToken n3) {
      nodeToken = n0;
      if ( nodeToken != null ) nodeToken.setParent(this);
      constraintSpec = n1;
      if ( constraintSpec != null ) constraintSpec.setParent(this);
      nodeOptional = n2;
      if ( nodeOptional != null ) nodeOptional.setParent(this);
      nodeToken1 = n3;
      if ( nodeToken1 != null ) nodeToken1.setParent(this);
   }

   public Constraint(ConstraintSpec n0, NodeOptional n1) {
      nodeToken = new NodeToken("(");
      if ( nodeToken != null ) nodeToken.setParent(this);
      constraintSpec = n0;
      if ( constraintSpec != null ) constraintSpec.setParent(this);
      nodeOptional = n1;
      if ( nodeOptional != null ) nodeOptional.setParent(this);
      nodeToken1 = new NodeToken(")");
      if ( nodeToken1 != null ) nodeToken1.setParent(this);
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
