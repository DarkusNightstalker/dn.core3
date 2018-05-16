/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dn.core3.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jhoan Brayam
 * @param <S>
 */
public abstract class Pagination<T> implements Serializable {

    private final static List<Object[]> ROWS_DATA;

    static {
        ROWS_DATA = new ArrayList();
        ROWS_DATA.add(new Object[]{10, "10"});
        ROWS_DATA.add(new Object[]{15, "15"});
        ROWS_DATA.add(new Object[]{20, "20"});
        ROWS_DATA.add(new Object[]{30, "30"});
        ROWS_DATA.add(new Object[]{50, "50"});
        ROWS_DATA.add(new Object[]{100, "100"});
        ROWS_DATA.add(new Object[]{-1, "Todos"});
    }

    private Integer page;
    private Integer rows;
    private Integer lastPage;
    private String messageTemplate;
    private Long totalRecords;
    public List<T> data;

    public Pagination() {
        this("Mostrando <span class='txt-color-darken'>{START}</span> a <span class='txt-color-darken'>{END}</span> de <span class='text-primary'>{TR}</span> registros");
    }

    public Pagination(String messageTemplate) {
        this.messageTemplate = messageTemplate;
        rows = (Integer) ROWS_DATA.get(0)[0];
    }

    public String message() {
        try {
            String message = messageTemplate
                    .replace("{TR}", totalRecords.toString())
                    .replace("{START}", getRecordStart().toString())
                    .replace("{END}", getRecordEnd().toString());
            return message;
        } catch (Exception e) {
            return "...";
        }
    }

    public List getRowsData() {
        return ROWS_DATA;
    }

    public Integer getRecordStart() {
        try {
            int currentRows = rows < 0 ? totalRecords.intValue() : rows;
            return ((page - 1) * currentRows) + 1;
        } catch (Exception e) {
            return null;
        }
    }

    public void first() {
        search(1);
    }

    public void last() {
        search(lastPage);
    }

    public void next() {
        search(page + 1);
    }

    public void previous() {
        search(page - 1);
    }

    public void changeRows() {
        search(1);
    }

    public Integer getRecordEnd() {
        try {
            int currentRows = rows < 0 ? totalRecords.intValue() : rows;
            int v = (page * currentRows);

            return v > totalRecords ? totalRecords.intValue() : v;
        } catch (Exception e) {
            return null;
        }
    }

    public void search(int page) {
        totalRecords = null;
        try {
            totalRecords = countTotalRecords();
        } catch (Exception e) {
            totalRecords = 0L;
        }
        if (totalRecords != 0) {
            int currentRows = rows < 0 ? totalRecords.intValue() : rows;
            this.lastPage = totalRecords.intValue() / currentRows;
            if (totalRecords.intValue() % currentRows != 0) {
                this.lastPage++;
            }
            if (page > lastPage) {
                page = lastPage;
            }
            this.page = page;
            data = searchRecords(page, currentRows);
        } else {
            this.page = null;
            this.lastPage = null;
            data = Collections.EMPTY_LIST;
        }
    }

    protected abstract Long countTotalRecords();

    protected abstract List searchRecords(int page, int recordsPerPage);

    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return the rows
     */
    public Integer getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    /**
     * @param messageTemplate the messageTemplate to set
     */
    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    /**
     * @return the totalRecords
     */
    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * @return the data
     */
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        if (data != null) {
            this.totalRecords = Long.parseLong(String.valueOf(data.size()));
        }
    }

    /**
     * @return the lastPage
     */
    public Integer getLastPage() {
        return lastPage;
    }

}
