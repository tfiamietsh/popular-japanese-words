package mecha;

import java.util.ArrayList;

public class Paginator<Value> {
    public Paginator() {
        this.page = this.pagesNum = 1;
        this.elemsPerPageNum = this.valuesNum = 120;
        this.pages = new ArrayList<>();
    }

    public void addAll(ArrayList<Value> values) {
        this.valuesNum = values.size();
        this.pages.clear();
        this.page = 1;
        this.pagesNum = Math.ceilDivExact(this.valuesNum, this.elemsPerPageNum);
        for (int i = 0; i < this.pagesNum; i++) {
            int offset = i * this.elemsPerPageNum;
            this.pages.add(new ArrayList<>(values.subList(offset,
                    Math.min(offset + this.elemsPerPageNum, this.valuesNum))));
        }
    }

    public ArrayList<Value> getCurrentPage() {
        if (this.page <= this.pagesNum)
            return this.pages.get(this.page - 1);
        return null;
    }

    public void nextPage() {
        this.page = Math.min(this.page + 1, this.pagesNum);
    }

    public void prevPage() {
        this.page = Math.max(this.page - 1, 1);
    }

    @Override
    public String toString() {
        return String.format("page %d of %d", this.page, this.pagesNum);
    }

    public int setElemsPerPageNum(int elemsPerPageNum) {
        this.elemsPerPageNum = Math.max(Math.min(elemsPerPageNum, this.valuesNum), 1);
        return this.elemsPerPageNum;
    }

    private final ArrayList<ArrayList<Value>> pages;
    private int page, pagesNum, elemsPerPageNum, valuesNum;
}
