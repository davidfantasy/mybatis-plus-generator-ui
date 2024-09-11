package com.github.davidfantasy.mybatisplus.generatorui.common.sqlparser;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.List;

/**
 * 解析子查询以及自身的where条件部分
 */
@Slf4j
public class SelectConditionParser implements SelectVisitor, FromItemVisitor {

    private final WhereParser whereConditionParser = new com.github.davidfantasy.mybatisplus.generatorui.common.sqlparser.WhereParser();

    public List<ConditionExpr> getParsedConditions() {
        return whereConditionParser.getConditions();
    }

    @Override
    public void visit(Table tableName) {
    }

    @Override
    public void visit(ParenthesedSelect parenthesedSelect) {
        parenthesedSelect.getSetOperationList().accept(this);
    }

    @Override
    public void visit(TableFunction tableFunction) {
    }

    @Override
    public void visit(ParenthesedFromItem parenthesedFromItem) {
        parenthesedFromItem.accept(this);
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        Expression where = plainSelect.getWhere();
        if (where != null) {
            where.accept(whereConditionParser);
        }
        plainSelect.getFromItem().accept(this);
    }

    @Override
    public void visit(SetOperationList setOpList) {
        for (Select sb : setOpList.getSelects()) {
            sb.accept(this);
        }
    }

    @Override
    public void visit(WithItem withItem) {
    }

    @Override
    public void visit(Values values) {
    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
        lateralSubSelect.accept((SelectVisitor) this);
    }

    @Override
    public void visit(TableStatement tableStatement) {
    }
}
