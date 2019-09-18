package cn.ieclipse.common;

/**
 * @author Jamling
 * 
 */
public class Pagination {

    private int page;
    private int pageSize = 10;
    private int pageNum;
    private int total;
    private int offset;
    private int end;

    /**
     * New pagination
     * 
     * @param pageSize
     *            size per page
     * @param total
     *            total number
     * @throws IllegalArgumentException
     *             if size or total is negative
     */
    public Pagination(int pageSize, int total) {
        if (total < 0) {
            throw new IllegalArgumentException("total number can't be negative");
        }
        if (pageSize < 0) {
            throw new IllegalArgumentException("page size can't be negative");
        }
        this.pageSize = pageSize;
        this.total = total;
        init();
    }

    /**
     * New pagination.
     */
    public Pagination() {

    }

    /**
     * Init page number and current page
     */
    private void init() {
        int mod = total % pageSize;
        int page = total / pageSize;
        if (mod > 0) {
            page = page + 1;
        }
        this.pageNum = page;

        if (this.page > 0 && this.page >= this.pageNum) {
            setPage(this.pageNum - 1);
        }
    }

    /**
     * Set total number, and the current page might be changed
     * 
     * @param total
     *            total number
     */
    public void setTotal(int total) {
        if (total < 0) {
            throw new IllegalArgumentException("total number can't be negative");
        }
        this.total = total;
        init();
    }

    /**
     * Set page size, and the current page might be changed
     * 
     * @param pageSize
     *            size per page
     */
    public void setPageSize(int pageSize) {
        if (pageSize < 0) {
            throw new IllegalArgumentException("page size can't be negative");
        }
        this.pageSize = pageSize;
        init();
    }

    /**
     * Set current page
     * 
     * @param page
     *            current page number
     * @throws IndexOutOfBoundsException
     *             if page out of range.
     */
    public void setPage(int page) {
        if (page >= 0 && page < pageNum) {
            this.page = page;
            pageChanged();
        } else {
            throw new IndexOutOfBoundsException(String.format("page(%d) out of range[0,%d)", page, pageNum));
        }
    }

    private void pageChanged() {
        this.offset = pageSize * this.page;
        this.end = Math.min(this.offset + pageSize, total);
        this.end = this.end - 1;
    }

    /**
     * Get current page index. start index is 0;
     * 
     * @return current page
     */
    public int getPage() {
        return page;
    }

    /**
     * Get total page number.
     * 
     * @return page number
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * Whether has next page or not
     * 
     * @return true if has next page
     */
    public boolean hasNextPage() {
        return page < pageNum - 1;
    }

    /**
     * Whether has previous page or not
     * 
     * @return true if has previous page
     */
    public boolean hasPrevPage() {
        return page > 0;
    }

    /**
     * Get current page offset.
     * 
     * @return current page start offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Get current page end index.
     * 
     * @return current page end index.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Get current page valid count, useful in SQL
     * 
     * 
     * @return valid count of current page.
     */
    public int getLimit() {
        return end - offset + 1;
    }

    public void dump() {
        System.out.println(String.format("page:%d/%d; offset:%d-%d limit %d", getPage() + 1, getPageNum(), getOffset(),
            getEnd(), getLimit()));
    }

    public static void main(String[] args) {
        Pagination p = new Pagination(10, 40);
        p.setPage(3);
        p.dump();
        p.setTotal(50);
        p.dump();
        p.setPageSize(50);
        p.dump();
    }
}
