package com.github.davidfantasy.mybatisplus.generatorui.sqlparser;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.List;

/**
 * 用于遍历解析SQL中的where条件，并封装成可匹配的正则表达式
 */
public class WhereParser extends ExpressionVisitorAdapter {


    private String currentLogicOp;

    /**
     * 所有解析到的符合规则的where条件，用于进一步匹配
     */
    private List<ConditionExpr> conditions = Lists.newArrayList();

    public List<ConditionExpr> getConditions() {
        return conditions;
    }

    @Override
    public void visit(AndExpression expr) {

        parseLogicOperator(expr, "AND");
    }

    @Override
    public void visit(OrExpression expr) {
        parseLogicOperator(expr, "OR");
    }

    @Override
    public void visit(Between expr) {
        parseBetweenOperator(expr);
    }

    @Override
    public void visit(GreaterThan expr) {
        parseComparisonOperator(expr);
    }

    @Override
    public void visit(GreaterThanEquals expr) {
        parseComparisonOperator(expr);
    }

    @Override
    public void visit(MinorThan expr) {
        parseComparisonOperator(expr);
    }

    @Override
    public void visit(MinorThanEquals expr) {
        parseComparisonOperator(expr);
    }

    @Override
    public void visit(LikeExpression expr) {
        parseCommonOperator(expr.getLeftExpression(), expr.getRightExpression(), "like");
    }

    @Override
    public void visit(NotEqualsTo expr) {
        parseComparisonOperator(expr);
    }

    @Override
    public void visit(EqualsTo expr) {
        parseComparisonOperator(expr);
    }

    @Override
    public void visit(InExpression expr) {
        if (expr.getRightExpression() != null) {
            parseCommonOperator(expr.getLeftExpression(), expr.getRightExpression(), "in");
        } else {
            currentLogicOp = "";
            expr.getRightItemsList().accept(this);
        }
    }

    @Override
    public void visit(SubSelect subSelect) {
        PlainSelect ps = (PlainSelect) subSelect.getSelectBody();
        ps.getWhere().accept(this);
    }

    private void parseLogicOperator(BinaryExpression binaryExpression, String operator) {
        binaryExpression.getLeftExpression().accept(this);
        currentLogicOp = operator;
        binaryExpression.getRightExpression().accept(this);
    }

    private void parseComparisonOperator(ComparisonOperator operator) {
        this.parseCommonOperator(operator.getLeftExpression(), operator.getRightExpression(), operator.getStringExpression());
    }

    private void parseBetweenOperator(Between between) {
        if (ConditionExpr.isDynamicParam(between.getBetweenExpressionStart().toString())
                || ConditionExpr.isDynamicParam(between.getBetweenExpressionEnd().toString())) {
            ConditionExpr condition = new ConditionExpr();
            condition.setLogicOperator(currentLogicOp);
            condition.setLeftExpr(between.getLeftExpression().toString());
            condition.setOperator("between");
            condition.setRightExpr(between.getBetweenExpressionStart().toString());
            condition.setMiddleOperator("and");
            condition.setEndExpr(between.getBetweenExpressionEnd().toString());
            condition.parseDynamicParams(between.getBetweenExpressionStart().toString());
            condition.parseDynamicParams(between.getBetweenExpressionEnd().toString());
            conditions.add(condition);
        }
        currentLogicOp = "";
    }

    private void parseCommonOperator(Expression left, Expression right, String operator) {
        if (left == null || right == null) {
            currentLogicOp = "";
            return;
        }
        if (ConditionExpr.isDynamicParam(right.toString())) {
            ConditionExpr condition = new ConditionExpr();
            condition.setLeftExpr(left.toString());
            condition.setOperator(operator);
            condition.setRightExpr(right.toString());
            condition.setLogicOperator(currentLogicOp);
            condition.parseDynamicParams(right.toString());
            conditions.add(condition);
        }
        currentLogicOp = "";
    }

}
