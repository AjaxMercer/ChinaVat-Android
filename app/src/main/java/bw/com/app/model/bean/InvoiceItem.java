package bw.com.app.model.bean;

/**
 * Created by codingsky on 15/12/23.
 */
public class InvoiceItem {

    public int invoiceId;
    public String documentId;
    public String presignedUrl;
    public boolean flag;
    public String invoiceType;
    public String invoiceNumber;
    public String supplierName;
    public String supplierBankAccount;
    public String buyerName;
    public String buyerBankAccount;
    public String buyerEmail;
    public String buyerPhone;
    public String Created;
    public String issuerName;
    public float totalExcTax;
    public float totalTax;
    public float totalIncTax;

    public InvoiceItem() {

    }

    public boolean isInclude(String s) {
        return s.equals(this.invoiceType) || s.equals(this.buyerPhone);
    }

    public boolean equals(InvoiceItem item) {
        return this.documentId.equals(item.documentId);
    }
}
