package com.cttic.liugw.antlr.test;

import com.cttic.liugw.antlr.gen.MathParser;
import com.cttic.liugw.antlr.gen.MathVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MathVisitorTest implements MathVisitor {
    @Override
    public Object visitProg(MathParser.ProgContext ctx) {
        return null;
    }

    @Override
    public Object visitPrintExpr(MathParser.PrintExprContext ctx) {
        return null;
    }

    @Override
    public Object visitAssign(MathParser.AssignContext ctx) {
        return null;
    }

    @Override
    public Object visitBlank(MathParser.BlankContext ctx) {
        return null;
    }

    @Override
    public Object visitParens(MathParser.ParensContext ctx) {
        return null;
    }

    @Override
    public Object visitMulDiv(MathParser.MulDivContext ctx) {
        return null;
    }

    @Override
    public Object visitAddSub(MathParser.AddSubContext ctx) {
        return null;
    }

    @Override
    public Object visitId(MathParser.IdContext ctx) {
        return null;
    }

    @Override
    public Object visitInt(MathParser.IntContext ctx) {
        return null;
    }

    @Override
    public Object visit(ParseTree parseTree) {
        return parseTree.toStringTree();
    }

    @Override
    public Object visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public Object visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public Object visitErrorNode(ErrorNode errorNode) {
        return null;
    }
}
